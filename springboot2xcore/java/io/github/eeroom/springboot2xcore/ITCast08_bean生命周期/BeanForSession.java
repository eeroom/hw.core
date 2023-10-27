package io.github.eeroom.springboot2xcore.ITCast08_bean生命周期;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PreDestroy;

@Scope(WebApplicationContext.SCOPE_SESSION)
@Component
public class BeanForSession{
    public BeanForSession(){
        System.out.println("Session 构造函数:"+this);
    }

    @PreDestroy
    public void end(){
        System.out.println("Session 销毁:"+this);
    }
}
