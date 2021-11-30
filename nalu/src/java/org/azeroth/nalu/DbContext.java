package org.azeroth.nalu;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

public class DbContext {

    NodeWhere where;
    NodeWhere having;
    public <T> DbSet<T> DbSet(Class<T> meta) throws Throwable {
        return new DbSet<>(meta,this);
    }

    public <T> List<T> toList(MyFunction<ResultSet,T> map){
        return null;
    }
}
