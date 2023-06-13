package io.github.eeroom.springcore.mybatis;

import io.github.eeroom.springcore.mybatis.dao.hzoa.ITeacher;
import io.github.eeroom.springcore.mybatis.dao.hzkd.IStudent;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

public class App {

    public static void main(String[] args){
        var context=new AnnotationConfigApplicationContext();
        context.register(RootConfig.class);
        context.refresh();
        var lstStudent= context.getBean(IStudent.class).getAll();
        var lstTeacher=context.getBean(ITeacher.class).getAll();
        int a=3;

    }

    @Configuration
    static class RootConfig{
        @Bean("mapperScannerConfigurer-hzkd")
        public MapperScannerConfigurer mapperScannerConfigurer() {
            MapperScannerConfigurer  mapperScannerConfigurer=new MapperScannerConfigurer();
            mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean-hzkd");
            mapperScannerConfigurer.setBasePackage("io.github.eeroom.springcore.mybatis.dao.hzkd");
            return mapperScannerConfigurer;
        }

        @Bean("mapperScannerConfigurer-hzoa")
        public MapperScannerConfigurer mapperScannerConfigurer2() {
            MapperScannerConfigurer  mapperScannerConfigurer=new MapperScannerConfigurer();
            mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean-hzoa");
            mapperScannerConfigurer.setBasePackage("io.github.eeroom.springcore.mybatis.dao.hzoa");
            return mapperScannerConfigurer;
        }

        @Bean("sqlSessionFactoryBean-hzkd")
        public SqlSessionFactoryBean sqlSessionFactoryBean(ApplicationContext context) throws Throwable {
            var sqlSessionFactoryBean=new SqlSessionFactoryBean();
            var datasource=new PooledDataSource(org.sqlite.JDBC.class.getName(),"jdbc:sqlite:D:/Code/db/springcore-hzkd.db","","");
            sqlSessionFactoryBean.setDataSource(datasource);
            var pathMatchingResourcePatternResolver= new PathMatchingResourcePatternResolver();
            var lstResource= pathMatchingResourcePatternResolver.getResources("classpath:mybatis-mapper/hzkd/**.xml");
            sqlSessionFactoryBean.setMapperLocations(lstResource);
            return sqlSessionFactoryBean;
        }

        @Bean("sqlSessionFactoryBean-hzoa")
        public SqlSessionFactoryBean sqlSessionFactoryBean2(ApplicationContext context) throws Throwable {
            var sqlSessionFactoryBean=new SqlSessionFactoryBean();
            var datasource=new PooledDataSource(org.sqlite.JDBC.class.getName(),"jdbc:sqlite:D:/Code/db/springcore-hzoa.db","","");
            sqlSessionFactoryBean.setDataSource(datasource);
            var pathMatchingResourcePatternResolver= new PathMatchingResourcePatternResolver();
            var lstResource= pathMatchingResourcePatternResolver.getResources("classpath:mybatis-mapper/hzoa/**.xml");
            sqlSessionFactoryBean.setMapperLocations(lstResource);
            return sqlSessionFactoryBean;
        }
    }
}
