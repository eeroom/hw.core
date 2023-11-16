package io.github.eeroom.springcore.mybatis整合springmybatis;

import io.github.eeroom.springcore.mybatis整合springmybatis.dao.hzoa.ITeacher;
import io.github.eeroom.springcore.mybatis整合springmybatis.dao.hzkd.IStudent;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.text.MessageFormat;

public class App {

    public static void main(String[] args){
        var context=new AnnotationConfigApplicationContext();
        context.register(RootConfig.class);
        context.refresh();
        var lstStudent= context.getBean(IStudent.class).getAll();
        lstStudent.forEach(x->System.out.println(MessageFormat.format("lstStudent,name:{0}",x.getName())));
        var lstTeacher=context.getBean(ITeacher.class).getAll();
        lstTeacher.forEach(x->System.out.println(MessageFormat.format("lstTeacher,name:{0}",x.getName())));
    }

}
