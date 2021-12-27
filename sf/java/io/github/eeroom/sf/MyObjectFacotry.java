package io.github.eeroom.sf;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

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
}
