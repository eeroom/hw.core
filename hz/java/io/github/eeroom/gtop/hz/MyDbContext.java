package io.github.eeroom.gtop.hz;

import io.github.eeroom.nalu.DbContext;
import io.github.eeroom.nalu.ParseSqlContext;
import io.github.eeroom.nalu.ParseSqlContextMysql;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Connection;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class MyDbContext extends DbContext {
    AppConfig config;
    public MyDbContext(AppConfig config){
        this.config=config;
    }

    @Override
    protected Connection getConnection() throws Throwable {
        try {
            return (Connection) MyObjectFacotry.getBean(RootConfig.basicDataSourceN1);
        }catch (Throwable ex){
            throw new RuntimeException(String.format("获取数据库连接失败,url:%s,用户名：%s",this.config.dburln1,this.config.dbusernamen1),ex);
        }
    }

    @Override
    protected ParseSqlContext getParseSqlContext() {
        return new ParseSqlContextMysql();
    }

}
