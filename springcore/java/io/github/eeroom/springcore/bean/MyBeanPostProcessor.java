package io.github.eeroom.springcore.bean;

import io.github.eeroom.springcore.bean.Calculation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

/**
 * 这个特殊的bean,所以本身仍然是bean,所以可以按照普通的自动扫包进行注册
 * 这里的注册方式为：利用ImportBeanDefinitionRegistrar这个特殊的类进注册
 */
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        if(!(bean instanceof Calculation))
            return bean;
        var obj=(Calculation)bean;
        //obj.url="sdffffffffff";
        return obj;
    }
}
