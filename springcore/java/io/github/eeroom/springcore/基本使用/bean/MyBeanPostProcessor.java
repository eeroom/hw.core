package io.github.eeroom.springcore.基本使用.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

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
