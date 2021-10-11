package org.azeroth.workflow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;

import javax.servlet.*;

public class App extends  org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer implements ServletContextListener {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        //不启用springsecurity
        //return new Class[]{RootConfig.class, SpringSecurityConfig.class};
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

    ServletRegistration.Dynamic registration;
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        /**
         * 开启支持multipart/form-data，这样才能上传文件
         * 这里是app的父类直接调用到这里，没法利用spring容器的能力读取配置文件，要么硬编码，要么其它方式读取配置文件数据然后进行配置
         * 2021年10月11日更新，利用ServletContextListener，待tomcat启动后，rootconfig的容器refresh之后，通过容器读取配置文件的数据，然后配置上传部分的功能
         */
//         var tmpdir=System.getProperty("java.io.tmpdir");
//        registration.setMultipartConfig(new MultipartConfigElement(tmpdir,2097152,4194304,0));
        this.registration=registration;
    }

    javax.servlet.FilterRegistration.Dynamic authenticationFilterDynamic;
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        //对应web.xml文件的Filter部分
        //后续在contextInitialized中再利用容器读取配置文件数据对filter进行配置
        this.authenticationFilterDynamic= servletContext.addFilter("AuthenticationFilter",AuthenticationFilter.class);
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
        //利用rootconfig的容器读取配置文件数据，然后配置servlet的上传功能
        servletContext.addListener(this);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        var contextRoot= WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
        var mapProperties= contextRoot.getBean(MapProperties.class);
        //配置上传功能
        this.registration.setMultipartConfig(new MultipartConfigElement(mapProperties.uploadtmpdir,mapProperties.maxUploadSize,mapProperties.maxUploadSize,mapProperties.maxInMemorySize));
        //配置filter
        this.authenticationFilterDynamic.setInitParameter("a1",mapProperties.camundaJdbcDriver);
        this.authenticationFilterDynamic.addMappingForUrlPatterns(null,false,"/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
