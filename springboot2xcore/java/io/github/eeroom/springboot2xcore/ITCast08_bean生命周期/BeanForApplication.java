package io.github.eeroom.springboot2xcore.ITCast08_bean生命周期;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PreDestroy;

@Scope(WebApplicationContext.SCOPE_APPLICATION)
@Component
public class BeanForApplication{
    public BeanForApplication(){
        System.out.println("Application 构造函数:"+this);
    }

    @PreDestroy
    public void end(){
        System.out.println("Application 销毁:"+this);
    }
}
