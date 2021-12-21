package io.github.eeroom.nalu;

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
