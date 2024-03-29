package io.github.eeroom.nalu;

import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DbSetAdd<T> extends TableSet<T> {
    List<T> lstEntity;
    ArrayList<Column> lstcol=new ArrayList<>();
    DbSetAdd(Class<T> meta,List<T> lst) {
        super(meta);
        this.lstEntity= lst;
    }

    public DbSetAdd<T> setInsertCol(Function<T,Columns> exp){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        exp.apply(this.entityProxy);
        for (var item : this.lstTarget){
            this.lstcol.add(new Column<>(item.item1,item.item2));
        }
        return this;
    }

    public DbSetAdd<T> setInsertCol(){
        var lst= dictGetMethod.get(this.meta.getName()).keySet().stream()
                .map(colName->new Column(this,colName))
                .collect(Collectors.toList());
        this.lstcol.addAll(lst);
        return this;
    }

    int execute(Connection cnn,ParseSqlContext context) {
        int rst=0;
        if(this.lstcol.size()<1)
            throw  new RuntimeException("必须指定要新增赋值的列");
        var lstName= this.lstcol.stream().map(x->x.colName).collect(Collectors.toList());
        var strCol=String.join(",",lstName);
        var lstPName= lstName.stream().map(x->"?").collect(Collectors.toList());
        var strP=String.join(",",lstPName);
        var sql=String.format("insert into %s (%s) values (%s)",this.tableName,strCol,strP);
        Object targetEntity=null;
        try(var pst= cnn.prepareStatement(sql)){
            var dict=dictGetMethod.get(this.meta.getName());
            for (var obj:this.lstEntity){
                targetEntity=obj;
                pst.clearParameters();
                int index=1;
                for (var pname:lstName){
                    var pValue= dict.get(pname).apply(obj);
                    pst.setObject(index++,pValue);
                }
                rst+= pst.executeUpdate();
            }
            return rst;
        }catch (Throwable throwable){
            throw  new ExecuteSqlException(sql,null,targetEntity,throwable);
        }
    }
}
