package org.azeroth.nalu;

public class NodeWhereLeaf<T,C> extends NodeWhere {
    Column<T,C> col;
    ColOperator opt;
    Object value;
    public NodeWhereLeaf(Column<T,C> col,ColOperator opt,Object value){
        this.col=col;
        this.opt=opt;
        this.value=value;
    }


}
