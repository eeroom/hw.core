package org.azeroth.nalu;

import java.util.function.Function;

public class DbSet<T> {
    T entityProxy;
    Class<T> meta;
    PropertyNameHandler handler;
    String tableName;
    String tableAlias;
    public DbSet(Class<T> meta){
        this.meta=meta;
        this.tableName=this.meta.getName();
        this.handler=new PropertyNameHandler();
        //使用cglib创建class的代理对象，java自带的代理类只能创建基于接口的代理对象，
        //这里是创建数据库model的pojo对象的代理，没有接口，所以需要使用cglib的
        this.entityProxy= (T)org.springframework.cglib.proxy.Enhancer.create(meta,this.handler);
    }

    public <R> Column<T,R> col(Function<T,R> exp){
        exp.apply(this.entityProxy);
        String colName=this.handler.getName();
        Column<T,R> column=new Column<>(this,colName);
        return column;
    }


}

