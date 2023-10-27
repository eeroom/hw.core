package io.github.eeroom.springboot2xcore.ITCast08_bean生命周期.controller;

import io.github.eeroom.springboot2xcore.ITCast08_bean生命周期.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class Home {
    @Lazy
    @Autowired
    BeanForProperty beanForProperty;

    @Lazy
    @Autowired
    BeanForSingleton beanForSingleton;

    @Lazy
    @Autowired
    BeanForRequest beanForRequest;

    @Lazy
    @Autowired
    BeanForSession beanForSession;

    @Lazy
    @Autowired
    BeanForApplication beanForApplication;

    @RequestMapping(path = "home/say")
    public void say(HttpServletResponse httpServletResponse) throws Throwable{
        httpServletResponse.getWriter().println("beanForProperty :"+beanForProperty);
        httpServletResponse.getWriter().println("beanForSingleton :"+beanForSingleton);
        httpServletResponse.getWriter().println("beanForRequest :"+beanForRequest);
        httpServletResponse.getWriter().println("beanForSession :"+beanForSession);
        httpServletResponse.getWriter().println("beanForApplication :"+beanForApplication);
    }
}
