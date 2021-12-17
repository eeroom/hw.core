package io.github.eeroom.nalu;

import java.sql.Connection;
import java.util.function.Function;

public class DbSetDelSimple<T> extends TableSet<T> {
    WhereNode whereNode;
    DbSetDelSimple(Class<T> meta) throws Throwable {
        super(meta);
    }

    public <R> Column<R> col(Function<T,R> exp){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        exp.apply(this.entityProxy);
        Column<R> column=new Column(this.lstTarget.get(0).item1,this.lstTarget.get(0).item2);
        return column;
    }


    public DbSetDelSimple<T> where(Function<DbSetDelSimple<T>, WhereNode> pred){
        var wh= pred.apply(this);
        if(this.whereNode==null)
            this.whereNode=wh;
        else
            this.whereNode=this.whereNode.and(wh);
        return this;
    }

    int execute(Connection cnn, ParseSqlContext context) throws Throwable {
        context.lstDbParameter.clear();
        var strwhere="";
        if(this.whereNode!=null)
            strwhere="where "+ this.whereNode.parse(context);
        var sql=String.format("delete from %s %s",this.tableName,strwhere);
        var pts= cnn.prepareStatement(sql);
        int pindex=1;
        for (var pp:context.lstDbParameter){
            pts.setObject(pindex++,pp.item2);
        }
        return pts.executeUpdate();
    }
}
