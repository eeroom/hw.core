package io.github.eeroom.springcore.mybatis整合springcore;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RootConfig {

    @Bean("sqlSessionFactory-kd")
    public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws IOException {
        var cnnstr="jdbc:sqlite:D:/Code/db/springcore-hzkd.db";
        var dataSource=new PooledDataSource(org.sqlite.JDBC.class.getName(),cnnstr,"","");
        var transactionFactory=new org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory();
        var env=new Environment.Builder("hz-kd")
                .dataSource(dataSource)
                .transactionFactory(transactionFactory)
                .build();
        var configuration=new org.apache.ibatis.session.Configuration(env);

        var lstmapperxml= applicationContext.getResources("mybatis整合springcore/mapper/hzkd/*.xml");
        for (var mapperxml:lstmapperxml){
            var mapperstream=mapperxml.getInputStream();
            var xmlMapperBuilder= new XMLMapperBuilder(mapperstream,configuration,mapperxml.toString(),configuration.getSqlFragments());
            xmlMapperBuilder.parse();
        }

        var sessionFactoryBuilder=new SqlSessionFactoryBuilder();
        var sessionFactory= sessionFactoryBuilder.build(configuration);
        return sessionFactory;
    }

    @Bean("sqlSessionFactory-oa")
    public SqlSessionFactory sqlSessionFactoryOA(ApplicationContext applicationContext) throws IOException {
        var cnnstr="jdbc:sqlite:D:/Code/db/springcore-hzoa.db";
        var dataSource=new PooledDataSource(org.sqlite.JDBC.class.getName(),cnnstr,"","");
        var transactionFactory=new org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory();
        var env=new Environment.Builder("hz-oa")
                .dataSource(dataSource)
                .transactionFactory(transactionFactory)
                .build();
        var configuration=new org.apache.ibatis.session.Configuration(env);

        var lstmapperxml= applicationContext.getResources("mybatis整合springcore/mapper/hzoa/*.xml");
        for (var mapperxml:lstmapperxml){
            var mapperstream=mapperxml.getInputStream();
            var xmlMapperBuilder= new XMLMapperBuilder(mapperstream,configuration,mapperxml.toString(),configuration.getSqlFragments());
            xmlMapperBuilder.parse();
        }

        var sessionFactoryBuilder=new SqlSessionFactoryBuilder();
        var sessionFactory= sessionFactoryBuilder.build(configuration);
        return sessionFactory;
    }

    @Bean
    public IDaoStudent iDaoStudent(@Qualifier("sqlSessionFactory-kd")SqlSessionFactory sessionFactory){
        return sessionFactory.openSession().getMapper(IDaoStudent.class);
    }

    @Bean
    public IDaoTeacher iDaoTeacher(@Qualifier("sqlSessionFactory-oa")SqlSessionFactory sessionFactory){
        return sessionFactory.openSession().getMapper(IDaoTeacher.class);
    }
}
