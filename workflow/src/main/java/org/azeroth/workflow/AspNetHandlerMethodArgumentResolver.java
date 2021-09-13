package org.azeroth.workflow;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.ServletRequest;

@Component
public class AspNetHandlerMethodArgumentResolver implements org.springframework.web.method.support.HandlerMethodArgumentResolver,org.springframework.context.ApplicationContextAware {
    RequestMappingHandlerAdapter adapter;
    ModelAttributeMethodProcessor pswwwformdata;
    RequestResponseBodyMethodProcessor psjson;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if(this.adapter==null){
            this.adapter=(RequestMappingHandlerAdapter)this.applicationContext.getBean("requestMappingHandlerAdapter");
            this.pswwwformdata= (ModelAttributeMethodProcessor)this.adapter.getArgumentResolvers().stream().filter(x->x instanceof ModelAttributeMethodProcessor).findFirst().get();
            this.psjson= (RequestResponseBodyMethodProcessor)this.adapter.getArgumentResolvers().stream().filter(x->x instanceof RequestResponseBodyMethodProcessor).findFirst().get();
        }
        return  true;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        var sr=(ServletRequest)nativeWebRequest.getNativeRequest(ServletRequest.class);
        var parameterType=sr.getContentType().toLowerCase();
        if(parameterType.indexOf("application/json")>=0)
            return this.psjson.resolveArgument(methodParameter,modelAndViewContainer,nativeWebRequest,webDataBinderFactory);
        else
            return this.pswwwformdata.resolveArgument(methodParameter,modelAndViewContainer,nativeWebRequest,webDataBinderFactory);
    }
    ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
