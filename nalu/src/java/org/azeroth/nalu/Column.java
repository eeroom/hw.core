package org.azeroth.nalu;

public class Column<T,C> {

    DbSet<T> dbSet;
String colName;
    public Column(DbSet<T> dbSet,String colName){
        this.dbSet=dbSet;
        this.colName=colName;
    }

    public NodeWhereLeaf<T,C> lt(C value){
        return new NodeWhereLeaf<>(this,ColOperator.lt,value);
    }

    public NodeWhereLeaf<T,C> gt(C value){
        return new NodeWhereLeaf<>(this,ColOperator.gt,value);
    }

    public NodeWhereLeaf<T,C> in(C... value){
        return new NodeWhereLeaf<>(this,ColOperator.in,value);
    }

    public NodeWhereLeaf<T,C> like(C value){
        return new NodeWhereLeaf<>(this,ColOperator.like,value);
    }

    public NodeWhere eq(C value){
        return new NodeWhereLeaf<>(this,ColOperator.eq,value);
    }
    public NodeWhereLeaf<T,C> between(C... value){
        return new NodeWhereLeaf<>(this,ColOperator.between,value);
    }
}
