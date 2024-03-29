package io.github.eeroom.springcore.H01ApplicationContext和BeanFactory的关系;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class ITCast01ApplicationContext {
    public static void main(String[] args) throws IOException {
        /**
         * BeanFactory是ApplicationContext的父接口
         * BeanFactory是spring的核心容器，主要的ApplicationContext实现都【组合】了它的功能
         * BeanFactory表面上是有getBean，实际上控制反转、基本的依赖注入、Bean的生命周期等各种功能，都有他的实现类提供
         * 官方的实现：org.springframework.beans.factory.support.DefaultListableBeanFactory
         */
        var BeanFactory=org.springframework.beans.factory.BeanFactory.class;
        var DefaultListableBeanFactory= org.springframework.beans.factory.support.DefaultListableBeanFactory.class;

        var context=new GenericApplicationContext();
        context.registerBeanDefinition("messageSource", BeanDefinitionBuilder
                .genericBeanDefinition(ResourceBundleMessageSource.class)
                .addPropertyValue("basenames","messages")
                .addPropertyValue("defaultEncoding","utf-8")
                .getBeanDefinition());
        /**
         * 3、事件体系
         * 类似于各种其他事件体系，spring内置一些事件类型，在context.refresh()方法内会在各个特定环节执行对应事件已注册的回调函数
         * ContextRefreshedEvent：context.refresh()触发，所有的bean被成功加载，后处理的bean被检测激活，所有的singleton bean被预初始化后执行回调
         * ContextStoppedEvent：context.stop()触发，被停止的spring容器可以再次通过调用start()方法重新启动
         * ContextStartdEvent：context.start()触发，这在经常需要停止后重新启动的场合比较常见
         * ContextClossedEvent：context.close()触发
         * RequestHandledEvent：DispatcherServlet执行完请求后触发，可以设置为不触发
         * 关键点：注册事件回调函数
         * 支持可以自定义事件类型，然后 context.publishEvent() 回调事件的监听函数
         */
        context.addApplicationListener(new ApplicationListener<ContextRefreshedEvent>() {
            @Override
            public void onApplicationEvent(ContextRefreshedEvent applicationEvent) {
                System.out.println("ContextRefreshedEvent");
            }
        });
        context.addApplicationListener(new ApplicationListener<ApplicationContextEvent>() {
            @Override
            public void onApplicationEvent(ApplicationContextEvent applicationEvent) {
                System.out.println("ApplicationContextEvent:"+applicationEvent.toString());
            }
        });
        class MyEvent extends ApplicationContextEvent {
            String msg;

            public MyEvent(ApplicationContext source,String msg) {
                super(source);
                this.msg=msg;
            }
        }
        context.addApplicationListener(new ApplicationListener<MyEvent>() {
            @Override
            public void onApplicationEvent(MyEvent applicationEvent) {
                System.out.println("MyEvent:"+applicationEvent.msg);
            }
        });


        context.refresh();
        context.publishEvent(new MyEvent(context,"我触发了MyEvent事件"));
        /**
         * 1、国际化
         * 关键点：context.refresh()中的initMessageSource()方法会获取名字为 messageSource 的bean并且设置为国际化的解析器,
         *        所以需要注册一个名为 messageSource 的bean
         * basenames属性设置多语言配置文件的路径，messages 等价于 classpath:messages
         * 如果把配置文件打包到jar包外面，则使用file:d:/i18/messages
         */
        System.out.println("hi:"+context.getMessage("hi",null, Locale.CHINA));
        System.out.println("hi:"+context.getMessage("hi",null, Locale.ENGLISH));

        context.stop();
        context.start();
        context.close();


    }
}
