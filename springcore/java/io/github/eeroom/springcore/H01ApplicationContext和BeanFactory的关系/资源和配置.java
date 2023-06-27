package io.github.eeroom.springcore.H01ApplicationContext和BeanFactory的关系;

import org.springframework.context.support.GenericApplicationContext;

public class 资源和配置 {

    public static void main(String[] args) throws Throwable{
        //根据通配对应的文件路径读取资源
        //Resource 是spring中定义的类，是对 InputStream 的组合和功能增强
        var context=new GenericApplicationContext();
        //仅在当前项目的jar包中找
        var resource1= context.getResource("classpath:application.properties");
        //在所有加载的jar包中找
        var resources2= context.getResources("classpath*:META-INF/spring.factories");
        // 在指定的磁盘路径找
        var resource3= context.getResource("file:d:/abc.properties");
    }
}
