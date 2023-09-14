package io.github.eeroom.springmvc;

import com.fasterxml.classmate.TypeResolver;
import io.github.eeroom.springmvc.swagger2.AspNetHandlerMethodResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
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
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@org.springframework.context.annotation.Configuration
@EnableSwagger2
public class WebConfig extends org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport {
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

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
        var aspNetRequestMappingHandler= new io.github.eeroom.springmvc.aspnet.RequestMappingHandler();
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
    public io.github.eeroom.springmvc.aspnet.HandlerMethodArgumentResolver aspNetHandlerMethodArgumentResolver(){
        return new io.github.eeroom.springmvc.aspnet.HandlerMethodArgumentResolver();
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //支持asp.net风格的参数模型绑定；根据请求的context-type,自定使用json方式或者表单方式进行模型绑定，不需要额外对参数设置特性）
        argumentResolvers.add(this.getApplicationContext().getBean(io.github.eeroom.springmvc.aspnet.HandlerMethodArgumentResolver.class));
    }

    @Bean
    public org.thymeleaf.templateresolver.ITemplateResolver templateResolver(){
        var templateResolver=new org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver();
        templateResolver.setPrefix("/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return templateResolver;
    }

    @Bean
    public org.thymeleaf.spring4.SpringTemplateEngine templateEngine(org.thymeleaf.templateresolver.ITemplateResolver templateResolver){
        var engine=new org.thymeleaf.spring4.SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);
        //支持layout
        var layout=new nz.net.ultraq.thymeleaf.LayoutDialect();
        engine.addDialect(layout);
        return engine;
    }

    @Bean
    public org.springframework.web.servlet.ViewResolver viewResolver(org.thymeleaf.spring4.SpringTemplateEngine engine){
      var resolver=new org.thymeleaf.spring4.view.ThymeleafViewResolver();
      resolver.setTemplateEngine(engine);
      resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());

        return  resolver;
    }

    @Bean
    public springfox.documentation.spring.web.plugins.Docket createRestApi(AppConfig appconfig) {
        //启用swagger,添加@EnableSwagger2,添加这个bean
        // springfox-swagger-ui这类类库提供了swagger-ui的入口页面，swagger-ui.html,
        // io.springfox->springfox-swagger2,EnableSwagger2注解会注册其涉及的bean,核心是注册了一个HandlerMapping,HandlerMapping的核心是Swagger2Controller，提供的核心api,/v2/api-docs
        var apiInfo= new ApiInfoBuilder()
                .title(appconfig.swagger2title)
                .description(appconfig.swagger2description)
                .termsOfServiceUrl(appconfig.swagger2termsOfServiceUrl)
                .version("1.0")
                .build();
        var docket= new springfox.documentation.spring.web.plugins.Docket(DocumentationType.SWAGGER_2)
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
        var scontext= SecurityContext.builder()
                .securityReferences(Stream.of(srf).collect(Collectors.toList()))
                .forPaths(PathSelectors.any())
                .build();
        docket.securityContexts(Stream.of(scontext).collect(Collectors.toList()))
                .securitySchemes(Stream.of(new ApiKey(appconfig.authenJwtHeader,appconfig.authenJwtHeader,"header")).collect(Collectors.toList()));
        return docket;
    }

    @Bean
    @Primary
    public springfox.documentation.spring.web.readers.operation.HandlerMethodResolver methodResolver(TypeResolver resolver) {
        //SpringfoxWebMvcConfiguration中定义了这个bean,这里利用@Primary替换掉swagger2的默认bean
        //目的：aspnet风格的api,方法参数不添加特性，swaager也可以读取到这个参数信息
        //注入点的位置：springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider的构造函数，
        return new AspNetHandlerMethodResolver(resolver);
    }

}
