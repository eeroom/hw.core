package io.github.eeroom.springboot2xcore;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.RepeatableContainers;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ITCast10DispatcherServlet {

    public static void main(String[] args){

         var lsta= WebConfig.class.getAnnotations();
        var annotationMetadata= AnnotationMetadata.introspect(WebConfig.class);
        var mergedAnnotations= MergedAnnotations.from(WebConfig.class, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS, RepeatableContainers.none());
        var enableConfigurationPropertiesMergedAnnotation= mergedAnnotations.get(Import.class);
var ac= enableConfigurationPropertiesMergedAnnotation.getClass("value");

        var ll=annotationMetadata.getAnnotations();
        var lstclass= annotationMetadata.getAnnotationTypes();
        var lstmeta= annotationMetadata.getMetaAnnotationTypes(Configuration.class.getName());
        var lst= annotationMetadata.getAnnotations().stream(Configuration.class).collect(Collectors.toList());

//                .flatMap((annotation) -> {
//            return Arrays.stream(annotation.getClassArray("value"));
//        }).filter((type) -> {
//            return Void.TYPE != type;
//        }).collect(Collectors.toSet());



        var webcontext=new AnnotationConfigServletWebServerApplicationContext();
        webcontext.register(WebConfig.class);
        webcontext.refresh();
    }

    @Configuration
    @PropertySource(value = {"application.properties"},encoding = "utf-8")
    @EnableConfigurationProperties({WebMvcProperties.class,WebProperties.class,ServerProperties.class})
    static class WebConfig{
        /**
         * springboot web程序 最基本的3个组件bean
         * 内嵌web容器工厂
         * 创建dispatcherServlet
         * 注册dispatcherServlet，spring mvc的入口
         * @return
         */

        @Bean
        public TomcatServletWebServerFactory tomcatServletWebServerFactory(){
            return new TomcatServletWebServerFactory();
        }

        /**
         * 虽然是spring容器的一个bean，但也被注册为tomcat的一个servlet由tomcat触发初始化操作进行初始化
         *
         * @return
         */
        @Bean
        public DispatcherServlet dispatcherServlet(){
            return new DispatcherServlet();
        }

        public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet,WebMvcProperties webMvcProperties){
            var registrationBean= new DispatcherServletRegistrationBean(dispatcherServlet,"/");
            //值大0，tomcat会在启动阶段就是初始化这个servlet,
            // 多个servlet，决定各自的初始顺序
            //springboot约定 对应配置文件 spring.mvc.servlet.load-on-startup=1
            //默认值为-1
            registrationBean.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());
            var webPropertiesClass= WebProperties.class;
            var webMvcPropertiesClass= WebMvcProperties.class;
            var ss= ServerProperties.class;
            return registrationBean;
        }

    }
}
