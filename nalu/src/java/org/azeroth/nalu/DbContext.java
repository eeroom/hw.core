package org.azeroth.nalu;

import org.azeroth.nalu.node.WhereNode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DbContext {

    public <T> DbSet<T> DbSet(Class<T> meta) throws Throwable {
        return new DbSet<>(meta,this);
    }

    public <T> List<T> toList(MyFunction<ResultSet,T> map,MyAction2<ParseSqlContext,Boolean> preparecontex) throws Throwable {

        ParseSqlContext context=new ParseSqlContext();
        preparecontex.execute(context,true);
        String cmdstr=this.parse(context);
        String cnnstr="jdbc:sqlserver://127.0.0.1\\sqlexpress:1433;DatabaseName=hw";
        var cnn= java.sql.DriverManager.getConnection(cnnstr,"sa","123456");


        var st= cnn.createStatement();
        var pst= cnn.prepareStatement("");

        cmdstr="select * from Log";
        var rs= st.executeQuery(cmdstr);
        while (rs.next()){
            System.out.println("Id="+rs.getLong("Id"));
            System.out.println("Name="+rs.getString("Name"));
        }


        return null;
    }

    private String parse(ParseSqlContext context) {
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
