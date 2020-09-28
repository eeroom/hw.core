package org.azeroth;

import java.util.List;
import java.util.function.Function;

public class Query {

    NodeWhere where;
    NodeWhere having;
    public <T> DbSet<T> DbSet(Class<T> meta){
        return new DbSet<>(meta);
    }

    public Query Where(NodeWhere nw){
        if(this.where==null)
            this.where=nw;
        else
            this.where=this.where.and(nw);
        return this;
    }

    public Query Having(NodeWhere nw){
        if(this.having==null)
            this.having=nw;
        else
            this.having=this.having.and(nw);
        return this;
    }

    public <T> List<T> toList(Function<Object[],T> transfer){
        throw  new IllegalArgumentException("方法未完成");
    }

    public <T> List<T> toList(){
        return this.toList(x->(T)x[0]);
    }
}
