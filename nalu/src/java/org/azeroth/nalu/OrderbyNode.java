package org.azeroth.nalu;

public class OrderbyNode implements  IParseSql {

    Column<?> column;
    OrderbyOpt orderbyOpt;
    public OrderbyNode(Column<?> column,OrderbyOpt orderbyOpt){
        this.column=column;
        this.orderbyOpt=orderbyOpt;
    }

    @Override
    public String parse(ParseSqlContext context) {
        var sql=String.format("%s %s",this.column.parse(context),this.orderbyOpt.getSql());
        return sql;
    }
}
