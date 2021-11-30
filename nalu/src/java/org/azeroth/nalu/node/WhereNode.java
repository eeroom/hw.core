package org.azeroth.nalu.node;

import org.azeroth.nalu.IParseSql;
import org.azeroth.nalu.Logic;
import org.azeroth.nalu.ParseSqlContext;

public abstract   class WhereNode implements IParseSql {

    public WhereNodeSegment and(WhereNode right){
        return new WhereNodeSegment(this, Logic.and,right);
    }

    public WhereNodeSegment or(WhereNode right){
        return new WhereNodeSegment(this,Logic.or,right);
    }


}
