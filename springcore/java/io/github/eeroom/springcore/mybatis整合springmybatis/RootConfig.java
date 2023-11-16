package io.github.eeroom.springcore.mybatis整合springmybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class RootConfig{

    @Bean("sqlSessionFactoryBean-hzkd")
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws Throwable {
        var sqlSessionFactoryBean=new SqlSessionFactoryBean();
        var datasource=new PooledDataSource(org.sqlite.JDBC.class.getName(),"jdbc:sqlite:D:/Code/db/springcore-hzkd.db","","");
        sqlSessionFactoryBean.setDataSource(datasource);
        var pathMatchingResourcePatternResolver= new PathMatchingResourcePatternResolver();
        var lstResource= pathMatchingResourcePatternResolver.getResources("classpath:mybatis整合springmybatis/hzkd/**.xml");
        sqlSessionFactoryBean.setMapperLocations(lstResource);
        return sqlSessionFactoryBean;
    }

    @Bean("sqlSessionFactoryBean-hzoa")
    public SqlSessionFactoryBean sqlSessionFactoryBean2() throws Throwable {
        var sqlSessionFactoryBean=new SqlSessionFactoryBean();
        var datasource=new PooledDataSource(org.sqlite.JDBC.class.getName(),"jdbc:sqlite:D:/Code/db/springcore-hzoa.db","","");
        sqlSessionFactoryBean.setDataSource(datasource);
        var pathMatchingResourcePatternResolver= new PathMatchingResourcePatternResolver();
        var lstResource= pathMatchingResourcePatternResolver.getResources("classpath:mybatis整合springmybatis/hzoa/**.xml");
        sqlSessionFactoryBean.setMapperLocations(lstResource);
        return sqlSessionFactoryBean;
    }

    @Bean("mapperScannerConfigurer-hzkd")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer  mapperScannerConfigurer=new MapperScannerConfigurer();
        //如果不指定，则@component的bean都会被扫描会注册成MapperProxy
        mapperScannerConfigurer.setAnnotationClass(Mapper.class);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean-hzkd");
        mapperScannerConfigurer.setBasePackage("io.github.eeroom.springcore.mybatis整合springmybatis.dao.hzkd");
        return mapperScannerConfigurer;
    }

    @Bean("mapperScannerConfigurer-hzoa")
    public MapperScannerConfigurer mapperScannerConfigurer2() {
        MapperScannerConfigurer  mapperScannerConfigurer=new MapperScannerConfigurer();
        //如果不指定，则@component的bean都会被扫描会注册成MapperProxy
        mapperScannerConfigurer.setAnnotationClass(Mapper.class);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean-hzoa");
        mapperScannerConfigurer.setBasePackage("io.github.eeroom.springcore.mybatis整合springmybatis.dao.hzoa");
        return mapperScannerConfigurer;
    }

}
