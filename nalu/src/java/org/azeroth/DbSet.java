package org.azeroth;

import java.lang.reflect.Method;
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
        this.entityProxy= (T)org.springframework.cglib.proxy.Enhancer.create(meta,this.handler);
    }

    public <R> Column<T,R> col(Function<T,R> exp){
        exp.apply(this.entityProxy);
        String colName=this.handler.getName();
        Column<T,R> column=new Column<>(this,colName);
        return column;
    }


}

