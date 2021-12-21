package io.github.eeroom.nalu;

public class WhereNodeMyDefine<T> extends WhereNode {
    DbSet<T> dbSet;
    MyFunction2<String,ParseSqlContext,String> wherestr;
    public WhereNodeMyDefine(DbSet<T> dbSet, MyFunction2<String,ParseSqlContext,String> wherestr){
        this.dbSet=dbSet;
        this.wherestr=wherestr;
    }
    @Override
    public String parse(ParseSqlContext context) {
        var sql= this.wherestr.apply(this.dbSet.tableAlias,context);
        return sql;
    }
}
