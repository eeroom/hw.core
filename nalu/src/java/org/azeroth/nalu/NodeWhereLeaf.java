package org.azeroth.nalu;

public class NodeWhereLeaf<C> extends NodeWhere {
    Column<C> col;
    ColOperator opt;
    Object value;
    public NodeWhereLeaf(Column<C> col,ColOperator opt,Object value){
        this.col=col;
        this.opt=opt;
        this.value=value;
    }


}
