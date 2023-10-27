package io.github.eeroom.springboot2xcore.ITCast08_bean生命周期;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PreDestroy;

@Scope(WebApplicationContext.SCOPE_REQUEST)
@Component
public class BeanForRequest{
    public BeanForRequest(){
        System.out.println("Request 构造函数:"+this);
    }

    @PreDestroy
    public void end(){
        System.out.println("Request 销毁:"+this);
    }
}
