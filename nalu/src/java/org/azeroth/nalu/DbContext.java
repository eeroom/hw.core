package org.azeroth.nalu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class DbContext {


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
        }
    }

    protected abstract Connection getConnection() throws Throwable;

    protected abstract ParseSqlContext getParseSqlContext();

    protected  <T> PagingList<T> toList(MyFunction<ResultSet,T> map,MyAction2<ParseSqlContext,Boolean> preparecontex) throws Throwable {
        var context=this.getParseSqlContext();
        preparecontex.execute(context,true);
        try(var cnn= this.getConnection()) {
           return context.toList(cnn,map);
        }
    }
}
