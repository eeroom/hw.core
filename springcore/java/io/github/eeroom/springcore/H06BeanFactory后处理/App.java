package io.github.eeroom.springcore.H06BeanFactory后处理;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class App {

    /**
     * beanFactory后处理器及其实现原理
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
        var context=new GenericApplicationContext();
        context.registerBeanDefinition(RootConfig3.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(RootConfig3.class).getBeanDefinition());
        //解析@ComponentScan  @Bean  @Import @ImportResource
        context.registerBeanDefinition(ConfigurationClassPostProcessor.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(ConfigurationClassPostProcessor.class).getBeanDefinition());
        context.refresh();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("---递归读取所有 @Import------------------");
        LinkedList<Annotation> lstanotation=new LinkedList<>();
        HashSet<Import> lstImport=new HashSet<>();
        HashSet<Annotation> lstAdd=new HashSet<>();
        var lstA= AnnotationUtils.getAnnotations(RootConfig3.class);
        for (Annotation annotation : lstA) {
            lstanotation.add(annotation);
        }

        while (lstanotation.size()>0){
            Annotation tmp=lstanotation.removeFirst();
            if(!lstAdd.add(tmp)){
                continue;
            }
            if(tmp instanceof Import){
                lstImport.add((Import) tmp);
                continue;
            }
            var tmpc=tmp.annotationType();
            var lstemp= AnnotationUtils.getAnnotations(tmpc);
            for (Annotation annotation : lstemp) {
                lstanotation.add(annotation);
            }
        }
        for (Import anImport : lstImport) {
            System.out.println(anImport);
        }

        System.out.println("----genericApplicationContext----MyConfigurationClassPostProcessor-------------------");
        var genericApplicationContext=new GenericApplicationContext();
        genericApplicationContext.registerBeanDefinition(MyConfigurationClassPostProcessor.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(MyConfigurationClassPostProcessor.class).getBeanDefinition());
        genericApplicationContext.registerBeanDefinition(RootConfig3.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(RootConfig3.class).getBeanDefinition());
        genericApplicationContext.refresh();
    }
}
