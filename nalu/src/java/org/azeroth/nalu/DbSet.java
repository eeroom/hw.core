package org.azeroth.nalu;

import org.azeroth.nalu.node.SelectNode;
import org.azeroth.nalu.node.WhereNode;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DbSet<T> implements MyAction2<DbSet<?>,String>,MyFunction<ResultSet,T> {
    T entityProxy;
    T entityIdentity;
    Class<T> meta;
    PropertyNameHandler handler;
    public String tableName;
    public String tableAlias;
    WhereNode whereNode;
    ArrayList<SelectNode> lstselect=new ArrayList<>();
    DbContext dbContext;
    public DbSet(){

    }

    public DbSet(Class<T> meta,DbContext dbContext) throws Throwable {
        this.meta=meta;
        this.tableName=this.meta.getSimpleName();
        this.entityIdentity=meta.getConstructor(null).newInstance(null);
        this.handler=new PropertyNameHandler(this,this.entityIdentity);
        //使用cglib创建class的代理对象，java自带的代理类只能创建基于接口的代理对象，
        //这里是创建数据库model的pojo对象的代理，没有接口，所以需要使用cglib的
        this.entityProxy= (T)org.springframework.cglib.proxy.Enhancer.create(meta,this.handler);
        this.dbContext=dbContext;
    }

    public <R> Column<R> col(Function<T,R> exp){
        this.setInvokeCallback(this);
        exp.apply(this.entityProxy);
        Column<R> column=new Column(this.lstTarget.get(0).item1,this.lstTarget.get(0).item2);
        return column;
    }

    public <R> DbSet<T> select(Function<T,Columns> exp){
        this.setInvokeCallback(this);
        exp.apply(this.entityProxy);
        for (var item : this.lstTarget){
            var col=new Column<>(item.item1,item.item2);
            var snode=new SelectNode(col);
            item.item1.lstselect.add(snode);
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

    //这是为了涉及where条件的时候，执行lambda的时候，得到该lamda对应的表和列
    ArrayList<Tuple.Tuple2<DbSet<?>,String>> lstTarget=new ArrayList<>();
    //这是为了涉及where条件的时候，执行lambda的时候，得到该lamda对应的表和列
    @Override
    public void execute(DbSet<?> dbSet, String s) {
        this.lstTarget.add(Tuple.create(dbSet,s));
    }

    public void  setInvokeCallback(MyAction2<DbSet<?>,String> callback){
        this.lstTarget.clear();
        this.handler.invokeCallback=callback;
    }

    public  List<T> toList() throws Throwable {
        int a=3;
        var lst=this.dbContext.toList(this,(x,y)->this.initParseSqlContext(x,y));
        return lst;
    }


    @Override
    public T apply(ResultSet resultSet) throws Throwable {
        var obj=this.meta.getConstructor(null).newInstance(null);

        return  obj;
    }

    void  initParseSqlContext(ParseSqlContext context,boolean initLeftTable){
        this.tableAlias="T"+context.nextTableIndex();
        context.lstTable.add(this);
        context.whereNode=this.addWhereNode(context.whereNode,this.whereNode);
        if(initLeftTable)
            context.fromTable=this;
        this.lstselect.forEach(x->x.index=context.nextColIndex());
        this.lstselect.forEach(x->x.nameNick="c"+x.index);
        context.lstSelectNode.addAll(this.lstselect);
    }

    WhereNode addWhereNode(WhereNode left, WhereNode right) {
        if(left==null)
            return right;
        if(right==null)
            return left;
        return left.and(right);
    }
}

