package io.github.eeroom.springmvc;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

import javax.servlet.*;

/**
 * SPI机制，Service Provider Interface，服务提供者接口，服务是接口或者抽象类，服务提供者负责实现。在做插件化功能时很实用。
 * servlet3.0新增加的规范,容器会在类路径中查找实现javax.servlet.ServletContainerInitializer接口的类
 */
public class App extends  org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        //不启用springsecurity
        //return new Class[]{RootConfig.class, SpringSecurityConfig.class};
        var servletContainerInitializer=javax.servlet.ServletContainerInitializer.class;
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

//    @Override
//    protected Filter[] getServletFilters() {
//        //不启用springsecurity
//        //开启spring-security，实现自己的登陆校验，把springsecurity相关的先不加载
////        var filter=new org.springframework.web.filter.DelegatingFilterProxy("springSecurityFilterChain");
////        return new Filter[]{filter};
//        //结束spring-security
//    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        //开启支持multipart/form-data，这样才能上传文件
        registration.setMultipartConfig(new MultipartConfigElement("D:/springmvc_multipart_formdata",2097152,4194304,0));
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        //对应web.xml文件的Filter部分
        var fdy= servletContext.addFilter("AuthenticationFilter",AuthenticationFilter.class);
        fdy.setInitParameter("a","hello world");
        fdy.addMappingForUrlPatterns(null,false,"/*");
    }

    @Override
    protected FrameworkServlet createDispatcherServlet(WebApplicationContext context) {
        return new MyDispatcherServlet(context);
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
