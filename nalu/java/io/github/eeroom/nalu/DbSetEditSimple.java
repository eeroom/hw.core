package io.github.eeroom.nalu;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DbSetEditSimple<T> extends TableSet<T> {
    WhereNode whereNode;
    ArrayList<Tuple.Tuple2<Column,Object>> lstUpdateCol=new ArrayList<>();
    DbSetEditSimple(Class<T> meta) throws Throwable {
        super(meta);
    }

    public DbSetEditSimple<T> where(Function<DbSetEditSimple<T>, WhereNode> pred){
        var wh= pred.apply(this);
        if(this.whereNode==null)
            this.whereNode=wh;
        else
            this.whereNode=this.whereNode.and(wh);
        return this;
    }

    public <R> Column<R> col(Function<T,R> exp){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        exp.apply(this.entityProxy);
        Column<R> column=new Column(this.lstTarget.get(0).item1,this.lstTarget.get(0).item2);
        return column;
    }

    public <R> DbSetEditSimple<T> setUpdateCol(Function<T,R> exp,R value){
        var col= this.col(exp);
        lstUpdateCol.add(Tuple.create(col,value));
        return this;
    }

    int execute(Connection cnn, ParseSqlContext context) throws Throwable {
        if(this.lstUpdateCol.size()<1)
            throw  new IllegalArgumentException("必须指定update的目标列");
        var lstset= lstUpdateCol.stream().map(x->x.item1.colName+"=?").collect(Collectors.toList());
        var strSet=String.join(",",lstset);
        context.lstDbParameter.clear();
        var strWhere="";
        if(this.whereNode!=null)
            strWhere="where "+this.whereNode.parse(context);
        var sql=String.format("update %s set %s %s",this.tableName,strSet,strWhere);
        var pst=cnn.prepareStatement(sql);
        int pindex=1;
        for (var ucol:lstUpdateCol){
            pst.setObject(pindex++,ucol.item2);
        }
        for (var pp:context.lstDbParameter){
            pst.setObject(pindex++,pp.item2);
        }
        return pst.executeUpdate();
    }
}
