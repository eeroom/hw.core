package io.github.eeroom.springboot2xcore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SpringBootApplication
@ComponentScan(excludeFilters ={@ComponentScan.Filter(classes = {App.class,App.WebConfig.class,App.SystemConfigInfo.class},type = FilterType.ASSIGNABLE_TYPE)} )
public class ITCast08Scop {

    public static void main(String[] args){

        /**
         * 打开浏览器 http://localhost:8080/home/say
         * 相同选项卡，可以观察每次刷新的结果 Session的不会变，Request会变
         * 相同浏览器不同选项卡，可以观察每次刷新的结果 Session和隔壁选项卡的相同，Request会变
         * 打开不同浏览器，可以观察每次刷新的结果 Session和隔壁浏览器的不同，Request会变
         * 修改servlet的失效时间为10s,但是tomcat内部对session的失效检测内部有一个最小刷新周期，大概1分钟
         * 等待一段时间，可以在idea的运行结果窗口查看销毁情况，request的每次都会销毁，session需要等待一段时间
         */
        var context= SpringApplication.run(ITCast08Scop.class);
    }


    @Scope(BeanDefinition.SCOPE_SINGLETON)
    @Component
    static class BeanForSingleton{
        public BeanForSingleton(){
            System.out.println("Singleton构造函数:"+this);
        }

        @PreDestroy
        public void end(){
            System.out.println("Singleton销毁:"+this);
        }
    }

    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Component
    static class BeanForProperty{
        public BeanForProperty(){
            System.out.println("Property 构造函数:"+this);
        }

        @PreDestroy
        public void end(){
            System.out.println("Property 销毁:"+this);
        }
    }

    @Scope(WebApplicationContext.SCOPE_REQUEST)
    @Component
    static class BeanForRequest{
        public BeanForRequest(){
            System.out.println("Request 构造函数:"+this);
        }

        @PreDestroy
        public void end(){
            System.out.println("Request 销毁:"+this);
        }
    }

    @Scope(WebApplicationContext.SCOPE_SESSION)
    @Component
    static class BeanForSession{
        public BeanForSession(){
            System.out.println("Session 构造函数:"+this);
        }

        @PreDestroy
        public void end(){
            System.out.println("Session 销毁:"+this);
        }
    }

    @Scope(WebApplicationContext.SCOPE_APPLICATION)
    @Component
    static class BeanForApplication{
        public BeanForApplication(){
            System.out.println("Application 构造函数:"+this);
        }

        @PreDestroy
        public void end(){
            System.out.println("Application 销毁:"+this);
        }
    }


}
