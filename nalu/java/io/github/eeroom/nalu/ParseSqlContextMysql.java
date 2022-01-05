package io.github.eeroom.nalu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ParseSqlContextMysql extends  ParseSqlContext {

    protected ArrayList<String> parseSqls() {
        ArrayList<String> lstsql=new ArrayList<>();
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

        if(this.takerows<1) {//不分页
            var sql=String.format("select %s \r\n from %s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s",
                    selectstr,
                    fromstr,
                    joinstr,
                    wherestr,
                    groupbystr,
                    havingstr,
                    orderbystr);
            lstsql.add(sql);
            return lstsql;
        }
        if("".equals(orderbystr))
            throw new RuntimeException("必须指定排序的列");
        var sql=String.format("select %s \r\n from %s\r\n%s\r\n%s\r\n%s\r\n%s\r\n%s \r\nlimit %s,%s ",
                selectstr,
                fromstr,
                joinstr,
                wherestr,
                groupbystr,
                havingstr,
                orderbystr,
                String.valueOf(this.skiprows),
                String.valueOf(this.takerows));
        var sqlcount = String.format("select count(0) as _theRowIndex \r\n from %s\r\n%s\r\n%s\r\n%s\r\n%s",
                    fromstr,
                    joinstr,
                    wherestr,
                    groupbystr,
                    havingstr);
        lstsql.add(sql);
        lstsql.add(sqlcount);
        return  lstsql;
    }

    @Override
    protected <T> PagingList<T> toList(Connection cnn, MyFunction<ResultSet, T> map) {
        var lstcmdstr =this.parseSqls();
        this.lstsql=new ArrayList<>();
        this.lstsql.addAll(lstcmdstr);
        if(lstcmdstr.size()==1){
            try(var pst= cnn.prepareStatement(lstcmdstr.get(0))) {
                for (var i = 0; i<this.lstDbParameter.size(); i++){
                    pst.setObject(i+1,this.lstDbParameter.get(i).item2);
                }
                var rt=new PagingList<T>();
                ArrayList<T> lst=new ArrayList<>();
                rt.lst= lst;
                try(var rs= pst.executeQuery()) {
                    while (rs.next()){
                        var obj= map.apply(rs);
                        lst.add(obj);
                    }
                    return rt;
                }
            }catch (Throwable throwable){
                throw new ExecuteSqlException(lstcmdstr,this.lstDbParameter.stream().map(x->x.item2).collect(Collectors.toList()),null,throwable);
            }
        }else {
            var rt=new PagingList<T>();
            try(var pst= cnn.prepareStatement(lstcmdstr.get(0))) {
                for (var i = 0; i<this.lstDbParameter.size(); i++){
                    pst.setObject(i+1,this.lstDbParameter.get(i).item2);
                }
                ArrayList<T> lst=new ArrayList<>();
                rt.lst= lst;
                try(var rs= pst.executeQuery()) {
                    while (rs.next()){
                        var obj= map.apply(rs);
                        lst.add(obj);
                    }
                }
            }catch (Throwable throwable){
                throw new ExecuteSqlException(lstcmdstr,this.lstDbParameter.stream().map(x->x.item2).collect(Collectors.toList()),null,throwable);
            }
            try(var pst= cnn.prepareStatement(lstcmdstr.get(1))) {
                for (var i = 0; i<this.lstDbParameter.size(); i++){
                    pst.setObject(i+1,this.lstDbParameter.get(i).item2);
                }
                try(var rs= pst.executeQuery()) {
                    rs.next();
                    rt.count= rs.getInt("_theRowIndex");
                }
            }catch (Throwable throwable){
                throw new ExecuteSqlException(lstcmdstr,this.lstDbParameter.stream().map(x->x.item2).collect(Collectors.toList()),null,throwable);
            }
            return rt;
        }
    }
}
