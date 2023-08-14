package io.github.eeroom.springboot2xcore;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App {
    public static void main(String[] args) throws Throwable {

        var context=new GenericApplicationContext();
        //中文会乱码，所以额外处理
        var encodedResource=new EncodedResource(context.getResource(ApplicationContext.CLASSPATH_URL_PREFIX+"web.config"),"utf-8");
        var propertySource=new ResourcePropertySource(encodedResource);

        context.registerBean(SystemConfigInfo.class,x->x.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE));

        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());
        context.refresh();
        //如果对应的bean是单例，则必须在 context.refresh()之前添加，否则@ConfigurationProperties(prefix = "wch") 标注的bean 取不到这个resource中配置信息
        //因为context中执行了提前实例化单例bean
        //如果对应的bean是多例，则只要在获取bean之前添加，都能正常获取到配置值
        context.getEnvironment().getPropertySources().addLast(propertySource);

        var configInfo= context.getBean(SystemConfigInfo.class);
        System.out.println("wch.name="+configInfo.name);
        System.out.println("wch.age="+configInfo.age);


        var annotationConfigServletWebServerApplicationContext=new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);


        for (String beanDefinitionName : annotationConfigServletWebServerApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }


    }

    @ConfigurationProperties(prefix = "wch")
    static class SystemConfigInfo{
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        int age;
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
