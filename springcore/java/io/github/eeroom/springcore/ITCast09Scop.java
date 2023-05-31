package io.github.eeroom.springcore;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

public class ITCast09Scop {

    /**
     * Scop失效解决办法
     * @param args
     */
    public static void main(String[] args){
        var context=new AnnotationConfigApplicationContext();
        context.register(Bean1.class);
        context.register(Bean2.class);
        context.refresh();
        var bean2= context.getBean(Bean2.class);
        /**
         * 对于单例对象，依赖注入仅执行一次，所以注入的多例对象就成为一个固定值
         * 如果希望注入的多例对象 不是固定值，而是每次获取都是 不同的对象，解决办法：
         * 1、被注入的字段或方法添加 @Lazy  ，配合 @Autowire 这样注入的是一个原多例类的代理对象
         * 2、在多例bean定义的scop注解设置 proxyMode =  ScopedProxyMode.TARGET_CLASS，这样获取的多例bean都是代理对象
         * 3、通过ObjectFactory<T>获取需要的bean
         * 4、通过appicationcontext或者beanfactory
         * 四个解决办法的核心理念都是：推迟bean的获取时间，
         */
        System.out.println( "---单例注入多例失效--解决办法：1、@Lazy--2、proxyMode =  ScopedProxyMode.TARGET_CLASS-----------");
        System.out.println(bean2.bean1);
        System.out.println(bean2.bean1);
        System.out.println( "---单例注入多例失效--解决办法：3、ObjectFactory<Bean1>-----------");
        System.out.println(bean2.bean1ObjectFactory.getObject());
        System.out.println(bean2.bean1ObjectFactory.getObject());
        System.out.println( "---单例注入多例失效--解决办法：4、通过appicationcontext或者beanfactory获取需要的bean-----------");
        System.out.println(bean2.getBean1());
        System.out.println(bean2.getBean1());
        System.out.println( "---logo(配置类中多例注入单例失效,解决办法：所在配置类添加 @Configuration 成为full模式（这个bean成为代理对象） 否则为lite模式)-------------------------------------------");
        System.out.println( context.getBean("logo"));
        System.out.println( context.getBean("logo"));
        System.out.println( "---guid-------------------------------------------");
        System.out.println( context.getBean("guid"));
        System.out.println( context.getBean("guid"));

    }

    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    static class Bean1{
        UUID uuid;
        public Bean1(){
            this.uuid=UUID.randomUUID();
        }

        public UUID getUuid(){
            return this.uuid;
        }

        @Bean
        @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
        public UUID logo(){
            return this.guid();
        }

        @Bean
        @Scope(value = BeanDefinition.SCOPE_SINGLETON)
        public UUID guid(){
            return UUID.randomUUID();
        }
    }

    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    static class Bean2{

        @Autowired
        Bean1 bean1;

        @Autowired
        ApplicationContext applicationContext;

        @Autowired
        ObjectFactory<Bean1> bean1ObjectFactory;

        public Bean1 getBean1(){
            return this.applicationContext.getBean(Bean1.class);
        }

    }
}
