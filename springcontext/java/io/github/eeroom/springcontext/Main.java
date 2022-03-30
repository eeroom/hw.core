package io.github.eeroom.springcontext;

public class Main {

    public static  void  main(String[] args){
        //使用配置类参数的构造函数，会直接调用refresh方法，不够灵活，不能在refresh之前使用AnnotationConfigApplicationContext的一些方法
        //这里用无参构造函数，后续显式调用refresh
        org.springframework.context.annotation.AnnotationConfigApplicationContext context=
                new org.springframework.context.annotation.AnnotationConfigApplicationContext();
        context.register(RootConfig.class);
        context.addApplicationListener(new MyApplicationListenerOnFinishRefresh());
        context.addApplicationListener(new MyApplicationListenerOnMyEvent());
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
         * spring提供很多基于Aware的回调接口，方便在bean中获取一些想要的容器相关的数据，比如容器本身，
         * bean对应的class实现这些接口，spring在实例化bean的时候会调用这些接口对应的方法，
         */
        var resourceLoaderAware= org.springframework.context.ResourceLoaderAware.class;
        var applicationContextAware= org.springframework.context.ApplicationContextAware.class;
        var applicationEventPublisherAware= org.springframework.context.ApplicationEventPublisherAware.class;
        var environmentAware= org.springframework.context.EnvironmentAware.class;
        var beanFactoryAware= org.springframework.beans.factory.BeanFactoryAware.class;
        /**
         * spring预留了实例化bean操作的前、后的回调接口，让用户可以在实例化之前或之后对bean的实例进行操作
         */
        var beanPostProcessor= org.springframework.beans.factory.config.BeanPostProcessor.class;
        var commonAnnotationBeanPostProcessor= org.springframework.context.annotation.CommonAnnotationBeanPostProcessor.class;

        /**
         * 事件体系，内置事件;org.springframework.context.support.AbstractApplicationContext提供对这些内置事件的支持，分别在对应的方法中触发这些事件
         * 用户通过AbstractApplicationContext提供的addApplicationListener方法向容器注册事件监听器，
         * 监听器需要实现接口ApplicationListener<E extends ApplicationEvent>
         * 用户自定义事件，参看rootconfig2中的示例,原理和内置事件体系的套路一样
         */
        //AbstractApplicationContext的finishRefresh方法中触发
        var contextRefreshedEvent=org.springframework.context.event.ContextRefreshedEvent.class;
        //AbstractApplicationContext的start方法触发
        var contextStartedEvent=org.springframework.context.event.ContextStartedEvent.class;
        var contextStoppedEvent=org.springframework.context.event.ContextStoppedEvent.class;
        var contextClosedEvent=org.springframework.context.event.ContextClosedEvent.class;
        var abstractApplicationContext=org.springframework.context.support.AbstractApplicationContext.class;
        var applicationListener= org.springframework.context.ApplicationListener.class;
        /**
         * aop,bean的类型对此无感知，不会侵入原类型
         * 配置类标注@EnableAspectJAutoProxy
         * 定义切面类标注为@Aspect，同时也必须标注@Component，注册切面类为bean
         * 切面类定义切点方法，标注为@@Around等几个aspectj约定的注解，参照aspectj的切点定义规则
         * spring容器实例化的bean本身就是一个原类型对应的代理类型；后续调用这个bean实例的方法或者字段的时候，代理对象会判断当前调用是否匹配某个或多个切点方法，然后实现出aop的效果
         * 参看本项目的CalFilter切面类
         */
        /**
         * test
         */
        var sayhello= context.getBean("seyhello");
        Calculation cal= context.getBean(Calculation.class);
        cal.Handler();
    }
}
