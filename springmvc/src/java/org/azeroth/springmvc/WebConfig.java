package org.azeroth.springmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

@org.springframework.context.annotation.Configuration
@org.springframework.web.servlet.config.annotation.EnableWebMvc
@org.springframework.context.annotation.ComponentScan
public class WebConfig extends org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter {
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

//    @Bean
//    public org.springframework.web.servlet.HandlerMapping handlerMapping(){
//        //不要删除
//        //这个bean直接使用特性标记，这样可以让ioc容器创建后就注册这个，如果在这个类的bean，需要等后期才会注册上，导致一些问题，比如swagger从ioc取不到这个bean，然后asp.net webapi风格的方法就取不到
//        var handler=new RequestMappingHandlerMappingAspNet();
//        var pathMatcher= (AntPathMatcher)handler.getPathMatcher();
//        //忽略请求路径的url的大小写，默认是区分大小写，w3标准是url和请求参数的名称不区分大小写
//        pathMatcher.setCaseSensitive(false);
//        //org.springframework.web.servlet.DispatcherServlet在initHandlerMappings中会对所有的handlerMapping排序，
//        //这里保证RequestMappingHandlerMappingAspNet排第一个，
//        //因为RequestMappingHandlerMapping加了Component,会被初始化到dispather，并且是第一个
//        handler.setOrder(-1);
//        return handler;
//    }

    /**
     * 忽略请求路径的url的大小写，默认是区分大小写，w3标准是url和请求参数的名称不区分大小写，
     * 对其他的默认加载的handlerMapping起作用，对RequestMappingHandlerMappingAspNet不起作用
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher antPathMatcher=new AntPathMatcher();
        antPathMatcher.setCaseSensitive(false);
        configurer.setPathMatcher(antPathMatcher);
    }
    @Bean
    public org.thymeleaf.templateresolver.ITemplateResolver templateResolver(){
        var templateResolver=new org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver();
        templateResolver.setPrefix("/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }

    @Bean
    public org.thymeleaf.ITemplateEngine templateEngine(org.thymeleaf.templateresolver.ITemplateResolver templateResolver){
        var engine=new org.thymeleaf.spring4.SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);
        //支持layout
        var layout=new nz.net.ultraq.thymeleaf.LayoutDialect();
        engine.addDialect(layout);
        return engine;
    }

    @Bean
    public org.springframework.web.servlet.ViewResolver viewResolver(org.thymeleaf.ITemplateEngine engine){
      var resolver=new org.thymeleaf.spring4.view.ThymeleafViewResolver();
      resolver.setTemplateEngine(engine);
      resolver.setCharacterEncoding("UTF-8");

        return  resolver;
    }

//    @Bean(name = "multipartResolver")
//    public MultipartResolver getStandardServletMultipartResolver(){
//        return new StandardServletMultipartResolver();
//    }


}
