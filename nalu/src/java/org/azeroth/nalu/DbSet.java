package org.azeroth.nalu;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class DbSet<T> {
    T entityProxy;
    T entityIdentity;
    Class<T> meta;
    PropertyNameHandler handler;
    String tableName;
    String tableAlias;
    WhereNode whereNode;
    WhereNode havingNode;
    ArrayList<SelectNode> lstSelectNode =new ArrayList<>();
    ArrayList<OrderbyNode> lstOrderbyNode=new ArrayList<>();
    ArrayList<Column> lstGroupByNode=new ArrayList<>();
    DbContext dbContext;
    static HashMap<String,HashMap<String,Method>> dictSetMethod=new HashMap<>();
    static HashMap<String,HashMap<String,Method>> dictGetMethod=new HashMap<>();

    public DbSet(){

    }

    DbSet(DbContext dbContext,Class<T> meta) throws Throwable {
        this.meta=meta;
        if(dictSetMethod.get(meta.getName())==null){
            HashMap<String,Method> dictset=new HashMap<>();
            HashMap<String,Method> dictget=new HashMap<>();
            var lstmethod=meta.getDeclaredMethods();
            for (var mt:lstmethod){
                if(mt.getName().startsWith("set")){
                    dictset.put(mt.getName().substring(3),mt);
                }else  if(mt.getName().startsWith("get")){
                    dictget.put(mt.getName().substring(3),mt);
                }
            }
            dictSetMethod.put(meta.getName(),dictset);
            dictGetMethod.put(meta.getName(),dictget);
        }
        this.tableName=this.meta.getSimpleName();
        this.entityIdentity=meta.getConstructor(null).newInstance(null);
        this.handler=new PropertyNameHandler(this,this.entityIdentity);
        //使用cglib创建class的代理对象，java自带的代理类只能创建基于接口的代理对象，
        //这里是创建数据库model的pojo对象的代理，没有接口，所以需要使用cglib的
        this.entityProxy= (T)org.springframework.cglib.proxy.Enhancer.create(meta,this.handler);
        this.dbContext=dbContext;
    }

    public <R> Column<R> col(Function<T,R> exp){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        exp.apply(this.entityProxy);
        Column<R> column=new Column(this.lstTarget.get(0).item1,this.lstTarget.get(0).item2);
        return column;
    }

    public <R> DbSet<T> select(Function<T,Columns> exp){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        exp.apply(this.entityProxy);
        for (var item : this.lstTarget){
            var col=new Column<>(item.item1,item.item2);
            var snode=new SelectNode(col);
            item.item1.lstSelectNode.add(snode);
        }
        return this;
    }

    public DbSet<T> where(Function<DbSet<T>, WhereNode> pred){
        var wh= pred.apply(this);
        if(this.whereNode==null)
            this.whereNode=wh;
        else
            this.whereNode=this.whereNode.and(wh);
        return this;
    }

    public <B> DbSet<Tuple.Tuple2<T,B>> join(DbSet<B> right, MyFunction2<DbSet<T>,DbSet<B>, WhereNode> on){
            var dbset= new DbSetComplex<>(this,right,on,(x,y)->Tuple.create(x,y));
            dbset.dbContext=this.dbContext;
            return dbset;
    }

    public <B,C> DbSet<C> join(DbSet<B> right, MyFunction2<DbSet<T>,DbSet<B>, WhereNode> on,MyFunction2<T,B,C> mapper){
        var dbset= new DbSetComplex<>(this,right,on,mapper);
        dbset.dbContext=this.dbContext;
        return dbset;
    }

    //这是为了涉及where条件的时候，执行lambda的时候，得到该lamda对应的表和列
    ArrayList<Tuple.Tuple2<DbSet<?>,String>> lstTarget=new ArrayList<>();
    //这是为了涉及where条件的时候，执行lambda的时候，得到该lamda对应的表和列
    void onProxyHandlerInvokedHandler(DbSet<?> dbSet, String s) {
        this.lstTarget.add(Tuple.create(dbSet,s));
    }

    void setProxyHookHandler(MyAction2<DbSet<?>,String> handler){
        this.lstTarget.clear();
        this.handler.onInvoked =handler;
    }

    public  List<T> toList() throws Throwable {
        var wrapper=this.dbContext.toList(this::map,(x, y)->this.initParseSqlContext(x,y));
        return wrapper.getLst();
    }

    public <R>  List<R> toList(MyFunction<T,R> mapper) throws Throwable {
        var wrapper=this.dbContext.toList(x->mapper.apply(this.map(x)),(x, y)->this.initParseSqlContext(x,y));
        return wrapper.getLst();
    }

    public  PagingList<T> toListByPaging() throws Throwable {
        var lst=this.dbContext.toList(this::map,(x, y)->this.initParseSqlContext(x,y));
        return lst;
    }

    public <R>   PagingList<R> toListByPaging(MyFunction<T,R> mapper) throws Throwable {
        var wrapper=this.dbContext.toList(x->mapper.apply(this.map(x)),(x, y)->this.initParseSqlContext(x,y));
        return wrapper;
    }

    T map(ResultSet resultSet) throws Throwable {
        var obj=this.meta.getConstructor(null).newInstance(null);
        var dict=dictSetMethod.get(this.meta.getName());
        for (var snode:this.lstSelectNode){
            var value= resultSet.getObject(snode.nameNick);
            dict.get(snode.column.colName).invoke(obj,value);
        }
        return  obj;
    }

    void  initParseSqlContext(ParseSqlContext context,boolean initLeftTable){
        this.tableAlias="T"+context.nextTableIndex();
        context.lstTable.add(this);
        context.whereNode=this.addWhereNode(context.whereNode,this.whereNode);
        context.havingNode=this.addWhereNode(context.havingNode,this.havingNode);
        if(initLeftTable)
            context.fromTable=this;
        this.lstSelectNode.forEach(x->x.index=context.nextColIndex());
        this.lstSelectNode.forEach(x->x.nameNick="c"+x.index);
        context.lstSelectNode.addAll(this.lstSelectNode);
        context.lstGroupByNode.addAll(this.lstGroupByNode);
        context.lstOrderByNode.addAll(this.lstOrderbyNode);
        context.takerows=this.takerows;
        context.skiprows=this.skiprows;
    }

    WhereNode addWhereNode(WhereNode left, WhereNode right) {
        if(left==null)
            return right;
        if(right==null)
            return left;
        return left.and(right);
    }

    int skiprows;
    int takerows;
    public DbSet<T> skipTake(int skiprows,int takerows){
        if(skiprows<0)
            throw  new IllegalArgumentException("skiprows不能小于0");
        if(takerows<=0)
            throw new IllegalArgumentException("takerows不能小于1");
        this.skiprows=skiprows;
        this.takerows=takerows;
        return this;
    }

    public <R> DbSet<T> orderBy(Function<T,R> handler){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        handler.apply(this.entityProxy);
        var wrapper=this.lstTarget.get(0);
        var col=new Column<>(wrapper.item1,wrapper.item2);
        wrapper.item1.lstOrderbyNode.add(new OrderbyNode(col,OrderbyOpt.asc));
        return this;
    }

    public <R> DbSet<T> orderByDescending(Function<T,R> handler){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        handler.apply(this.entityProxy);
        var wrapper=this.lstTarget.get(0);
        var col=new Column<>(wrapper.item1,wrapper.item2);
        wrapper.item1.lstOrderbyNode.add(new OrderbyNode(col,OrderbyOpt.desc));
        return this;
    }

    public DbSet<T> having(Function<DbSet<T>, WhereNode> pred){
        var wh= pred.apply(this);
        if(this.havingNode==null)
            this.havingNode=wh;
        else
            this.havingNode=this.havingNode.and(wh);
        return this;
    }

    public <R> DbSet<T> groupBy(Function<T,R> handler){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        handler.apply(this.entityProxy);
        var wrapper=this.lstTarget.get(0);
        var col=new Column<>(wrapper.item1,wrapper.item2);
        wrapper.item1.lstGroupByNode.add(col);
        return this;
    }


}

