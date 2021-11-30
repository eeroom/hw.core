package org.azeroth.nalu;

import java.sql.ResultSet;
import java.util.function.Function;

public class DbSetComplex<T,B,C> extends DbSet<C> {
    DbSet<T> left;
    DbSet<B> rigth;
    MyFunction2<T,B,C> mapper;
    public DbSetComplex(DbSet<T> left,DbSet<B> rigth,MyFunction2<T,B,C> mapper){
        this.left=left;
        this.rigth=rigth;
        this.mapper=mapper;
        this.entityProxy=this.mapper.apply(left.entityProxy,rigth.entityProxy);
    }

    @Override
    public void setInvokeCallback(MyAction2<DbSet<?>, String> callback) {
        this.lstTarget.clear();
        this.left.setInvokeCallback(callback);
        this.rigth.setInvokeCallback(callback);
    }

    @Override
    public C apply(ResultSet resultSet) throws Throwable {
        var obj= this.mapper.apply(this.left.apply(resultSet),this.rigth.apply(resultSet));
        return obj;

    }
}
