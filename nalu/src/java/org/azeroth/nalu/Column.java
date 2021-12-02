package org.azeroth.nalu;

public class Column<C> implements IParseSql {

    DbSet<?> dbSet;
    String colName;

    public Column(DbSet<?> dbSet,String colName){
        this.dbSet=dbSet;
        this.colName=colName;
    }

    public WhereNodeLeaf<C> lt(C value){
        return new WhereNodeLeaf<>(this, ColOpt.lt,value);
    }

    public WhereNodeLeaf<C> gt(C value){
        return new WhereNodeLeaf<>(this, ColOpt.gt,value);
    }

    public WhereNodeLeaf<C> in(C... value){
        return new WhereNodeLeaf<>(this, ColOpt.in,value);
    }

    public WhereNodeLeaf<C> like(C value){
        return new WhereNodeLeaf<>(this, ColOpt.like,value);
    }

    public WhereNode eq(C value){
        return new WhereNodeLeaf<>(this, ColOpt.eq,value);
    }
    public WhereNode eq(Column<C> column){
        return  new WhereJoinOnNode(this,column);
    }
    public WhereNodeLeaf<C> between(C... value){
        return new WhereNodeLeaf<>(this, ColOpt.between,value);
    }

    @Override
    public String parse(ParseSqlContext context) {
        var sql= this.dbSet.tableAlias+"."+this.colName;
        return sql;
    }

    public ColumnByFunction<C> max(){
        return new ColumnByFunction<>(this.dbSet,this.colName,FunctionOpt.max);
    }

    public ColumnByFunction<C> min(){
        return new ColumnByFunction<>(this.dbSet,this.colName,FunctionOpt.min);
    }

    public ColumnByFunction<C> avg(){
        return new ColumnByFunction<>(this.dbSet,this.colName,FunctionOpt.avg);
    }

    public ColumnByFunction<C> lower(){
        return new ColumnByFunction<>(this.dbSet,this.colName,FunctionOpt.lower);
    }

    public ColumnByFunction<C> upper(){
        return new ColumnByFunction<>(this.dbSet,this.colName,FunctionOpt.upper);
    }

    public ColumnByFunction<C> sum(){
        return new ColumnByFunction<>(this.dbSet,this.colName,FunctionOpt.sum);
    }

    public ColumnByFunction<C> count(){
        return new ColumnByFunction<>(this.dbSet,this.colName,FunctionOpt.count);
    }

    public ColumnByMyFunction mydefine(MyFunction2<String,ParseSqlContext,String> myfunction){
        return new ColumnByMyFunction(this.dbSet,this.colName,myfunction);
    }
}
