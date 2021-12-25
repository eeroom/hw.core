package io.github.eeroom.hz;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import io.github.eeroom.hz.aspnet.RequestMappingHandler;
import io.github.eeroom.hz.authen.JwtAuthenticationInterceptor;
import io.github.eeroom.hz.swagger2.AspNetHandlerMethodResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 这个容器只扫描控制器的bean,
 * 如果不扫，全部交给rootconfig的容器去扫，swagger不能正常工作
 * 2021年10月11日更新：简化扫包，WebConfig不扫包，通过aspNetRequestMappingHandler.setDetectHandlerMethodsInAncestorContexts(true)，作用是查找controller的时候检测父容器，
 * 这样api也能正常，之前不扫包swagger不能正常工作的原因就是（控制器被父容器扫描，然后aspNetRequestMappingHandler的容器是webconfig,查找控制器的时候没有去检测父容器）
 */
@org.springframework.context.annotation.Configuration
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
        var aspNetRequestMappingHandler= new RequestMappingHandler();
        //这个设置为true,查找所有控制器的时候会检测父容器
        aspNetRequestMappingHandler.setDetectHandlerMethodsInAncestorContexts(true);
        return aspNetRequestMappingHandler;
    }

    /**
     * 因为AspNetHandlerMethodArgumentResolver依赖requestMappingHandlerAdapter这个bean,
     * 但是这个bean是定义在WebMvcConfigurationSupport中的，也就是注册在webconfig的容器中
     * 又因为webconfig的容器不扫包，而是rootconfig的容器扫包，所以AspNetHandlerMethodArgumentResolver必须定义在webconfig中，
     * 如果让rootconfig的容器扫进去，那么后续获取requestMappingHandlerAdapter这个bean的时候就会获取不到
     * @return
     */
    @Bean
    public io.github.eeroom.hz.aspnet.HandlerMethodArgumentResolver aspNetHandlerMethodArgumentResolver(){
        return new io.github.eeroom.hz.aspnet.HandlerMethodArgumentResolver();
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //支持asp.net风格的参数模型绑定；根据请求的context-type,自定使用json方式或者表单方式进行模型绑定，不需要额外对参数设置特性）
        argumentResolvers.add(this.getApplicationContext().getBean(HandlerMethodArgumentResolver.class));
    }

    @Bean
    public Docket createRestApi(MapProperties appconfig) {
        //启用swagger,添加@EnableSwagger2,添加这个bean
        // springfox-swagger-ui这类类库提供了swagger-ui的入口页面，swagger-ui.html,
        // io.springfox->springfox-swagger2,EnableSwagger2注解会注册其涉及的bean,核心是注册了一个HandlerMapping,HandlerMapping的核心是Swagger2Controller，提供的核心api,/v2/api-docs
        var apiInfo= new ApiInfoBuilder()
                .title(appconfig.swagger2title)
                .description(appconfig.swagger2description)
                .termsOfServiceUrl(appconfig.swagger2termsOfServiceUrl)
                .version("1.0")
                .build();
        var docket= new Docket(DocumentationType.SWAGGER_2)
                .enable(appconfig.swagger2enable)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(appconfig.controllerPath))
                .paths(PathSelectors.any())
                .build();
        //jwt非全局配置,各个方法的请求头设置
        var ticketPar=new ParameterBuilder();
        var tokenParameter =ticketPar.name(appconfig.authenJwtHeader)//请求头名称
                .description("jwt")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("defaultValue")
                .required(false)
                .build();
        var lstp=new ArrayList<Parameter>();
        lstp.add(tokenParameter);
        //docket.globalOperationParameters(lstp);

        //jwt全局配置
        var srf= SecurityReference.builder()
                .reference(appconfig.authenJwtHeader)
                .scopes(Stream.of(new AuthorizationScope("global","全局jwt")).toArray(AuthorizationScope[]::new))
                .build();
        var scontext=SecurityContext.builder()
                .securityReferences(Stream.of(srf).collect(Collectors.toList()))
                .forPaths(PathSelectors.any())
                .build();
        docket.securityContexts(Stream.of(scontext).collect(Collectors.toList()))
                .securitySchemes(Stream.of(new ApiKey(appconfig.authenJwtHeader,appconfig.authenJwtHeader,"header")).collect(Collectors.toList()));
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

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthenticationInterceptor());
    }
}
