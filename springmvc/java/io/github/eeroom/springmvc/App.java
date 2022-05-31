package io.github.eeroom.springmvc;

import io.github.eeroom.springmvc.authen.AuthenticationFilter;
import io.github.eeroom.springmvc.authen.SpringSecurityConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

import javax.servlet.*;

/**
 * servlet3.0新增加的规范，利用spi机制,servlet容器(tomcat)会在启动过程中,查找各个jar包中的特定文件：META-INF/services/javax.servlet.ServletContainerInitializer
 * 如果存在这个文件,就会利用spi机制获取接口javax.servlet.ServletContainerInitializer的实现类,然后执行其onStartup方法,可以有多个实现类，挨个执行
 * 同时：新增规范还有一个注解@HandlesTypes,HandlesTypes的参数的类型就是class类型,表示javax.servlet.ServletContainerInitializer当前实现类能处理的类型
 * servlet容器会扫描@HandlesTypes参数中的类型的实现类,并且创建这个实现类的实例,然后作为ServletContainerInitializer的onStartup的参数
 * springmvc从3.1开始,在spring-web-xxxx.jar包中增加了这个特定文件,并且内容为：org.springframework.web.SpringServletContainerInitializer
 * 并且SpringServletContainerInitializer的@HandlesTypes注解参数为WebApplicationInitializer.class
 * 所以servlet容器会扫描WebApplicationInitializer.class的实现类然后作为参数传给onStartup方法
 * 然后在ServletContainerInitializer的onStartup方法中,又挨个执行WebApplicationInitializer.class实现类的onStartup方法
 * 当前的APP类最终就是WebApplicationInitializer.class的实现类
 * 所以会自动被servlet容器实例化,实例值作为参数传递给SpringServletContainerInitializer的onStartup方法
 * 我们的程序可以有多个WebApplicationInitializer.class的实现类
 */
public class App extends  org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        //不启用springsecurity
        //return new Class[]{RootConfig.class, SpringSecurityConfig.class};
        var servletContainerInitializer=javax.servlet.ServletContainerInitializer.class;
        var handlesTypes= javax.servlet.annotation.HandlesTypes.class;
        var springServletContainerInitializer=  org.springframework.web.SpringServletContainerInitializer.class;
        var webApplicationInitializer= org.springframework.web.WebApplicationInitializer.class;
        return new Class[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        //不启用springsecurity
        //开启spring-security，实现自己的登陆校验，把springsecurity相关的先不加载
        //var springSecurityFilterChain=new org.springframework.web.filter.DelegatingFilterProxy("springSecurityFilterChain");
        var authenticationFilter=new org.springframework.web.filter.DelegatingFilterProxy("authenticationFilter");
        return new Filter[]{authenticationFilter};
        //结束spring-security
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        //开启支持multipart/form-data，这样才能上传文件
        registration.setMultipartConfig(new MultipartConfigElement("D:/springmvc_multipart_formdata",2097152,4194304,0));
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        //对应web.xml文件的Filter部分
        var fdy= servletContext.addFilter("AuthenticationFilter", AuthenticationFilter.class);
        fdy.setInitParameter("a","hello world");
        fdy.addMappingForUrlPatterns(null,false,"/*");
    }

    @Override
    protected FrameworkServlet createDispatcherServlet(WebApplicationContext context) {
        return super.createDispatcherServlet(context);
    }

    @Override
    protected void registerContextLoaderListener(ServletContext servletContext) {
        //对应web.xml文件的Listener部分
        super.registerContextLoaderListener(servletContext);
        //添加这个RequestContextListener，就可以在filter里面使用ioc的时候，处理request,session等作用域的bean
        org.springframework.web.context.request.RequestContextListener listener=new org.springframework.web.context.request.RequestContextListener();
        servletContext.addListener(listener);
    }
}
