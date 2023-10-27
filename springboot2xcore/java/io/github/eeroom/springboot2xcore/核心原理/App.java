package io.github.eeroom.springboot2xcore.核心原理;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

public class App {

    public static void main(String[] args){
        /**
         * 不使用SpringApplication.run() 和 @SpringBootApplication 启动内嵌tomcat并且接口服务
         */
        var webcontext=new AnnotationConfigServletWebServerApplicationContext();
        webcontext.register(WebConfig.class);
        webcontext.refresh();

        /**
         * spring 提供的大量处理 注解的 工具
         */
        var lsta = WebConfig.class.getAnnotations();
        var mergedAnnotations = MergedAnnotations.from(WebConfig.class, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS, RepeatableContainers.none());
        var enableConfigurationPropertiesMergedAnnotation = mergedAnnotations.get(Import.class);
        var ac = enableConfigurationPropertiesMergedAnnotation.getClass("value");

        var annotationMetadata = AnnotationMetadata.introspect(WebConfig.class);
        var ll = annotationMetadata.getAnnotations();
        var lstclass = annotationMetadata.getAnnotationTypes();
        var lstmeta = annotationMetadata.getMetaAnnotationTypes(Configuration.class.getName());
        var lst = annotationMetadata.getAnnotations().stream(Configuration.class).collect(Collectors.toList());
    }
}
