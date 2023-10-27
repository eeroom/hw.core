package io.github.eeroom.springcore.H01ApplicationContext和BeanFactory的关系;

import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.ResourcePropertySource;

public class 资源和环境变量增强 {

    public static void main(String[] args) throws Throwable{
        /**
         * 根据通配符获取资源
         * 关键点：通配符，本质就是目标文件的路径
         * classpath:application.properties 仅在当前项目的jar包中找
         * classpath*:*.properties 在所有加载的jar包中找
         * file:d:/abc.properties 在指定的磁盘路径找
         */
        var context=new GenericApplicationContext();
        //Resource 是spring中定义的类，是对 InputStream 的组合和功能增强
        var resource1= context.getResource("classpath:application.properties");
        var resource2= context.getResources("classpath*:META-INF/spring.factories");
        var resource3= context.getResource("file:d:/abc.properties");

        /**
         * 环境变量增强
         * 整合如下5类的变量值：
         * 系统环境变量
         * jvm环境变量
         * jvm参数 -D参数名=参数值
         * springboot程序 --参数=参数值
         * 自定义的配置文件数据： context.getEnvironment().getPropertySources().addLast(new ResourcePropertySource("classpath:application.properties"))
         */
        System.out.println("java_home:"+context.getEnvironment().getProperty("java_home"));
        System.out.println("java.vm.version(context.getEnvironment().getProperty):"+context.getEnvironment().getProperty("java.vm.version"));
        System.out.println("java.vm.version(System.getProperty):"+System.getProperty("java.vm.version"));
        //ResourcePropertySource,本质就是解析特定格式的解析器，和web.config的Appsetting类似
        context.getEnvironment().getPropertySources().addLast(new ResourcePropertySource("classpath:application.properties"));
        System.out.println("mqs.url(context.getEnvironment().getProperty):"+context.getEnvironment().getProperty("mqs.url"));
    }
}
