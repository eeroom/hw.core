package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.FailFastProblemReporter;
import org.springframework.beans.factory.parsing.PassThroughSourceExtractor;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.LinkedHashSet;

public class AppConfigurationClassPostProcessor {

    public static void main(String[] args){
        /**
         * ConfigurationClassPostProcessor 的内部核心逻辑
         */
        var beanFactory=new DefaultListableBeanFactory();
        var beanName="RootConfig";
        beanFactory.registerBeanDefinition(beanName,
                BeanDefinitionBuilder.genericBeanDefinition(org.springframework.context.annotation.RootConfig.class).getBeanDefinition());
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
        ProblemReporter problemReporter = new FailFastProblemReporter();
        Environment environment=new StandardEnvironment();
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        BeanNameGenerator componentScanBeanNameGenerator = new AnnotationBeanNameGenerator();
        ConfigurationClassParser parser =
                new ConfigurationClassParser(metadataReaderFactory, problemReporter, environment, resourceLoader, componentScanBeanNameGenerator, beanFactory);

        var rootconfigDef=beanFactory.getBeanDefinition(beanName);
        var rootconfigDefHolder= new BeanDefinitionHolder(rootconfigDef,beanName);
        var lstDefHolder = new LinkedHashSet();
        lstDefHolder.add(rootconfigDefHolder);

        parser.parse(lstDefHolder);
        parser.validate();
        var lstConfigClass=parser.getConfigurationClasses();
        SourceExtractor sourceExtractor = new PassThroughSourceExtractor();
        BeanNameGenerator importBeanNameGenerator = new AnnotationBeanNameGenerator() {
            protected String buildDefaultBeanName(BeanDefinition definition) {
                return definition.getBeanClassName();
            }
        };
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("-------ConfigurationClassBeanDefinitionReader----------------------------------");
        ConfigurationClassBeanDefinitionReader reader=
                new ConfigurationClassBeanDefinitionReader(beanFactory, sourceExtractor, resourceLoader, environment, importBeanNameGenerator, parser.getImportRegistry());
        reader.loadBeanDefinitions(lstConfigClass);

        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("-------DefaultListableBeanFactory----------------------------------");
        System.out.println(beanFactory.getBean(beanName));
        System.out.println(beanFactory.getBean("bean2"));
        System.out.println("-------AnnotationConfigApplicationContext----------------------------------");
        var cc=new AnnotationConfigApplicationContext(RootConfig.class);
        cc.getBean("bean2");


    }
}
