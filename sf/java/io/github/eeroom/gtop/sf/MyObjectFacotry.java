package io.github.eeroom.gtop.sf;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@Component
public class MyObjectFacotry implements ApplicationContextAware {
    static ApplicationContext rootcontext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        rootcontext=  applicationContext;
    }

    public static  <T>  T getBean(Class<T> meta){
        return rootcontext.getBean(meta);
    }

    public static   Object getBean(String beanName){
        return rootcontext.getBean(beanName);
    }

    public static String[] getBeanNamesForType(Class<?> meta){
        return rootcontext.getBeanNamesForType(meta);
    }
}
