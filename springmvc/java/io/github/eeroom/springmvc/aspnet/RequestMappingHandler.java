package io.github.eeroom.springmvc.aspnet;


import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class RequestMappingHandler extends org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping {

    public RequestMappingHandler(){
        //org.springframework.web.servlet.DispatcherServlet在initHandlerMappings中会对所有的handlerMapping排序，
        //这里保证RequestMappingHandlerMappingAspNet排第一个，
        //因为RequestMappingHandlerMapping加了Component,会被初始化到dispather，并且是第一个
        this.setOrder(-1);
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        var rmi=super.getMappingForMethod(method,handlerType);
        if(rmi!=null)
            return rmi;
        try {
            return this.getMappingForMethodAspNet(method,handlerType);
        }catch (Throwable ex){
            throw new RuntimeException(String.format("getMappingForMethodAspNet发生异常:%s,%s",handlerType.getName(),method.getName()));
        }
    }

    /**
     * 基于约定：按照controller/action的请求路径监听，
     * 优点：避免写一些重复和意义不大的RequestMapping("/home/student"),借鉴asp.net mvc的做法
     * 前提：控制器添加Controller或者RestController特性，方法没有添加RequestMapping特性，方法为public并且为实例方法
     * @param method
     * @param handlerType
     * @return
     * @throws NoSuchFieldException
     */
    private RequestMappingInfo getMappingForMethodAspNet(Method method, Class<?> handlerType) throws Throwable {
        if(handlerType.getAnnotation(org.springframework.stereotype.Controller.class)==null
                && handlerType.getAnnotation(org.springframework.web.bind.annotation.RestController.class)==null)
            return null;
        int flag=method.getModifiers();
        if(!java.lang.reflect.Modifier.isPublic(flag) || java.lang.reflect.Modifier.isStatic(flag))
            return null;
        var config=this.getConfigInfo();
        var lstSegment= handlerType.getName().split("\\.");
        String controllerNameFull=lstSegment[lstSegment.length-1];
        String controllerName= controllerNameFull.toLowerCase().replace("controller","");

        //设定api默认的http方法
        var httpGet = AnnotatedElementUtils.findMergedAnnotation(method, HttpGet.class);
        var httpPost = AnnotatedElementUtils.findMergedAnnotation(method, HttpPost.class);
        var lstm=new ArrayList<RequestMethod>();
        var lstc=new ArrayList<String>();

        if(httpGet!=null)
            lstm.add(RequestMethod.GET);
        if(httpPost!=null){
            lstm.add(RequestMethod.POST);
            lstc.add(MediaType.APPLICATION_JSON_VALUE);
            lstc.add(MediaType.MULTIPART_FORM_DATA_VALUE);
            lstc.add(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            lstc.add(MediaType.ALL_VALUE);
        }
        if(lstm.size()==0){
            lstm.add(RequestMethod.POST);
            lstc.add(MediaType.APPLICATION_JSON_VALUE);
            lstc.add(MediaType.MULTIPART_FORM_DATA_VALUE);
            lstc.add(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            lstc.add(MediaType.ALL_VALUE);
        }
        RequestMethod[] lstMethod=new RequestMethod[lstm.size()];
        lstm.toArray(lstMethod);
        String[] lstConsume=new String[lstc.size()];
        lstc.toArray(lstConsume);
        var rmi= RequestMappingInfo.paths(this.resolveEmbeddedValuesInPatterns(new String[]{"/"+controllerName+"/"+method.getName()}))
                .methods(lstMethod)//这里决定api的请求方法，GET,POST,PUT等
                .params(new String[0])
                .headers(new String[0])
                .consumes(lstConsume)
                .produces(new String[0])
                .mappingName("")
                .customCondition(null)
                .options(config)
                .build();
        return rmi;
    }

    org.springframework.web.servlet.mvc.method.RequestMappingInfo.BuilderConfiguration configValue;
    private org.springframework.web.servlet.mvc.method.RequestMappingInfo.BuilderConfiguration getConfigInfo()  throws  Throwable{
        if(this.configValue!=null)
            return this.configValue;
        var configField= org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping.class.getDeclaredField("config");
        configField.setAccessible(true);
        this.configValue= (org.springframework.web.servlet.mvc.method.RequestMappingInfo.BuilderConfiguration)configField.get(this);
        return this.configValue;
    }
}
