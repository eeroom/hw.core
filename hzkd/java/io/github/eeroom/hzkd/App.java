package io.github.eeroom.hzkd;

import io.github.eeroom.hzkd.cors.AllowAnyOriginFilter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.web.Log4jServletContextListener;
import org.apache.logging.log4j.web.Log4jServletFilter;
import org.apache.logging.log4j.web.WebLoggerContextUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

import javax.servlet.*;
import java.util.EnumSet;

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
        var corscfg=servletContext.addFilter("CORSFilter", AllowAnyOriginFilter.class);
        corscfg.addMappingForUrlPatterns(null,false,"/*");
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
        //2021年10月15日更新，改用springcontext的事件体系，利用ContextRefreshedEvent事件的回调方法
        //servletContext.addListener(this);
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        /**
         * 利用ServletContextListener的回调方法实现：利用容器读取配置信息然后配置servlet的上传功能，配置filter的初始参数
         * 2021年10月15日更新，改用springcontext的事件体系，利用ContextRefreshedEvent事件的回调方法
         */
//        var contextRoot= WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
//        var mapProperties= contextRoot.getBean(MapProperties.class);
//        //配置上传功能
//        this.registration.setMultipartConfig(new MultipartConfigElement(mapProperties.uploadtmpdir,mapProperties.maxUploadSize,mapProperties.maxUploadSize,mapProperties.maxInMemorySize));
//        //配置filter
//        this.authenticationFilterDynamic.setInitParameter("a1",mapProperties.camundaJdbcDriver);
//        this.authenticationFilterDynamic.addMappingForUrlPatterns(null,false,"/*");
        var ll=333;
        Logger LOGGER = StatusLogger.getLogger();
        LOGGER.debug("Log4jServletContainerInitializer starting up Log4j in Servlet 3.0+ environment.");
        FilterRegistration.Dynamic filter = servletContextEvent.getServletContext().addFilter("log4jServletFilter", Log4jServletFilter.class);
        if (filter == null) {
            LOGGER.warn("WARNING: In a Servlet 3.0+ application, you should not define a log4jServletFilter in web.xml. Log4j 2 normally does this for you automatically. Log4j 2 web auto-initialization has been canceled.");
            return;
        }

//        Log4jWebLifeCycle initializer = WebLoggerContextUtils.getWebLifeCycle(servletContextEvent.getServletContext());
//        initializer.start();
//        initializer.setLoggerContext();
        servletContextEvent.getServletContext().addListener(new Log4jServletContextListener());
        filter.setAsyncSupported(true);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, new String[]{"/*"});
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @Override
    protected ApplicationContextInitializer<?>[] getServletApplicationContextInitializers() {
        return new ApplicationContextInitializer[]{new ApplicationContextInitializerToDelayConfig()};
    }

    class ApplicationContextInitializerToDelayConfig implements ApplicationContextInitializer<AnnotationConfigWebApplicationContext>{

        @Override
        public void initialize(AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext) {
            annotationConfigWebApplicationContext.addApplicationListener(new ApplicationListenerToDelayConfig());
        }
    }

    class ApplicationListenerToDelayConfig implements ApplicationListener<ContextRefreshedEvent> {

        @Override
        public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
            System.setProperty("log4j.configurationFile","log4j2.xml");
            //基于springcontext的事件体系，在ContextRefreshedEvent事件的回调方法中，利用容器读取配置信息然后配置servlet的上传功能，配置filter的初始参数
            var contextRoot=(AnnotationConfigWebApplicationContext)contextRefreshedEvent.getApplicationContext();
            var mapProperties= contextRoot.getBean(AppConfig.class);
            //配置上传功能
            App.this.registration.setMultipartConfig(new MultipartConfigElement(mapProperties.uploadtmpdir,mapProperties.maxUploadSize,mapProperties.maxUploadSize,mapProperties.maxInMemorySize));
            //配置filter
//            App.this.authenticationFilterDynamic.setInitParameter("a1",mapProperties.camundaJdbcDriver);
//            App.this.authenticationFilterDynamic.addMappingForUrlPatterns(null,false,"/*");
        }
    }
}