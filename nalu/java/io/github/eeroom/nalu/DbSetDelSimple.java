package io.github.eeroom.nalu;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DbSetDelSimple<T> extends TableSet<T> {
    WhereNode whereNode;
    DbSetDelSimple(Class<T> meta) {
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

    int execute(Connection cnn, ParseSqlContext context){
        context.lstDbParameter.clear();
        var strwhere="";
        if(this.whereNode!=null)
            strwhere="where "+ this.whereNode.parse(context);
        var sql=String.format("delete from %s %s",this.tableName,strwhere);
        try (var pts= cnn.prepareStatement(sql)){
            int pindex=1;
            for (var pp:context.lstDbParameter){
                pts.setObject(pindex++,pp.item2);
            }
            return pts.executeUpdate();
        }catch (Throwable throwable){
            throw  new ExecuteSqlException(sql,context.lstDbParameter.stream().map(x->x.item2).collect(Collectors.toList()),null,throwable);
        }
    }
}
