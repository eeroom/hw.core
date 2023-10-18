package io.github.eeroom.springcore.H06BeanFactory后处理;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

public class RootConfig2 implements org.springframework.context.ApplicationEventPublisherAware,org.springframework.context.ApplicationContextAware {
    ApplicationEventPublisher applicationEventPublisher;
    ApplicationContext applicationContext;
    @Bean
    public String seyhello(){
        var me=new MyEvent(this.applicationContext);
        me.tag="hello owr";
        //触发自定义事件
        this.applicationEventPublisher.publishEvent(me);
        this.applicationContext.publishEvent(me);
        return "sayhello";
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher=applicationEventPublisher;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
