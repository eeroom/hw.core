package io.github.eeroom.nalu;

public class ColumnByFunction<C> extends Column<C> {
    FunctionOpt functionOpt;
    public ColumnByFunction(TableSet<?> dbSet, String colName,FunctionOpt functionOpt) {
        super(dbSet, colName);
        this.functionOpt=functionOpt;
    }

    @Override
    public String parse(ParseSqlContext context) {
        var colName= super.parse(context);
        var sql=String.format("%s(%s)",this.functionOpt.getSql(),colName);
        return sql;
    }
}
