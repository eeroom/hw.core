package org.azeroth.nalu;

public class ColumnByMyFunction<C> extends Column<C> {
    MyFunction2<String,ParseSqlContext,String> myfunction;

    public ColumnByMyFunction(DbSet<?> dbSet, String colName, MyFunction2<String,ParseSqlContext,String> myfunction) {
        super(dbSet, colName);
        this.myfunction=myfunction;
    }

    @Override
    public String parse(ParseSqlContext context) {
        return this.myfunction.apply(super.parse(context),context);
    }
}
