package io.github.eeroom.hzkd;

import io.github.eeroom.nalu.DbContext;
import io.github.eeroom.nalu.ParseSqlContext;
import io.github.eeroom.nalu.ParseSqlContextMysql;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Connection;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class MyDbContext extends DbContext {
    AppConfig configdata;
    public MyDbContext(AppConfig configdata){
        this.configdata=configdata;
    }

    @Override
    protected Connection getConnection() throws Throwable {
        try {
            var cnn= java.sql.DriverManager.getConnection(this.configdata.dburln1,this.configdata.dbusernamen1,this.configdata.dbpwdn1);
            return cnn;
        }catch (Throwable ex){
            throw new RuntimeException(String.format("连接数据库失败,地址:%s,用户名：%s",this.configdata.dburln1,this.configdata.dbusernamen1));
        }


    }

    @Override
    protected ParseSqlContext getParseSqlContext() {
        return new ParseSqlContextMysql();
    }

}
