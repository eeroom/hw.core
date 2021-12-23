package io.github.eeroom.sf;

import io.github.eeroom.nalu.DbContext;
import io.github.eeroom.nalu.ParseSqlContext;
import io.github.eeroom.nalu.ParseSqlContextMysql;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SfDbContext extends DbContext implements ApplicationContextAware {
    MapProperties configdata;
    static ApplicationContext rootcontext;
    public SfDbContext(MapProperties configdata){
        this.configdata=configdata;
    }
    public static SfDbContext newInstance(){
        return rootcontext.getBean(SfDbContext.class);
    }
    @Override
    protected Connection getConnection() throws Throwable {

        var cnn= java.sql.DriverManager.getConnection(this.configdata.dbsfurl,this.configdata.dbsfusername,this.configdata.dbsfpwd);
        return cnn;
    }

    @Override
    protected ParseSqlContext getParseSqlContext() {
        return new ParseSqlContextMysql();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        rootcontext=applicationContext;
    }
}
