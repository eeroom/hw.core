package io.github.eeroom.nalu;

import java.sql.Connection;
import java.util.List;
import java.util.function.Function;

public class DbSetDel<T> extends TableSet<T> {
    List<T> lstEntity;
    MyFunction2<DbSetDel<T>,T,WhereNode> whCreator;
    DbSetDel(Class<T> meta,List<T> lstEntity){
        super(meta);
        this.lstEntity=lstEntity;
    }

    public <R> Column<R> col(Function<T,R> exp){
        this.setProxyHookHandler(this::onProxyHandlerInvokedHandler);
        exp.apply(this.entityProxy);
        Column<R> column=new Column(this.lstTarget.get(0).item1,this.lstTarget.get(0).item2);
        return column;
    }

    public DbSetDel<T> where(MyFunction2<DbSetDel<T>,T,WhereNode> wh){
        this.whCreator=wh;
        return this;
    }

    int execute(Connection cnn, ParseSqlContext context) throws Throwable {
        if(this.whCreator==null)
            throw  new RuntimeException("必须指定where条件");
        int rst=0;
        for (var obj:this.lstEntity){
            context.lstDbParameter.clear();
            var strwhere=this.whCreator.apply(this,obj).parse(context);
            var sql=String.format("delete from %s where %s",this.tableName,strwhere);
            var pts= cnn.prepareStatement(sql);
            int pindex=1;
            for (var pp:context.lstDbParameter){
                pts.setObject(pindex++,pp.item2);
            }
            rst+=pts.executeUpdate();
        }
        return rst;
    }
}
