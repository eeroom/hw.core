package io.github.eeroom.nalu;

public class WhereNodeSegment extends WhereNode {

    WhereNode left;
    LogicOpt logicOpt;
    WhereNode right;
    public WhereNodeSegment(WhereNode left, LogicOpt logicOpt, WhereNode right){
        this.left=left;
        this.right=right;
        this.logicOpt = logicOpt;
    }

    @Override
    public String parse(ParseSqlContext context) {
        if(this.logicOpt == LogicOpt.and )
            return String.format("%s %s %s",this.left.parse(context),this.logicOpt.getSql(),this.right.parse(context));
        return String.format("(%s %s %s)",this.left.parse(context),this.logicOpt.getSql(),this.right.parse(context));
    }
}
