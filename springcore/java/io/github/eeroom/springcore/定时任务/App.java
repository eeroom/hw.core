package io.github.eeroom.springcore.定时任务;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args){
        var context=new AnnotationConfigApplicationContext();
        context.register(RootConfig.class);
        context.refresh();
    }
}
