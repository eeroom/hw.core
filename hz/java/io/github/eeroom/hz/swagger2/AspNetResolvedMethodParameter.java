package io.github.eeroom.hz.swagger2;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.collect.Lists;
import io.github.eeroom.hz.MapProperties;
import io.github.eeroom.hz.MyObjectFacotry;
import io.github.eeroom.hz.aspnet.HttpGet;
import net.bytebuddy.description.annotation.AnnotationDescription;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.service.ResolvedMethodParameter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;

public class AspNetResolvedMethodParameter extends ResolvedMethodParameter {

    //swagger2的api元数据处理过程中，如果参数没有使用注解修饰，就增加RequestBody注解，这样swagger2返回的api信息中才有参数信息
    static List<Annotation> getAnnotation(MethodParameter methodParameter){
        var lst=methodParameter.getParameterAnnotations();
        if(lst.length>0)
            return Lists.newArrayList(lst);
        RequestMapping rm= methodParameter.getMethodAnnotation(RequestMapping.class);
        if(rm!=null)
            return Lists.newArrayList(lst);
        var hg=methodParameter.getMethodAnnotation(HttpGet.class);
        if(hg!=null)
            return Lists.newArrayList(lst);
        if(methodParameter.getMethod().getParameters().length!=1)
            return Lists.newArrayList(lst);
        var appconfig= MyObjectFacotry.getBean(MapProperties.class);
        if(methodParameter.getContainingClass().getPackageName().indexOf(appconfig.controllerPath)<0)
            return Lists.newArrayList(lst);;
        try {
            var tmp = AnnotationDescription.AnnotationInvocationHandler.of(AspNetResolvedMethodParameter.class.getClassLoader(), RequestBody.class,new HashMap<>());
            return Lists.newArrayList(tmp);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("AspNetResolvedMethodParameter执行出错:%s%s",
                    methodParameter.getMethod().getDeclaringClass().getName(),
                    methodParameter.getMethod().getName()));
        }
    }

    public AspNetResolvedMethodParameter(String paramName, MethodParameter methodParameter, ResolvedType parameterType) {
        this(methodParameter.getParameterIndex(), paramName,getAnnotation(methodParameter),parameterType);
    }

    public AspNetResolvedMethodParameter(int parameterIndex, String defaultName, List<Annotation> annotations, ResolvedType parameterType) {
        super(parameterIndex, defaultName, annotations, parameterType);

    }
}
