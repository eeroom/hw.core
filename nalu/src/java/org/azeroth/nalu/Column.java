package org.azeroth.nalu;

public class Column<C> implements IParseSql {

    DbSet<?> dbSet;
    String colName;

    public Column(DbSet<?> dbSet,String colName){
        this.dbSet=dbSet;
        this.colName=colName;
    }

    public WhereNodeLeaf<C> lt(C value){
        return new WhereNodeLeaf<>(this,ColOperator.lt,value);
    }

    public WhereNodeLeaf<C> gt(C value){
        return new WhereNodeLeaf<>(this,ColOperator.gt,value);
    }

    public WhereNodeLeaf<C> in(C... value){
        return new WhereNodeLeaf<>(this,ColOperator.in,value);
    }

    public WhereNodeLeaf<C> like(C value){
        return new WhereNodeLeaf<>(this,ColOperator.like,value);
    }

    public WhereNode eq(C value){
        return new WhereNodeLeaf<>(this,ColOperator.eq,value);
    }
    public WhereNode eq(Column<C> column){
        return  new WhereJoinOnNode(this,column);
    }
    public WhereNodeLeaf<C> between(C... value){
        return new WhereNodeLeaf<>(this,ColOperator.between,value);
    }

    @Override
    public String parse(ParseSqlContext context) {
        var sql= this.dbSet.tableAlias+"."+this.colName;
        return sql;
    }
}
