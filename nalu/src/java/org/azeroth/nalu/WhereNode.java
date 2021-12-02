package org.azeroth.nalu;

public abstract   class WhereNode implements IParseSql {

    public WhereNodeSegment and(WhereNode right){
        return new WhereNodeSegment(this, LogicOpt.and,right);
    }

    public WhereNodeSegment or(WhereNode right){
        return new WhereNodeSegment(this, LogicOpt.or,right);
    }

}
