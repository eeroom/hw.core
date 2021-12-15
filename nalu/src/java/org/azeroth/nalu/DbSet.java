package org.azeroth.nalu;


import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DbSet<T> extends TableSet<T> {
    WhereNode whereNode;
    WhereNode havingNode;
    ArrayList<SelectNode> lstSelectNode =new ArrayList<>();
    ArrayList<OrderbyNode> lstOrderbyNode=new ArrayList<>();
    ArrayList<Column> lstGroupByNode=new ArrayList<>();
    DbContext dbContext;

    DbSet(){

    }

    DbSet(DbContext dbContext,Class<T> meta) throws Throwable {
        super(meta);
        this.dbContext=dbContext;
    }

    public <R> Column<R> col(Function<T,R> exp){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        exp.apply(this.entityProxy);
        Column<R> column=new Column((DbSet<?>) this.lstTarget.get(0).item1,this.lstTarget.get(0).item2);
        return column;
    }

    public DbSet<T> select(Function<T,Columns> exp){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        exp.apply(this.entityProxy);
        for (var item : this.lstTarget){
            var col=new Column<>((DbSet<?>) item.item1,item.item2);
            var snode=new SelectNode(col);
            ((DbSet<?>)item.item1).lstSelectNode.add(snode);
        }
        return this;
    }

    public DbSet<T> select(){
        var lst= dictGetMethod.get(this.meta.getName()).keySet().stream()
                .map(colName->new Column(this,colName))
                .map(col->new SelectNode(col))
                .collect(Collectors.toList());
        this.lstSelectNode.addAll(lst);
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

    public WhereNode whereByRaw(MyFunction2<String, ParseSqlContext,String> wherestr){
        var wh=new WhereNodeMyDefine(this,wherestr);
        return wh;
    }

    public <B,A> DbSet<Tuple.Tuple2<T,B>> join(DbSet<B> right, Function<T,A> leftcol,Function<B,A> rightcol){
            var dbset= new DbSetComplex<>(this,right,leftcol,rightcol,(x,y)->Tuple.create(x,y));
            dbset.dbContext=this.dbContext;
            return dbset;
    }

    public <B,C,A> DbSet<C> join(DbSet<B> right,  Function<T,A> leftcol,Function<B,A> rightcol,MyFunction2<T,B,C> mapper){
        var dbset= new DbSetComplex<>(this,right,leftcol,rightcol,mapper);
        dbset.dbContext=this.dbContext;
        return dbset;
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
        var col=new Column<>((DbSet<?>) wrapper.item1,wrapper.item2);
        this.lstOrderbyNode.add(new OrderbyNode(col,OrderbyOpt.asc));
        return this;
    }

    public <R> DbSet<T> orderByDescending(Function<T,R> handler){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        handler.apply(this.entityProxy);
        var wrapper=this.lstTarget.get(0);
        var col=new Column<>((DbSet<?>) wrapper.item1,wrapper.item2);
        this.lstOrderbyNode.add(new OrderbyNode(col,OrderbyOpt.desc));
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
        var col=new Column<>((DbSet<?>) wrapper.item1,wrapper.item2);
        this.lstGroupByNode.add(col);
        return this;
    }
}

