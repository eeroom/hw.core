package io.github.eeroom.nalu;

import java.util.ArrayList;
import java.util.List;

public class ExecuteSqlException extends RuntimeException {

    public ExecuteSqlException(List<String> sql,Object parameter,Object entity,Throwable throwable){
        super("nalu执行sql语句发生异常，sql语句和相关参数值请参看sql,parameter和entity",throwable);
        this.sql=sql;
        this.parameter=parameter;
        this.entity=entity;
    }

    public ExecuteSqlException(String sql,Object parameter,Object entity,Throwable throwable){
        super("nalu执行sql语句发生异常，sql语句和相关参数值请参看sql,parameter和entity",throwable);
        this.sql=new ArrayList<>();
        this.sql.add(sql);
        this.parameter=parameter;
        this.entity=entity;
    }

    List<String> sql;
    Object parameter;

    public List<String> getSql() {
        return sql;
    }

    public void setSql(List<String> sql) {
        this.sql = sql;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    Object entity;

}
