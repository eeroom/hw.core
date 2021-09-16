package org.azeroth.workflow;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
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
 */
@Component
public class AspNetHandlerMethodArgumentResolver implements org.springframework.web.method.support.HandlerMethodArgumentResolver,org.springframework.context.ApplicationContextAware {
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
