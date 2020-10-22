package org.azeroth.nalu;

public abstract   class NodeWhere {

    public NodeWhereSegment and(NodeWhere right){
        return new NodeWhereSegment(this,Logic.and,right);
    }

    public NodeWhereSegment or(NodeWhere right){
        return new NodeWhereSegment(this,Logic.or,right);
    }
}
