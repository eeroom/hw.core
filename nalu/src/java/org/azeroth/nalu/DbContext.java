package org.azeroth.nalu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class DbContext {
    protected String rowCountFiledName = "_rowsCount";
    public <T> DbSet<T> DbSet(Class<T> meta) throws Throwable {
        return new DbSet<>(this,meta);
    }

    protected abstract Connection getConnection() throws SQLException;

    protected  <T> PagingList<T> toList(MyFunction<ResultSet,T> map,MyAction2<ParseSqlContext,Boolean> preparecontex) throws Throwable {
        ParseSqlContext context=new ParseSqlContext();
        preparecontex.execute(context,true);
        String cmdstr=this.parseSql(context);
        var cnn= this.getConnection();
        var pst= cnn.prepareStatement(cmdstr);
        for (var i = 1; i<=context.lstDbParameter.size(); i++){
            pst.setObject(i,context.lstDbParameter.get(i).item2);
        }
        var rs= pst.executeQuery();
        ArrayList<T> lst=new ArrayList<>();
        while (rs.next()){
            var obj= map.apply(rs);
            lst.add(obj);
        }
        var rt=new PagingList<T>();
        rt.lst= lst;
        return rt;
    }

    /**
     * 适用于mssqlserver，其他数据库请重新实现该方法
     * 这里的分页采用rownumber方式
     * @param context
     * @return
     */
   protected  String parseSql(ParseSqlContext context) {
        var lstselect=context.lstSelectNode.stream().map(x->x.parse(context)).collect(java.util.stream.Collectors.toList());
        String selectstr=String.join(",",lstselect);
        String fromstr=String.format("%s as %s",context.fromTable.tableName,context.fromTable.tableAlias);
        var joinstr="";
        if(context.lstJoinNode.size()>0)
            joinstr=String.join("\r\n",context.lstJoinNode.stream().map(x->x.parse(context)).collect(java.util.stream.Collectors.toList()));
        String wherestr="";
        if(context.whereNode!=null)
            wherestr="where "+context.whereNode.parse(context);
        String groupbystr="";
        if(context.lstGroupByNode.size()>0)
            groupbystr=String.join(",",context.lstGroupByNode.stream().map(x->x.parse(context)).collect(java.util.stream.Collectors.toList()));
        String havingstr="";
        if(context.havingNode!=null)
            havingstr=context.havingNode.parse(context);
        String orderbystr="";
        if(context.lstOrderByNode.size()>0)
            orderbystr=String.join(",",context.lstOrderByNode.stream().map(x->x.parse(context)).collect(java.util.stream.Collectors.toList()));
        if(context.takerows<1){//不分页
            var sql=String.format("select %s \r\n from %s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s",
                    selectstr,
                    fromstr,
                    joinstr,
                    wherestr,
                    groupbystr,
                    havingstr,
                    orderbystr);
            return sql;
        }else {
            var tmpRowIndex = "_theRowIndex";
            var cmdstr =String.format("select %s,ROW_NUMBER() OVER(%s) AS %s \r\n from %s\r\n%s\r\n%s\r\n%s\r\n%s",
                    selectstr,
                    orderbystr,
                    tmpRowIndex,
                    fromstr,
                    joinstr,
                    wherestr,
                    groupbystr,
                    havingstr);
            var sql = String.format("with htt AS(\r\n%s),\r\n hbb AS(\r\n select COUNT(0) AS %s from htt)\r\n select htt.*,hbb.* from htt,hbb WHERE htt.%s BETWEEN %s AND %s",
                    cmdstr,
                    this.rowCountFiledName,
                    tmpRowIndex,
                    String.valueOf(context.skiprows),
                    String.valueOf(context.skiprows+context.takerows-1));
            return sql;
        }
    }
}