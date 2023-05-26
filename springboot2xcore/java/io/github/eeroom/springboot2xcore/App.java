package io.github.eeroom.springboot2xcore;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App {
    public static void main(String[] args) throws Throwable {
        var annotationConfigServletWebServerApplicationContext=new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);


        for (String beanDefinitionName : annotationConfigServletWebServerApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }


    }

    @Configuration
    static  class WebConfig{

        @Bean
        public ServletWebServerFactory servletWebServerFactory(){
            return new TomcatServletWebServerFactory();
        }

        @Bean
        public DispatcherServlet dispatcherServlet(){
            return  new DispatcherServlet();
        }

        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet){
            return new DispatcherServletRegistrationBean(dispatcherServlet,"/");
        }

        @Bean("/home")
        public Controller home(){
            return new Controller() {
                @Override
                public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
                    httpServletResponse.getWriter().println("hello world");
                    return null;
                }
            };
        }
    }
}
