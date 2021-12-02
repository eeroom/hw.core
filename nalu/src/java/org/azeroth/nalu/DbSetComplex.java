package org.azeroth.nalu;

import java.sql.ResultSet;

public class DbSetComplex<T,B,C> extends DbSet<C> {
    DbSet<T> left;
    DbSet<B> rigth;
    MyFunction2<T,B,C> mapper;
    WhereNode joinjw;
    JoinOpt joinOpt;
    DbSetComplex(DbSet<T> left,DbSet<B> rigth,MyFunction2<DbSet<T>,DbSet<B>, WhereNode> on,MyFunction2<T,B,C> mapper){
        this.dbContext=dbContext;
        this.left=left;
        this.rigth=rigth;
        this.mapper=mapper;
        this.entityProxy=this.mapper.apply(left.entityProxy,rigth.entityProxy);
        this.joinjw=on.apply(this.left,this.rigth);
        this.joinOpt=JoinOpt.inner;
    }

    @Override
    void setProxyHookHandler(MyAction2<TableSet<?>, String> handler) {
        this.lstTarget.clear();
        this.left.setProxyHookHandler(handler);
        this.rigth.setProxyHookHandler(handler);
    }

    @Override
    C map(ResultSet resultSet) throws Throwable {
        var obj= this.mapper.apply(this.left.map(resultSet),this.rigth.map(resultSet));
        return obj;

    }

    @Override
    void initParseSqlContext(ParseSqlContext context, boolean initLeftTable) {
        this.left.initParseSqlContext(context,initLeftTable);
        this.rigth.initParseSqlContext(context,false);
        context.whereNode=this.addWhereNode(context.whereNode,this.whereNode);
        context.havingNode=this.addWhereNode(context.havingNode,this.havingNode);
        context.lstJoinNode.add(new JoinNode(this.rigth,this.joinjw,this.joinOpt));
        context.lstGroupByNode.addAll(this.lstGroupByNode);
        context.lstOrderByNode.addAll(this.lstOrderbyNode);
        context.takerows=this.takerows;
        context.skiprows=this.skiprows;
    }
}
