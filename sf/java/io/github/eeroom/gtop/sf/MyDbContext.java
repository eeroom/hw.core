package io.github.eeroom.gtop.sf;

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
    AppConfig configdata;
    public MyDbContext(AppConfig configdata){
        this.configdata=configdata;
    }

    @Override
    protected Connection getConnection() throws Throwable {

        var cnn= java.sql.DriverManager.getConnection(this.configdata.dburln1,this.configdata.dbusernamen1,this.configdata.dbpwdn1);
        return cnn;
    }

    @Override
    protected ParseSqlContext getParseSqlContext() {
        return new ParseSqlContextMysql();
    }

}
