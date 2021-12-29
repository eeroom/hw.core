package io.github.eeroom.gtop.hz;

import io.github.eeroom.nalu.DbContext;
import io.github.eeroom.nalu.ParseSqlContext;
import io.github.eeroom.nalu.ParseSqlContextMysql;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MyDbContext extends DbContext {
    AppConfig config;
    public MyDbContext(AppConfig config){
        this.config=config;
    }

    @Override
    protected Connection getConnection() throws Throwable {
        try {
            var cnn= java.sql.DriverManager.getConnection(this.config.dburln1,this.config.dbusernamen1,this.config.dbpwdn1);
            return cnn;
        }catch (Throwable ex){
            throw new RuntimeException(String.format("连接数据库失败,url:%s,用户名：%s",this.config.dburln1,this.config.dbusernamen1));
        }
    }

    @Override
    protected ParseSqlContext getParseSqlContext() {
        return new ParseSqlContextMysql();
    }

}
