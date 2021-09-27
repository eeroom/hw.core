package org.azeroth.workflow.controller;

import org.azeroth.workflow.ApiResultWrapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class AdviceController implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if(!org.springframework.http.converter.json.MappingJackson2HttpMessageConverter.class.isAssignableFrom(aClass))
            return false;
        if(methodParameter.getMethod().getDeclaringClass().getPackageName().indexOf("workflow.controller")<0)
            return false;
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if(o instanceof ApiResultWrapper)
            return o;
        if(o instanceof ResponseEntity<?>)
            return o;
        var rt= new ApiResultWrapper();
        rt.setCode(HttpServletResponse.SC_OK);
        rt.setData(o);
        return rt;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResultWrapper> handlerError(Throwable ex){
        var rt=new ApiResultWrapper();
        rt.setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        rt.setMessage(ex.getMessage());
        rt.setTag(ex.toString());
        return new ResponseEntity<>(rt, HttpStatus.OK);
    }
}
