package io.github.eeroom.springcore.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * ImportBeanDefinitionRegistrar的实现类需要借助其它的配置类，把CalculationBeanPostProcessorRegistrar作为import的参数，实现代码运行时注册bean
 */
public class MyBeanPostProcessorRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
    BeanFactory beanFactory;
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(MyBeanPostProcessor.class);
        beanDefinition.setSynthetic(true);
        //使用这个方式把CalculationBeanPostProcessor注册到springcontext中
        beanDefinitionRegistry.registerBeanDefinition(MyBeanPostProcessor.class.getName(), beanDefinition);
    }


    /*
    查看这个调用的方法栈，可以看到这也代码写死的一个约定，如果当前类型实现BeanFactoryAware，ResourceLoaderAware等aware,就会调用对应接口的方法，忽略ApplicationContextAware
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }
}
