package io.github.eeroom.springcore.mybatis整合springcore;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.MessageFormat;

public class App {

    public static void main(String[] args){
        var context= new AnnotationConfigApplicationContext();
        context.register(RootConfig.class);
        context.refresh();
        var iDaoStudent= context.getBean(IDaoStudent.class);
        var lstStudent= iDaoStudent.getAll();
        lstStudent.forEach(x->System.out.println(MessageFormat.format("lstStudent，name:{0}",x.getName())));

        var iDaoTeacher=context.getBean(IDaoTeacher.class);
        var lstTeacher= iDaoTeacher.getAll();
        lstTeacher.forEach(x->System.out.println(MessageFormat.format("lstTeacher，name:{0}",x.getName())));
    }
}
