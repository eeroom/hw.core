package io.github.eeroom.springcore;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import javax.annotation.PostConstruct;

public class ITCast07ApplicationContext04 {
    public static void main(String[] args) {
        /**
         * Aware接口
         * BeanNameAware 注入bean的名字
         * BeanFactoryAware 注入beanfactory容器
         * ApplicationContextAware 注入Application容器
         * EmbeddedValuesResolverAware 注入解析器，解析 ${}
         *
         * Aware接口的场景
         * @Autowired 的解析需要用到bean后处理器，属于扩展功能
         * Aware接口属于内置功能，不加任何扩展，Spring就能识别
         * 某些情况下，扩展功能会失效，但内置功能不会失效
         */
        var context = new GenericApplicationContext();
        context.registerBeanDefinition(MyBean.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(MyBean.class).getBeanDefinition());

        context.registerBeanDefinition(AutowiredAnnotationBeanPostProcessor.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(AutowiredAnnotationBeanPostProcessor.class).getBeanDefinition());
        context.registerBeanDefinition(CommonAnnotationBeanPostProcessor.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(CommonAnnotationBeanPostProcessor.class).getBeanDefinition());

        context.refresh();

        context.close();
        System.out.println("------依赖注入失效----------------------------------");
        context=new GenericApplicationContext();
        context.registerBeanDefinition(MyConfig.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(MyConfig.class).getBeanDefinition());
        context.registerBeanDefinition(AutowiredAnnotationBeanPostProcessor.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(AutowiredAnnotationBeanPostProcessor.class).getBeanDefinition());
        context.registerBeanDefinition(CommonAnnotationBeanPostProcessor.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(CommonAnnotationBeanPostProcessor.class).getBeanDefinition());
        context.registerBeanDefinition(ConfigurationClassPostProcessor.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(ConfigurationClassPostProcessor.class).getBeanDefinition());
        /**
         * 核心步骤：beanfactory后处理器，bean后处理器，创建配置类的单例
         */
        context.refresh();

        context.close();

    }

    @Configuration
    static class MyConfig implements InitializingBean,ApplicationContextAware{
        @Autowired
        public void setcontext(ApplicationContext applicationContext){
            System.out.println("Autowired--MyConfig::setcontext" + applicationContext);
        }

        @PostConstruct
        public void start(){
            System.out.println("@PostConstruct--MyConfig::start");
        }

        /**
         * 如果添加beanfactory后处理器和bean后处理相关的bean，则配置类中 依赖注入会失效
         * @return
         */
        @Bean
        public BeanFactoryPostProcessor beanFactoryPostProcessor(){
            return new BeanFactoryPostProcessor() {
                @Override
                public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
                    System.out.println("beanfactory后处理器 postProcessBeanFactory");
                }
            };
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("@InitializingBean--afterPropertiesSet");
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            System.out.println("ApplicationContextAware--setApplicationContext:" + applicationContext);
        }
    }

    static class MyBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, EmbeddedValueResolverAware, InitializingBean {

        @Override
        public void setBeanName(String s) {
            System.out.println("BeanNameAware--setBeanName:" + s);
        }


        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            System.out.println("BeanFactoryAware--setBeanFactory:" + beanFactory);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            System.out.println("ApplicationContextAware--setApplicationContext:" + applicationContext);
        }

        @Override
        public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
            System.out.println("EmbeddedValueResolverAware--setEmbeddedValueResolver:" + stringValueResolver);
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("InitializingBean--afterPropertiesSet");
        }

        @Autowired
        public void setcontext(ApplicationContext applicationContext){
            System.out.println("Autowired--setcontext:"+applicationContext);
        }

        @PostConstruct
        public void  init(){
            System.out.println("PostConstruct--init:");
        }
    }
}
