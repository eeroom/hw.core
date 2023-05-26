package io.github.eeroom.springcore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.util.ResourceBundle;

public class ITCast03ApplicationContext02 {
    public static void main(String[] args) throws Throwable {
        System.out.println("----ClassPathXmlApplicationContext-------------------------------------------");
        var classPathXmlApplicationContext=new ClassPathXmlApplicationContext();
        classPathXmlApplicationContext.setConfigLocation("bean.xml");
        classPathXmlApplicationContext.refresh();
        System.out.println("bean1:"+(Bean1)classPathXmlApplicationContext.getBean(Bean1.class));

        System.out.println("----FileSystemXmlApplicationContext,jar文件同级目录需要bean2.xml文件-------------------------------------------");
        var fileSystemXmlApplicationContext=new FileSystemXmlApplicationContext("bean2.xml");
        System.out.println("bean1:"+(Bean1)fileSystemXmlApplicationContext.getBean(Bean1.class));

        System.out.println("----XmlBeanDefinitionReader-------------------------------------------");
        var beanFactory=new DefaultListableBeanFactory();
        var xmlreader=new XmlBeanDefinitionReader(beanFactory);
        xmlreader.loadBeanDefinitions(new FileSystemResource("bean2.xml"));
        xmlreader.loadBeanDefinitions(new ClassPathResource("bean.xml"));
        System.out.println("bean1:"+(Bean1)beanFactory.getBean(Bean1.class));

        System.out.println("----AnnotationConfigApplicationContext-------------------------------------------");
        var annotationConfigApplicationContext=new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.register(Config.class);
        annotationConfigApplicationContext.refresh();
        for (String beanDefinitionName : annotationConfigApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }





    }

    @Configuration
    static class Config{

        Config(){
            System.out.println("Config 被创建了");
        }

        @Bean
        public Bean3 bean3(Bean4 bean4){
            var bean3=new Bean3();
            bean3.target=bean4;
            return bean3;
        }

        @Bean
        public Bean4 bean4(){
            return new Bean4();
        }
    }

    static class  Bean3{

        Bean4 target;

        Bean3(){
            System.out.println("Bean3 被创建了");
        }
    }

    static class Bean4{
        Bean4(){
            System.out.println("Bean4 被创建了");
        }

    }

    /**
     * xml的bean定义配置使用
     */
    static class  Bean1{
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Bean2 getTarget() {
            return target;
        }

        public void setTarget(Bean2 target) {
            this.target = target;
        }

        Bean2 target;
    }

    /**
     * xml的bean定义配置使用
     */
    static class Bean2{

    }
}
