package io.github.eeroom.nalu;

import java.sql.ResultSet;
import java.util.function.Function;

public class DbSetComplex<T,B,C,A> extends DbSet<C> {
    DbSet<T> left;
    DbSet<B> rigth;
    MyFunction2<T,B,C> mapper;
    WhereNode joinjw;
    JoinOpt joinOpt;
    DbSetComplex(DbSet<T> left, DbSet<B> rigth, Function<T,A> leftcol, Function<B,A> rightcol, MyFunction2<T,B,C> mapper){
        this.dbContext=dbContext;
        this.left=left;
        this.rigth=rigth;
        this.mapper=mapper;
        this.entityProxy=this.mapper.apply(left.entityProxy,rigth.entityProxy);
        this.joinOpt=JoinOpt.inner;
        this.left.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        this.joinjw= left.col(leftcol).eq(rigth.col(rightcol));
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
