package io.github.eeroom.springcore.H02BeanFactory不会做的事和后处理器;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

public class ITCast02BeanFactory {

    public static void main(String[] args){
        var beanFactory=new DefaultListableBeanFactory();
        /**
         * bean的定义：所属类型，scop，初始化，销毁
         * beanFactory不会做的事情
         * 1、不会主动执行beanfactory后处理器
         * 2、不会主动添加bean后处理器
         * 3、不会主动预先创建单例对象
         * 4、不会解析${} #{}
         *
         * bean后处理器具有排序逻辑：
         */
        var configDefinition= BeanDefinitionBuilder.genericBeanDefinition(Config.class)
                .setScope(DefaultListableBeanFactory.SCOPE_SINGLETON)
                .getBeanDefinition();
        beanFactory.registerBeanDefinition("config",configDefinition);
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println("bean定义："+beanDefinitionName);
        }
        /**
         * DefaultListableBeanFactory默认情况下不识别 @Configuration   @Bean @Component 等注解
         * beanfactory的后处理功能本质：注册bean定义，bean定义的来源则是根据各个处理器的约定，比如M后处理约定把@abc标注的class注册为一个bean定义
         * bean后处理器功能本质：针对bean生命周期的各个阶段提供扩展，例如 @Autowired @Resource
         */
        System.out.println("---注册常用的beanfactoy后处理器和bean后处理器的bean定义------------------------------------");
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println("bean定义："+beanDefinitionName);
        }
        System.out.println("---运行beanfactoy后处理器------------------------------------");
        var dictBeanFactoryPostProcessor= beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        dictBeanFactoryPostProcessor.forEach((k,v)->{
            v.postProcessBeanFactory(beanFactory);
        });
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println("bean定义："+beanDefinitionName);
        }
        System.out.println("bean1.target="+beanFactory.getBean(Bean1.class).target);
        System.out.println("---给beanfactory添加bean处理器------------------------------------");
        var lstBeanPostProcessor=beanFactory.getBeansOfType(BeanPostProcessor.class);
        lstBeanPostProcessor.values().forEach(beanFactory::addBeanPostProcessor);
        System.out.println("bean1.target="+beanFactory.getBean(Bean1.class).target);
        System.out.println("---预先创建单例对象，常见的application实现在refresh方法中都会调用此方法--");
        beanFactory.preInstantiateSingletons();

    }

    @Configuration
    static class Config{

        Config(){
            System.out.println("config被创建了");
        }

        @Bean
        @Scope(DefaultListableBeanFactory.SCOPE_PROTOTYPE)
        public Bean1 bean1(){
            return new Bean1();
        }

        @Bean
        @Scope(DefaultListableBeanFactory.SCOPE_PROTOTYPE)
        public Bean2 bean2(){
            return new Bean2();
        }
    }

    static class  Bean1{
        @Autowired
        Bean2 target;
    }

    static class Bean2{

    }
}
