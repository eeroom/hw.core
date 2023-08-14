package io.github.eeroom.springboot2xcore;

import io.github.eeroom.springboot2xcore.ITCast08Scop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class HomeController {
    @Lazy
    @Autowired
    ITCast08Scop.BeanForProperty beanForProperty;

    @Lazy
    @Autowired
    ITCast08Scop.BeanForSingleton beanForSingleton;

    @Lazy
    @Autowired
    ITCast08Scop.BeanForRequest beanForRequest;

    @Lazy
    @Autowired
    ITCast08Scop.BeanForSession beanForSession;

    @Lazy
    @Autowired
    ITCast08Scop.BeanForApplication beanForApplication;

    @RequestMapping(path = "home/say")
    public void say(HttpServletResponse httpServletResponse) throws Throwable{
        httpServletResponse.getWriter().println("beanForProperty :"+beanForProperty);
        httpServletResponse.getWriter().println("beanForSingleton :"+beanForSingleton);
        httpServletResponse.getWriter().println("beanForRequest :"+beanForRequest);
        httpServletResponse.getWriter().println("beanForSession :"+beanForSession);
        httpServletResponse.getWriter().println("beanForApplication :"+beanForApplication);
    }
}
