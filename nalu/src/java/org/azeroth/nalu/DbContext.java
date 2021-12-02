package org.azeroth.nalu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class DbContext {
    protected String rowCountFiledName = "_rowsCount";

    ArrayList<MyFunction2Throwable<Connection,ParseSqlContext,Integer>> lstexecute=new ArrayList<>();

    public <T> DbSet<T> dbSet(Class<T> meta) throws Throwable {
        return new DbSet<>(this,meta);
    }

    public <T> DbSetAdd<T> add(T entity) throws Throwable {
        var lst=new ArrayList<T>();
        lst.add(entity);
        var add=new DbSetAdd(entity.getClass(),lst);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetAdd<T> add(List<T> lstentity) throws Throwable {
        var add=new DbSetAdd(lstentity.get(0).getClass(),lstentity);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetDel<T> delete(T entity) throws Throwable {
        var lst=new ArrayList<T>();
        lst.add(entity);
        var add=new DbSetDel(entity.getClass(),lst);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetDel<T> delete(List<T> lstentity) throws Throwable {
        var add=new DbSetDel(lstentity.get(0).getClass(),lstentity);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetDelSimple<T> delete(Class<T> meta) throws Throwable {
        var add=new DbSetDelSimple<>(meta);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetEdit<T> edit(T entity) throws Throwable {
        var lst=new ArrayList<T>();
        lst.add(entity);
        var add=new DbSetEdit(entity.getClass(),lst);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetEdit<T> edit(List<T> lstentity) throws Throwable {
        var add=new DbSetEdit(lstentity.get(0).getClass(),lstentity);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetEditSimple<T> edit(Class<T> meta) throws Throwable {
        var add=new DbSetEditSimple<>(meta);
        lstexecute.add(add::execute);
        return add;
    }

    public int saveChange() throws Throwable {
        return this.saveChange(Connection.TRANSACTION_READ_COMMITTED);
    }

    public int saveChange(int transactionLevel) throws Throwable {
        var cnn= this.getConnection();
        cnn.setAutoCommit(false);
        cnn.setTransactionIsolation(transactionLevel);
        int rst=0;
        var lstexe=this.lstexecute;
        this.lstexecute=new ArrayList<>();
        for (var handler:lstexe){
            var context=new ParseSqlContext();
            rst+= handler.apply(cnn,context);
        }
        cnn.commit();
        return rst;
    }

    protected abstract Connection getConnection() throws Throwable;

    protected  <T> PagingList<T> toList(MyFunction<ResultSet,T> map,MyAction2<ParseSqlContext,Boolean> preparecontex) throws Throwable {
        ParseSqlContext context=new ParseSqlContext();
        preparecontex.execute(context,true);
        String cmdstr=this.parseSql(context);
        var cnn= this.getConnection();
        var pst= cnn.prepareStatement(cmdstr);
        for (var i = 0; i<context.lstDbParameter.size(); i++){
            pst.setObject(i+1,context.lstDbParameter.get(i).item2);
        }
        var rt=new PagingList<T>();
        ArrayList<T> lst=new ArrayList<>();
        rt.lst= lst;
        var rs= pst.executeQuery();
        if(context.takerows>0){
            if(!rs.next())
                return rt;
            lst.add(map.apply(rs));
            rt.count=rs.getInt(this.rowCountFiledName);
        }
        while (rs.next()){
            var obj= map.apply(rs);
            lst.add(obj);
        }
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
        String selectstr=String.join(",\r\n",lstselect);
        String fromstr=String.format("%s as %s",context.fromTable.tableName,context.fromTable.tableAlias);
        var joinstr="";
        if(context.lstJoinNode.size()>0)
            joinstr=String.join("\r\n",context.lstJoinNode.stream().map(x->x.parse(context)).collect(java.util.stream.Collectors.toList()));
        String wherestr="";
        if(context.whereNode!=null)
            wherestr="where "+context.whereNode.parse(context);
        String groupbystr="";
        if(context.lstGroupByNode.size()>0)
            groupbystr="group by "+String.join(",",context.lstGroupByNode.stream().map(x->x.parse(context)).collect(java.util.stream.Collectors.toList()));
        String havingstr="";
        if(context.havingNode!=null)
            havingstr="having "+context.havingNode.parse(context);
        String orderbystr="";
        if(context.lstOrderByNode.size()>0)
            orderbystr="order by "+String.join(",",context.lstOrderByNode.stream().map(x->x.parse(context)).collect(java.util.stream.Collectors.toList()));
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
            var cmdstr =String.format("select %s,\r\nROW_NUMBER() OVER(%s) AS %s \r\n from %s\r\n%s\r\n%s\r\n%s\r\n%s",
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
