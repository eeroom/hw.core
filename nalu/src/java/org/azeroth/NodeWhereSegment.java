package org.azeroth;

public class NodeWhereSegment extends  NodeWhere {

    NodeWhere left;
    Logic logic;
    NodeWhere right;
    public NodeWhereSegment(NodeWhere left,Logic logic,NodeWhere right){
        this.left=left;
        this.right=right;
        this.logic=logic;
    }
}
