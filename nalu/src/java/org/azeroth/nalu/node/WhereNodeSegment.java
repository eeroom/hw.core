package org.azeroth.nalu.node;

import org.azeroth.nalu.Logic;
import org.azeroth.nalu.ParseSqlContext;

public class WhereNodeSegment extends WhereNode {

    WhereNode left;
    Logic logic;
    WhereNode right;
    public WhereNodeSegment(WhereNode left, Logic logic, WhereNode right){
        this.left=left;
        this.right=right;
        this.logic=logic;
    }

    @Override
    public String parse(ParseSqlContext context) {
        if(this.logic==Logic.and )
            return  this.left.parse(context) +" and "+this.right.parse(context);
        return String.format("(%s or %s)",this.left.parse(context),this.right.parse(context));
    }
}
