package org.azeroth.workflow.swagger2;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.collect.Lists;
import net.bytebuddy.description.annotation.AnnotationDescription;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;
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
        if(methodParameter.getContainingClass().getPackageName().indexOf("org.azeroth.workflow.controller")<0)
            return Lists.newArrayList(lst);;
        RequestBody tmp= null;
        try {
            tmp = AnnotationDescription.AnnotationInvocationHandler.of(AspNetResolvedMethodParameter.class.getClassLoader(), RequestBody.class,new HashMap<>());
        } catch (ClassNotFoundException e) {
            System.out.println("AspNetResolvedMethodParameter执行出错");
            e.printStackTrace();
        }
        return Lists.newArrayList(tmp);
    }

    public AspNetResolvedMethodParameter(String paramName, MethodParameter methodParameter, ResolvedType parameterType) {
        this(methodParameter.getParameterIndex(), paramName,getAnnotation(methodParameter),parameterType);
    }

    public AspNetResolvedMethodParameter(int parameterIndex, String defaultName, List<Annotation> annotations, ResolvedType parameterType) {
        super(parameterIndex, defaultName, annotations, parameterType);

    }
}
