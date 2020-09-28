package org.azeroth.springcontext;

public class Main {

    public static  void  main(String[] args){

        //引入spring的ioc容器
        org.springframework.context.annotation.AnnotationConfigApplicationContext context=
                new org.springframework.context.annotation.AnnotationConfigApplicationContext(RootConfig.class);
        Calculation cal= context.getBean(Calculation.class);
        cal.Handler();
    }
}
