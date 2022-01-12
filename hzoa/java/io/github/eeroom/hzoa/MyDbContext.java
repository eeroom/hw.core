package io.github.eeroom.hzoa;

import io.github.eeroom.nalu.DbContext;
import io.github.eeroom.nalu.ParseSqlContext;
import io.github.eeroom.nalu.ParseSqlContextMysql;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Connection;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class MyDbContext extends DbContext implements ApplicationContextAware {
    AppConfig config;
    ApplicationContext applicationContext;
    public MyDbContext(AppConfig config){
        this.config=config;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
    @Override
    protected Connection getConnection() throws Throwable {
        try {
            var ds= (BasicDataSource)this.applicationContext.getBean(RootConfig.basicDataSourceN1);
            return  ds.getConnection();
        }catch (Throwable ex){
            throw new RuntimeException(String.format("获取数据库连接失败,url:%s,用户名：%s",this.config.dburln1,this.config.dbusernamen1),ex);
        }
    }

    @Override
    protected ParseSqlContext getParseSqlContext() {
        return new ParseSqlContextMysql();
    }


}
