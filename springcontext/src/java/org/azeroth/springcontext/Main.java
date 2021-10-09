package org.azeroth.springcontext;

public class Main {

    public static  void  main(String[] args){

        //引入spring的ioc容器
        //aop
        //依赖aspectjweaver，ioc的容器配置添加EnableAspectJAutoProxy
        //建立切面，一个实现类
        //建立切点，参照aspectj的规则
        //使用配置类参数的构造函数，会直接调用refresh方法，不够灵活，不能在refresh之前使用AnnotationConfigApplicationContext的一些方法
        //这里用无参构造函数，后续显式调用refresh
        org.springframework.context.annotation.AnnotationConfigApplicationContext context=
                new org.springframework.context.annotation.AnnotationConfigApplicationContext();
        context.register(RootConfig.class);
        context.refresh();
        /**
         * 向容易注册bean的常见方式
         * 1、在配置类中定义标注@bean的方法，方法名称就是bean的注册名称，方法返回类型就是bean的注册类型
         * 2、在配置类上标注@ComponentScan（自动扫包注册bean）,定义标注@Component的类型，类型名称（首字母转小写）就是bean的注册名称，类型就是bean的注册类型
         * 3、在配置类中标注@Import，import的参数为标注了@Configuration的另一个配置类型，后续和2一样
         * 4、在配置类中导入xml定义的bean,xml中导入javaconfig定义的bean，和3类似
         * 5、在配置类中标注@Import，import的参数为实现ImportBeanDefinitionRegistrar的类型，在实现中向容器注册bean,参看本项目的MyBeanPostProcessorRegistrar
         * 6、在配置类中标注@Import，import的参数为实现ImportSelector的类型，在实现中返回另一个配置类全名称，且该配置类不需要标注@Configuration，参看本项目@MyImportSelector
         * 7、利用AnnotationConfigApplicationContext类的add系列方法，添加一些processer等，本质也是注册bean
         */

        /**
         * spring提供很多Aware在bean中需要获取一些容器相关的数据，比如容器本身，
         */

        /**
         * spring预留的让用户处理bean实例化的一些约定，processer
         */
        var sayhello= context.getBean("seyhello");
        Calculation cal= context.getBean(Calculation.class);
        cal.Handler();
    }
}
