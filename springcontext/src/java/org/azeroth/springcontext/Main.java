package org.azeroth.springcontext;

public class Main {

    public static  void  main(String[] args){

        //引入spring的ioc容器
        //aop
        //依赖aspectjweaver，ioc的容器配置添加EnableAspectJAutoProxy
        //建立切面，一个实现类
        //建立切点，参照aspectj的规则
        org.springframework.context.annotation.AnnotationConfigApplicationContext context=
                new org.springframework.context.annotation.AnnotationConfigApplicationContext(RootConfig.class);
        Calculation cal= context.getBean(Calculation.class);
        cal.Handler();
    }
}
