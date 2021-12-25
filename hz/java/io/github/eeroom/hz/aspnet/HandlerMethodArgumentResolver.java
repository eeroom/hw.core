package io.github.eeroom.hz.aspnet;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.ServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * 解析前提，
 * 控制器方法没有RequestMapping及其派生的注解，可以有httppost,httpget
 * 请求方法必须为post
 * context必须为json或者application/x-www-form-urlencoded
 * 2021年10月11日更新：移除@Component注释。因为AspNetHandlerMethodArgumentResolver依赖requestMappingHandlerAdapter这个bean,
 * 但是这个bean是定义在WebMvcConfigurationSupport中的，也就是注册在webconfig的容器中
 * 又因为webconfig的容器不扫包，而是rootconfig的容器扫包，所以AspNetHandlerMethodArgumentResolver必须定义在webconfig中，
 * 如果让rootconfig的容器扫进去，那么后续获取requestMappingHandlerAdapter这个bean的时候就会获取不到
 * 如果是让webconfig的容器扫这个bean,就可以直接添加@Component注解，不用在webconfig利用@bean来定义这个bean
 */
//@Component
public class HandlerMethodArgumentResolver implements org.springframework.web.method.support.HandlerMethodArgumentResolver,org.springframework.context.ApplicationContextAware {
    RequestMappingHandlerAdapter adapter;
    ModelAttributeMethodProcessor parsewwwformdata;
    RequestResponseBodyMethodProcessor parsejson;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        var rm= methodParameter.getMethodAnnotation(RequestMapping.class);
        if(rm!=null)
            return false;
        var httpContext= (Map<String,Object>)this.applicationContext.getBean("httpContext");
        var method= (String)httpContext.get("Method");
        if(!"post".equalsIgnoreCase(method))
            return false;
        var contentType= Optional.ofNullable(httpContext.get("ContentType")).orElse("").toString().toLowerCase();
        if(contentType.indexOf(MediaType.APPLICATION_JSON_VALUE)<0 && contentType.indexOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE)<0 &&contentType.indexOf(MediaType.MULTIPART_FORM_DATA_VALUE)<0)
            return false;
        if(this.adapter==null){
            this.adapter=(RequestMappingHandlerAdapter)this.applicationContext.getBean("requestMappingHandlerAdapter");
            this.parsewwwformdata= (ModelAttributeMethodProcessor)this.adapter.getArgumentResolvers().stream().filter(x->x instanceof ModelAttributeMethodProcessor).findFirst().get();
            this.parsejson= (RequestResponseBodyMethodProcessor)this.adapter.getArgumentResolvers().stream().filter(x->x instanceof RequestResponseBodyMethodProcessor).findFirst().get();
        }
        return  true;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        var sr=(ServletRequest)nativeWebRequest.getNativeRequest(ServletRequest.class);
        var parameterType=sr.getContentType().toLowerCase();
        if(parameterType.indexOf(MediaType.APPLICATION_JSON_VALUE)>=0)
            return this.parsejson.resolveArgument(methodParameter,modelAndViewContainer,nativeWebRequest,webDataBinderFactory);
        else
            return this.parsewwwformdata.resolveArgument(methodParameter,modelAndViewContainer,nativeWebRequest,webDataBinderFactory);
    }
    ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
