package org.azeroth.springboot1x;


import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizerBeanPostProcessor;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.AbstractFilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.web.servlet.DispatcherServlet;

@org.springframework.boot.autoconfigure.SpringBootApplication
public class App {
    public static void main(String[] args){
        //springboot新增的读取配置文件的注解类，非常方便
        var configurationProperties= ConfigurationProperties.class;

        //整个程序的核心springcontext，SpringApplication.run方法最终会创建出这个context,并且调用refresh,这个是spring容器的常规操作
        //这个类重新onRefresh,加入私货createEmbeddedServletContainer，
        //createEmbeddedServletContainer方法会从context拿到EmbeddedServletContainerFactory的实例，这个bean的注册点就在EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat
        //然后调用EmbeddedServletContainerFactory的接口方法，并且传入一个ServletContextInitializer的实例，让后续调用回到selfInitialize方法
        //ServletContextInitializer就是servlet3.1以后的约定，tomcat是约定找classpath/META-INF/services/javax.servlet.ServletContrainerInitalizer中声明的类，实例化，然后调用接口，从而调用到我们的代码中
        //TomcatEmbeddedServletContainerFactory的getEmbeddedServletContainer中是直接创建一个ServletContainerInitializer的实例TomcatStarter，然后添加到tomcat的context中
        //ServletContextInitializer的实例被放到ServletContainerInitializer的实例TomcatStarter中，后续tomcat启动后就最终调回AnnotationConfigEmbeddedWebApplicationContext中的selfInitialize，
        //selfInitialize方法中，会传入servletcontext,然后把springcontext存到servletcotnext中，把dispatchservlet添加到servletcontext中
        var contextClass= org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext.class;
        var tomcatEmbeddedServletContainerFactory= org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory.class;
        var servletContextInitializer= ServletContextInitializer.class;
        var ServletContrainerInitalizer= javax.servlet.ServletContainerInitializer.class;
        //spring根据这里面定义的bean创建DispatcherServlet的实例，然后进行配置,内部的重要配置类：DispatcherServletRegistrationConfiguration，DispatcherServletConfiguration
        var dispatcherServletAutoConfiguration= DispatcherServletAutoConfiguration.class;
        //DispatcherServletConfiguration定义非常关键的bean;dispatchservlet的bean,后续tomcat利用servlet约定调用onstart都会涉及到他
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        //DispatcherServletRegistrationConfiguration关键的bean，这个bean
        var dsrc= ServletRegistrationBean.class;
        //AbstractFilterRegistrationBean
        var s= AbstractFilterRegistrationBean.class;

        //这个配置类，利用import ImportBeanDefinitionRegistrar的约定，注册EmbeddedServletContainerCustomizerBeanPostProcessor；
        var servletContainerConfigClass= EmbeddedServletContainerAutoConfiguration.class;
        //读取application.properties中的配置信息修改tomcat的实例
        var embcc= EmbeddedServletContainerCustomizerBeanPostProcessor.class;
        //创建tomcat实例,添加ServletContainerInitializer，启动tomcat
        var embededTomcatClass= EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class;
        var tomcatEmbeddedServletContainerFactory2= org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory.class;
        //initialize方法中启动tomcat
        var tomcatEmbeddedServletContainer= TomcatEmbeddedServletContainer.class;

        //org.springframework.boot.autoconfigure.SpringBootApplication最终import了EnableAutoConfigurationImportSelector
        // springcontext的大量的配置都是利用EnableAutoConfigurationImportSelector注册的
        //spring的重要约定接口DeferredImportSelector和ImportSelector
        var deferredImportSelector= DeferredImportSelector.class;

        //涉及mvc的配置类
        var mvccfg1= org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.class;
        var mvccfg2= org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.class;
        var mvccfg3= org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.EnableWebMvcConfiguration.class;


        //org.springframework.boot.SpringApplication.run(App.class);
        var app=new org.springframework.boot.SpringApplication(App.class);
        app.run();
    }
}
