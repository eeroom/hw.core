package io.github.eeroom.nalu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ParseSqlContext {

    protected String rowCountFiledName = "_rowsCount";
   private int tableIndex=0;
    private int parameterIndex=0;
    private int colindex=0;

    protected int nextTableIndex(){
       return this.tableIndex++;
   }
    protected int nextColIndex() {
        return colindex++;
    }
    protected String nextParameterName(){
        return "?";
    }
   public ArrayList<DbSet> lstTable=new ArrayList<>();
   public WhereNode whereNode;
   public WhereNode havingNode;
   public DbSet fromTable;
   public ArrayList<SelectNode> lstSelectNode=new ArrayList<>();
    public ArrayList<JoinNode> lstJoinNode =new ArrayList<>();


    public ArrayList<Tuple.Tuple2<String,Object>> lstDbParameter =new ArrayList<>();
    public ArrayList<OrderbyNode> lstOrderByNode=new ArrayList<>();
    public ArrayList<Column> lstGroupByNode=new ArrayList<>();
    public  int takerows;
    public  int skiprows;

    protected String GetTableName(DbSet table){
        return table.tableName;
    }

    protected String GetTableNameAlias(DbSet table){
        return table.tableAlias;
    }
    /**
     * 适用于mssqlserver，其他数据库请重新实现该方法
     * 这里的分页采用rownumber方式
     */
    protected  String parseSql() {
        if(this.lstSelectNode.size()<1)
            throw new RuntimeException("必须指定要查询的列，请使用select相关的方法");
        var lstselect=this.lstSelectNode.stream().map(x->x.parse(this)).collect(java.util.stream.Collectors.toList());
        String selectstr=String.join(",\r\n",lstselect);
        String fromstr=String.format("%s as %s",this.GetTableName(this.fromTable),this.GetTableNameAlias(this.fromTable));
        var joinstr="";
        if(this.lstJoinNode.size()>0)
            joinstr=String.join("\r\n",this.lstJoinNode.stream().map(x->x.parse(this)).collect(java.util.stream.Collectors.toList()));
        String wherestr="";
        if(this.whereNode!=null)
            wherestr="where "+this.whereNode.parse(this);
        String groupbystr="";
        if(this.lstGroupByNode.size()>0)
            groupbystr="group by "+String.join(",",this.lstGroupByNode.stream().map(x->x.parse(this)).collect(java.util.stream.Collectors.toList()));
        String havingstr="";
        if(this.havingNode!=null)
            havingstr="having "+this.havingNode.parse(this);
        String orderbystr="";
        if(this.lstOrderByNode.size()>0)
            orderbystr="order by "+String.join(",",this.lstOrderByNode.stream().map(x->x.parse(this)).collect(java.util.stream.Collectors.toList()));
        if(this.takerows<1){//不分页
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
            if("".equals(orderbystr))
                throw new RuntimeException("必须指定排序的列");
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
                    String.valueOf(this.skiprows),
                    String.valueOf(this.skiprows+this.takerows-1));
            return sql;
        }
    }

    protected ArrayList<String> lstsql;
    protected  <T> PagingList<T> toList(Connection cnn, MyFunction<ResultSet,T> map) throws Throwable {
        String cmdstr=this.parseSql();
        this.lstsql=new ArrayList<>();
        this.lstsql.add(cmdstr);
        try(var pst= cnn.prepareStatement(cmdstr)) {
            for (var i = 0; i<this.lstDbParameter.size(); i++){
                pst.setObject(i+1,this.lstDbParameter.get(i).item2);
            }
            var rt=new PagingList<T>();
            ArrayList<T> lst=new ArrayList<>();
            rt.lst= lst;
            try(var rs= pst.executeQuery()) {
                if(this.takerows>0){
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
        }
    }
}
