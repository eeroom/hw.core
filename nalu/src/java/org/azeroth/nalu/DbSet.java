package org.azeroth.nalu;

import java.util.ArrayList;
import java.util.function.Function;

public class DbSet<T> implements MyAction2<DbSet<?>,String> {
    T entityProxy;
    T entityIdentity;
    Class<T> meta;
    PropertyNameHandler handler;
    String tableName;
    String tableAlias;
    ArrayList<NodeWhere> lstwh=new ArrayList<>();

    public DbSet(){

    }

    public DbSet(Class<T> meta) throws Throwable {
        this.meta=meta;
        this.tableName=this.meta.getName();
        this.entityIdentity=meta.getConstructor(null).newInstance(null);
        this.handler=new PropertyNameHandler(this,this.entityIdentity);
        //使用cglib创建class的代理对象，java自带的代理类只能创建基于接口的代理对象，
        //这里是创建数据库model的pojo对象的代理，没有接口，所以需要使用cglib的
        this.entityProxy= (T)org.springframework.cglib.proxy.Enhancer.create(meta,this.handler);
    }

    public <R> Column<R> col(Function<T,R> exp){
        this.handler.invokeCallback=this;
        exp.apply(this.entityProxy);
        Column<R> column=new Column(this.targetdbset,this.targetCol);
        return column;
    }

    public DbSet<T> where(Function<DbSet<T>,NodeWhere> pred){
        var wh= pred.apply(this);
        this.lstwh.add(wh);
        return this;
    }

    public <B> DbSet<Tuple.Tuple2<T,B>> join(DbSet<B> right, MyFunction2<DbSet<T>,DbSet<B>,NodeWhere> on){
            var entityProxy=Tuple.create(this.entityProxy,right.entityProxy);
            var dbset= new DbSetComplex<>(this,right,entityProxy);
            return dbset;
    }

    //这是为了涉及where条件的时候，执行lambda的时候，得到该lamda对应的表和列
    DbSet<?> targetdbset;
    String targetCol;
    //这是为了涉及where条件的时候，执行lambda的时候，得到该lamda对应的表和列
    @Override
    public void execute(DbSet<?> dbSet, String s) {
        this.targetdbset=targetdbset;
        this.targetCol=targetCol;
    }
}

