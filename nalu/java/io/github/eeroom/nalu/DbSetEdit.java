package io.github.eeroom.nalu;

import java.sql.Connection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DbSetEdit<T> extends TableSet<T> {
    List<T> lstEntity;
    List<Column> lstcol;
    MyFunction2<DbSetEdit<T>,T,WhereNode> whCreator;

    DbSetEdit(Class<T> meta,List<T> lstEntity) {
        super(meta);
        this.lstEntity=lstEntity;
    }

    public DbSetEdit<T> setUpdateCol(Function<T,Columns> exp){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        exp.apply(this.entityProxy);
        for (var item : this.lstTarget){
            this.lstcol.add(new Column<>(item.item1,item.item2));
        }
        return this;
    }

    public DbSetEdit<T> setUpdateCol(){
        var lst= dictGetMethod.get(this.meta.getName()).keySet().stream()
                .map(colName->new Column(this,colName))
                .collect(Collectors.toList());
        this.lstcol.addAll(lst);
        return this;
    }

    public DbSetEdit<T> where(MyFunction2<DbSetEdit<T>,T,WhereNode> wh){
        this.whCreator=wh;
        return this;
    }

    public <R> Column<R> col(Function<T,R> exp){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        exp.apply(this.entityProxy);
        Column<R> column=new Column(this.lstTarget.get(0).item1,this.lstTarget.get(0).item2);
        return column;
    }

    int execute(Connection cnn, ParseSqlContext context){
        if(this.lstcol.size()<1)
            throw  new RuntimeException("必须指定update的目标列");
        if(this.whCreator==null)
            throw  new RuntimeException("必须指定where条件");
        var lstName= this.lstcol.stream().map(x->x.colName).collect(Collectors.toList());
        var strCol=String.join(",",lstName);
        var lstPName= lstName.stream().map(x->x+"=?").collect(Collectors.toList());
        var strSet=String.join(",",lstPName);
        int rst=0;
        var dict=dictGetMethod.get(this.meta.getName());
        for (var obj:this.lstEntity){
            context.lstDbParameter.clear();
            var strWhere=this.whCreator.apply(this,obj).parse(context);
            var sql=String.format("update %s set %s where %s",this.tableName,strSet,strWhere);
            try (var pst=cnn.prepareStatement(sql)){
                int pindex=1;
                for (var name:lstName){
                    pst.setObject(pindex++,dict.get(name).apply(obj));
                }
                for (var pp:context.lstDbParameter){
                    pst.setObject(pindex++,pp.item2);
                }
                rst+=pst.executeUpdate();
            }catch (Throwable throwable){
                throw  new ExecuteSqlException(sql,context.lstDbParameter.stream().map(x->x.item2).collect(Collectors.toList()),obj,throwable);
            }
        }
        return rst;
    }
}
