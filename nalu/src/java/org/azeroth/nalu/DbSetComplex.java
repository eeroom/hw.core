package org.azeroth.nalu;

import org.azeroth.nalu.node.JoinNode;
import org.azeroth.nalu.node.WhereNode;

import java.sql.ResultSet;
import java.util.function.Function;

public class DbSetComplex<T,B,C> extends DbSet<C> {
    DbSet<T> left;
    DbSet<B> rigth;
    MyFunction2<T,B,C> mapper;
    WhereNode joinjw;
    public DbSetComplex(DbSet<T> left,DbSet<B> rigth,MyFunction2<DbSet<T>,DbSet<B>, WhereNode> on,MyFunction2<T,B,C> mapper){
        this.dbContext=dbContext;
        this.left=left;
        this.rigth=rigth;
        this.mapper=mapper;
        this.entityProxy=this.mapper.apply(left.entityProxy,rigth.entityProxy);
        this.joinjw=on.apply(this.left,this.rigth);
    }

    @Override
    public void setInvokeCallback(MyAction2<DbSet<?>, String> callback) {
        this.lstTarget.clear();
        this.left.setInvokeCallback(callback);
        this.rigth.setInvokeCallback(callback);
    }

    @Override
    public C apply(ResultSet resultSet) throws Throwable {
        var obj= this.mapper.apply(this.left.apply(resultSet),this.rigth.apply(resultSet));
        return obj;

    }

    @Override
    void initParseSqlContext(ParseSqlContext context, boolean initLeftTable) {
        this.left.initParseSqlContext(context,initLeftTable);
        this.rigth.initParseSqlContext(context,false);
        context.whereNode=this.addWhereNode(context.whereNode,this.whereNode);
        context.lstJoin.add(new JoinNode(this.rigth,this.joinjw,JoinOpt.inner));
    }
}
