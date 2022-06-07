package io.github.eeroom.hzoa.controller;

import io.github.eeroom.hzoa.ApidataWrapper;
import io.github.eeroom.hzoa.AppConfig;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@ControllerAdvice
public class AdviceController implements ResponseBodyAdvice<Object> {
    AppConfig appConfig;
    public AdviceController(AppConfig appConfig){
        this.appConfig = appConfig;
    }
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if(!org.springframework.http.converter.json.MappingJackson2HttpMessageConverter.class.isAssignableFrom(aClass))
            return false;
        if(methodParameter.getMethod().getDeclaringClass().getPackageName().indexOf(this.appConfig.controllerPath)<0)
            return false;
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if(o instanceof ApidataWrapper)
            return o;
        var rt= new ApidataWrapper();
        rt.setCode(HttpServletResponse.SC_OK);
        rt.setData(o);
        return rt;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApidataWrapper> handlerError(Throwable ex){
        var lstmsg=new ArrayList<String>();
        this.parseErrorMsg(ex,lstmsg,5);
        var rt=new ApidataWrapper();
        rt.setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        rt.setMessage(String.join("\r\n",lstmsg));
        rt.setTag(ex.toString());
        return new ResponseEntity<>(rt, HttpStatus.OK);
    }

    private void parseErrorMsg(Throwable ex, ArrayList<String> lstmsg,int deep) {
        lstmsg.add(ex.getMessage());
        deep--;
        if(deep<0)
            return;
        var tmp=ex.getCause();
        if(tmp==null)
            return;
        if(tmp==ex)
            return;
        parseErrorMsg(tmp,lstmsg,deep);
    }
}
