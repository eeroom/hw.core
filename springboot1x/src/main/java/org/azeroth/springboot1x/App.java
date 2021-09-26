package org.azeroth.springboot1x;


import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizerBeanPostProcessor;

@org.springframework.boot.autoconfigure.SpringBootApplication
public class App {
    public static void main(String[] args){
        //整个程序的核心springcontext
        var contextClass= org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext.class;
        //这个配置类，利用import ImportBeanDefinitionRegistrar的约定，注册EmbeddedServletContainerCustomizerBeanPostProcessor；
        var servletContainerConfigClass= EmbeddedServletContainerAutoConfiguration.class;
        //读取application.properties中的配置信息修改tomcat的实例
        var embcc= EmbeddedServletContainerCustomizerBeanPostProcessor.class;
        //创建tomcat实例
        var embededTomcatClass= EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class;
        org.springframework.boot.SpringApplication.run(App.class);
    }
}
