package org.azeroth.nalu.node;

import org.azeroth.nalu.Column;
import org.azeroth.nalu.ParseSqlContext;

public class WhereJoinOnNode extends WhereNode {
    Column left;
    Column right;
    public WhereJoinOnNode(Column left,Column right){
        this.left=left;
        this.right=right;
    }

    @Override
    public String parse(ParseSqlContext context) {
        var sql=this.left.parse(context)+" = "+this.right.parse(context);
        return sql;
    }
}
