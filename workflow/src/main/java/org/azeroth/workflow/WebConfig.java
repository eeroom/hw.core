package org.azeroth.workflow;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import org.azeroth.workflow.swagger2.AspNetHandlerMethodResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.context.annotation.Configuration
@org.springframework.context.annotation.ComponentScan(excludeFilters ={@ComponentScan.Filter(classes = RootConfig.class,type = FilterType.ASSIGNABLE_TYPE  )})
@EnableSwagger2
public class WebConfig extends org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport {
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
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher antPathMatcher=new AntPathMatcher();
        antPathMatcher.setCaseSensitive(false);
        configurer.setPathMatcher(antPathMatcher);
    }
//    @Bean(name = "multipartResolver")
//    public MultipartResolver getStandardServletMultipartResolver(){
//        return new StandardServletMultipartResolver();
//    }
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new AspNetRequestMappingHandler();
    }

    /**
     * MydispatherServlet中把一些需要用到的信息写到字典，方便后续使用
     * 例如：AspNetHandlerMethodArgumentResolver需要根据当前请求的POST,GET，content-type等信息决定是否解析当前请求的参数
     * @return
     */
    @Bean
    @Scope(WebApplicationContext.SCOPE_REQUEST)
    public Map<String,Object> httpContext(){
        var mp=new HashMap<String,Object>();
        return mp;
    }

    @Autowired
    AspNetHandlerMethodArgumentResolver aspNetHandlerMethodArgumentResolver;

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //支持asp.net风格的参数模型绑定；根据请求的context-type,自定使用json方式或者表单方式进行模型绑定，不需要额外对参数设置特性）
        argumentResolvers.add(aspNetHandlerMethodArgumentResolver);
    }

    @Bean
    public Docket createRestApi() {
        //启用swagger,添加@EnableSwagger2,添加这个bean
        // springfox-swagger-ui这类类库提供了swagger-ui的入口页面，swagger-ui.html,
        // io.springfox->springfox-swagger2,EnableSwagger2注解会注册其涉及的bean,核心是注册了一个HandlerMapping,HandlerMapping的核心是Swagger2Controller，提供的核心api,/v2/api-docs
        var apiInfo= new ApiInfoBuilder()
                .title("azeroth-api")
                .description("swagger-bootstrap-ui")
                .termsOfServiceUrl("http://localhost:8084/")
                .version("1.0")
                .build();

        var docket= new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.azeroth.workflow.controller"))
                .paths(PathSelectors.any())
                .build();
        //配置各个方法的请求头设置（非全局），Access-Token是请求头名称
        String authenHeaderName="Access-Token";
        var ticketPar=new ParameterBuilder();
        var tokenParameter =ticketPar.name(authenHeaderName)//请求头名称
                .description("jwt")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        var lstp=new ArrayList<Parameter>();
        lstp.add(tokenParameter);
        //每个方法的参数列表都会多这个参数
        //docket.globalOperationParameters(lstp);

        //配置各个方法的请求头设置（全局），Access-Token是请求头名称
        var lstAuthenticationScop=new AuthorizationScope[1];

        lstAuthenticationScop[0]=new AuthorizationScope("global","全局jwt");
        var sr=new SecurityReference(authenHeaderName,lstAuthenticationScop);
        var sr2=new SecurityReference("Authorization",lstAuthenticationScop);
        var lstSr= Lists.newArrayList(sr,sr2);
        var sc= SecurityContext.builder()
                .securityReferences(lstSr)
                .forPaths(PathSelectors.any())
                .build();
        var apiKey=new ApiKey(authenHeaderName,authenHeaderName,"header");
        var apiKey2=new ApiKey("Authorization","Authorization","header");
        docket.securityContexts(Lists.newArrayList(sc))
                .securitySchemes(Lists.newArrayList(apiKey,apiKey2));

        return docket;
    }


    @Bean
    @Primary
    public HandlerMethodResolver methodResolver(TypeResolver resolver) {
        //SpringfoxWebMvcConfiguration中定义了这个bean,这里利用@Primary替换掉swagger2的默认bean
        //目的：aspnet风格的api,方法参数不添加特性，swaager也可以读取到这个参数信息
        //注入点的位置：springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider的构造函数，
        return new AspNetHandlerMethodResolver(resolver);
    }

    @Value("${upload.tmpdir}")
    String uploadtmpdir;
    @Value("${upload.maxUploadSize}")
    int maxUploadSize;
    @Value("${upload.maxInMemorySize}")
    int maxInMemorySize;


    //需要依赖额外的包，这里不采用
    public MultipartResolver multipartResolver(){
        var mr=new CommonsMultipartResolver();
        mr.setMaxInMemorySize(this.maxInMemorySize);
        mr.setMaxUploadSize(this.maxUploadSize);
        return mr;
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationHandlerInterceptor());
    }
}
