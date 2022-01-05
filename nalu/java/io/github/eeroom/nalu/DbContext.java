package io.github.eeroom.nalu;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class DbContext {

    protected static Function<String,String> ColNameGetter;
    ArrayList<MyFunction2<Connection,ParseSqlContext,Integer>> lstexecute=new ArrayList<>();

    public <T> DbSet<T> dbSet(Class<T> meta) {
        return new DbSet<>(this,meta);
    }

    public <T> DbSetAdd<T> add(T entity) {
        var lst=new ArrayList<T>();
        lst.add(entity);
        var add=new DbSetAdd(entity.getClass(),lst);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetAdd<T> add(List<T> lstentity) {
        var add=new DbSetAdd(lstentity.get(0).getClass(),lstentity);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetDel<T> delete(T entity) {
        var lst=new ArrayList<T>();
        lst.add(entity);
        var add=new DbSetDel(entity.getClass(),lst);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetDel<T> delete(List<T> lstentity) {
        var add=new DbSetDel(lstentity.get(0).getClass(),lstentity);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetDelSimple<T> delete(Class<T> meta) {
        var add=new DbSetDelSimple<>(meta);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetEdit<T> edit(T entity) {
        var lst=new ArrayList<T>();
        lst.add(entity);
        var add=new DbSetEdit(entity.getClass(),lst);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetEdit<T> edit(List<T> lstentity) {
        var add=new DbSetEdit(lstentity.get(0).getClass(),lstentity);
        lstexecute.add(add::execute);
        return add;
    }

    public <T> DbSetEditSimple<T> edit(Class<T> meta) {
        var add=new DbSetEditSimple<>(meta);
        lstexecute.add(add::execute);
        return add;
    }

    public int saveChange()  {
        return this.saveChange(Connection.TRANSACTION_READ_COMMITTED);
    }

    public int saveChange(int transactionLevel) {
        try(var cnn= this.getConnection()){
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
        }catch (Throwable throwable){
            throw  new RuntimeException(throwable);
        }
    }

    protected abstract Connection getConnection() throws Throwable;

    protected abstract ParseSqlContext getParseSqlContext();

    ParseSqlContext context;
    protected  <T> PagingList<T> toList(MyFunction<ResultSet,T> map,MyAction2<ParseSqlContext,Boolean> preparecontex) {
        this.context=this.getParseSqlContext();
        preparecontex.execute(this.context,true);
        try(var cnn= this.getConnection()) {
           return this.context.toList(cnn,map);
        }catch (Throwable throwable){
            throw new RuntimeException(throwable);
        }
    }
}
