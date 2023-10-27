package io.github.eeroom.springboot2xcore.ITCast08_bean生命周期;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
public class BeanForProperty{
    public BeanForProperty(){
        System.out.println("Property 构造函数:"+this);
    }

    @PreDestroy
    public void end(){
        System.out.println("Property 销毁:"+this);
    }
}
