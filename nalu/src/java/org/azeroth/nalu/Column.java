package org.azeroth.nalu;

public class Column<C> {

    DbSet<?> dbSet;
String colName;
    public Column(DbSet<?> dbSet,String colName){
        this.dbSet=dbSet;
        this.colName=colName;
    }

    public NodeWhereLeaf<C> lt(C value){
        return new NodeWhereLeaf<>(this,ColOperator.lt,value);
    }

    public NodeWhereLeaf<C> gt(C value){
        return new NodeWhereLeaf<>(this,ColOperator.gt,value);
    }

    public NodeWhereLeaf<C> in(C... value){
        return new NodeWhereLeaf<>(this,ColOperator.in,value);
    }

    public NodeWhereLeaf<C> like(C value){
        return new NodeWhereLeaf<>(this,ColOperator.like,value);
    }

    public NodeWhere eq(C value){
        return new NodeWhereLeaf<>(this,ColOperator.eq,value);
    }
    public NodeWhereLeaf<C> between(C... value){
        return new NodeWhereLeaf<>(this,ColOperator.between,value);
    }
}
