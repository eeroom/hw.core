package org.azeroth.nalu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class DbContext {

    public <T> DbSet<T> DbSet(Class<T> meta) throws Throwable {
        return new DbSet<>(this,meta);
    }

    protected abstract Connection getConnection() throws SQLException;

    protected  <T> List<T> toList(MyFunction<ResultSet,T> map,MyAction2<ParseSqlContext,Boolean> preparecontex) throws Throwable {
        ParseSqlContext context=new ParseSqlContext();
        preparecontex.execute(context,true);
        String cmdstr=this.parseSql(context);
        var cnn= this.getConnection();
        var pst= cnn.prepareStatement(cmdstr);
        for (var i=1;i<=context.dictParameter.size();i++){
            pst.setObject(i,context.dictParameter.get(i).item2);
        }
        var rs= pst.executeQuery();
        ArrayList<T> lst=new ArrayList<>();
        while (rs.next()){
            var obj= map.apply(rs);
            lst.add(obj);
        }
        return lst;
    }

   protected  String parseSql(ParseSqlContext context) {
        var lstselect=context.lstSelectNode.stream().map(x->x.parse(context)).collect(java.util.stream.Collectors.toList());
        String selectstr=String.join(",",lstselect);
        String fromstr=String.format("%s as %s",context.fromTable.tableName,context.fromTable.tableAlias);
        var lstjoin=context.lstJoin.stream().map(x->x.parse(context)).collect(java.util.stream.Collectors.toList());
        var joinstr="";
        if(lstjoin.size()>0)
            joinstr=String.join("\r\n",lstjoin);
        String wherestr="";
        if(context.whereNode!=null)
            wherestr="where "+context.whereNode.parse(context);
        var sql=String.format("select %s from %s %s %s",selectstr,fromstr,joinstr,wherestr);
        return sql;
    }
}
