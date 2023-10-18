package io.github.eeroom.springcore.H06BeanFactory后处理;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MyConfigurationClassPostProcessor implements BeanFactoryPostProcessor {
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        var beanFactory=(DefaultListableBeanFactory)configurableListableBeanFactory;
        var cachingMetadataReaderFactory=new CachingMetadataReaderFactory();
        var lstBeanName=new ArrayList<String>();
        var lstConfigurationBeanName=new ArrayList<String>();
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            var bd= beanFactory.getBeanDefinition(beanDefinitionName);
            var metaReader= this.tryInvoke(()->cachingMetadataReaderFactory.getMetadataReader(bd.getBeanClassName()));
            if(metaReader.getAnnotationMetadata().hasAnnotation(ComponentScan.class.getName())){
                lstBeanName.add(beanDefinitionName);
            }
            if(metaReader.getAnnotationMetadata().hasAnnotation(Configuration.class.getName())){
                lstConfigurationBeanName.add(beanDefinitionName);
            }
        }

        System.out.println("---读取 @ComponentScan注解指定的包中的所有class------------------");
        for (String componetScanConfigClassBeanName : lstBeanName) {
            var metaReader= this.tryInvoke(()->cachingMetadataReaderFactory.getMetadataReader(beanFactory.getBeanDefinition(componetScanConfigClassBeanName).getBeanClassName()));
            var componentScan= AnnotationUtils.findAnnotation(metaReader.getClassMetadata().getClass(), ComponentScan.class);
            var lstBasePath=new ArrayList<String>();
            lstBasePath.addAll(Arrays.stream(componentScan.basePackages()).collect(Collectors.toList()));
            if(lstBasePath.size()<1){
                lstBasePath.add(RootConfig.class.getPackage().getName());
            }
            var annotationBeanNameGenerator= new AnnotationBeanNameGenerator();
            for (String basePackage : lstBasePath) {
                System.out.println(basePackage);
                var path= ApplicationContext.CLASSPATH_URL_PREFIX+ ClassUtils.convertClassNameToResourcePath(basePackage)+"/**/*.class";
                var lstResource=this.tryInvoke(()->new PathMatchingResourcePatternResolver().getResources(path));

                System.out.println("---添加了@Component注解或者其派生注解的类------------------");
                for (Resource resource : lstResource) {
                    var metadataReader=  this.tryInvoke(()->cachingMetadataReaderFactory.getMetadataReader(resource));
                    var annotationMetadata=metadataReader.getAnnotationMetadata();
                    var componentName= Component.class.getName();
                    if(!annotationMetadata.hasAnnotation(componentName) && !annotationMetadata.hasMetaAnnotation(componentName))
                    {
                        continue;
                    }
                    var classMetadata=metadataReader.getClassMetadata();
                    System.out.println(classMetadata.getClassName());
                    var bd= BeanDefinitionBuilder.genericBeanDefinition(classMetadata.getClassName()).getBeanDefinition();
                    var beanName= annotationBeanNameGenerator.generateBeanName(bd,beanFactory);
                    beanFactory.registerBeanDefinition(beanName,bd);
                }
            }
            for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
                System.out.println(beanDefinitionName);
            }
        }
        System.out.println("---解析 @Configuration注解类中的@Bean-----------------");
        for (String beanName : lstConfigurationBeanName) {
            var metadataReader=this.tryInvoke(()->cachingMetadataReaderFactory.getMetadataReader(beanFactory.getBeanDefinition(beanName).getBeanClassName()));
            var lstmethod= metadataReader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
            for (MethodMetadata methodMetadata : lstmethod) {
                //特别的：对于构造函数和工厂方法的bean定义，参数依赖解析使用AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR
                var builder=BeanDefinitionBuilder.genericBeanDefinition()
                        .setFactoryMethodOnBean(methodMetadata.getMethodName(),beanName)
                        .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                var initMethod= methodMetadata.getAnnotationAttributes(Bean.class.getName()).get("initMethod");
                if(!StringUtils.isEmpty(initMethod)){
                    builder.setInitMethodName(initMethod.toString());
                }
                beanFactory.registerBeanDefinition(methodMetadata.getMethodName(),builder.getBeanDefinition());
            }
        }
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }


    }

    <T> T tryInvoke(MySupplier<T> handler){
        try {
            return handler.get();
        } catch (Throwable throwable) {
            throw  new RuntimeException(throwable);
        }
    }
}
