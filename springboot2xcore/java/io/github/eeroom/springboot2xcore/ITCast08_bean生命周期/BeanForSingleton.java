package io.github.eeroom.springboot2xcore.ITCast08_bean生命周期;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Scope(BeanDefinition.SCOPE_SINGLETON)
@Component
public class BeanForSingleton{
    public BeanForSingleton(){
        System.out.println("Singleton构造函数:"+this);
    }

    @PreDestroy
    public void end(){
        System.out.println("Singleton销毁:"+this);
    }
}
