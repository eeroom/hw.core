package io.github.eeroom.springboot2xcore;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.*;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

public class ITCast11Anotation {

    public static void main(String[] args){

        /**
         * jdk提供的元注解  @Retention(RetentionPolicy.RUNTIME) @Inherited 等
         * A注解标注在B注解上，则认为A是B的元注解
         * 不允许A注解继承B注解，，但是所有自定义注解编译后都会被定义为继承自java.lang.annotation.Annotation接口的calss
         * 注解可以有元注解，元注解可以有元注解，效果上和类的继承类似
         * jdk 反射获取注解信息的策略：
         *      获取直接标注的注解 以及标注在父类且具有@Inherited元注解的注解！
         *      忽略父接口上标注的注解，
         *      忽略直接标注的注解的元注解
         * spring对jdk的处理策略
         *      定义MergedAnnotations.SearchStrategy枚举用于指定搜索范围
         *      javaconfig模式下，基于 元注解的继承效果，定义了大量配置类相关的注解 @SpringBootApplication
         *      定义了 @AliasFor 注解，实现元注解继承效果下的 参数传递，非常重要！
         * 结论：优先使用AnnotatedElementUtils，特点：不能指定搜索范围， 只能取搜索到的第一个符合条件的注解或者全部
         *      MergedAnnotations  特点：可以指定搜索范围，
         */
        System.out.println("--jdk--Person.class.getAnnotations()---");
        for (Annotation annotation : Person.class.getAnnotations()) {
            System.out.println(annotation);
        }
        System.out.println("--AnnotationUtils.getAnnotation()--------------------------------------------");
        System.out.println("直接标注在类或接口上:"
                +AnnotationUtils.getAnnotation(Person.class, EnableDrive.class));
        System.out.println("标注在父类，有@Inherited 元注解，可以取到:"
                +AnnotationUtils.getAnnotation(Person.class, TestInherited.class));
        System.out.println("直接注解的元注解，可以取到，但是获取不到传递的参数值:"
                +AnnotationUtils.getAnnotation(Person.class, Card.class));
        System.out.println("标注在父类，没有@Inherited 元注解，取不到:"
                +AnnotationUtils.getAnnotation(Person.class, Scope.class));
        System.out.println("标注在父接口，取不到:"
                +AnnotationUtils.getAnnotation(Person.class, TestInheritedForSuperInterface.class));

        System.out.println("AnnotationUtils.findAnnotation()--------------------------------------------");
        System.out.println("标注在父类，没有@Inherited 元注解，可以取到:"
                +AnnotationUtils.findAnnotation(Person.class, Scope.class));
        System.out.println("标注在父接口，可以取到:"
                +AnnotationUtils.findAnnotation(Person.class, TestInheritedForSuperInterface.class));

        System.out.println("--AnnotatedElementUtils.getAllMergedAnnotations()--------------------------------------------");
        for (Component annotation : AnnotatedElementUtils.getAllMergedAnnotations(Person.class, Component.class)) {
            System.out.println(annotation);
        }
        System.out.println("--AnnotatedElementUtils.findAllMergedAnnotations()--------------------------------------------");
        for (Component annotation : AnnotatedElementUtils.findAllMergedAnnotations(Person.class, Component.class)) {
            System.out.println(annotation);
        }
        System.out.println("直接注解的元注解，可以取到，并且能够获取到传递的参数值:"
                +AnnotatedElementUtils.findMergedAnnotation(Person.class, Card.class));
        System.out.println("--AnnotationMetadata.introspect()-内部就是MergedAnnotations.from()---MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS-------------------------------------------");
        AnnotationMetadata.introspect(Person.class).getAnnotations().stream().forEach(x->{
            System.out.println(x.getType()+":"+tryInvoke(()->x.getString("value"),""));
        });
        System.out.println("--MergedAnnotations.from()---MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS---------------------");
        MergedAnnotations.from(Person.class, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS).stream().forEach(x->{
            System.out.println(x.getType()+":"+tryInvoke(()->x.getString("value"),""));
        });
        System.out.println("--MergedAnnotations.from()---MergedAnnotations.SearchStrategy.TYPE_HIERARCHY---------------------");
        MergedAnnotations.from(Person.class, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).stream().forEach(x->{
            System.out.println(x.getType()+":"+tryInvoke(()->x.getString("value"),""));
        });
    }

    static <T> T tryInvoke(MyAction<T> action,T msg){
        try {
            return action.invoke();
        }catch (Throwable throwable){
            if(msg!=null)
                return msg;
            throw new RuntimeException(throwable);
        }
    }

    @FunctionalInterface
    static interface MyAction<T>{
        T invoke() throws Throwable;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Component("元注解标注")
    static  @interface  Card{
        String value() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Card
    static @interface  EnableDrive{
        @AliasFor(annotation = Card.class,attribute = "value")
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    static @interface TestInherited{
        String value() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    static @interface TestInheritedForSuperInterface{
        String value() default "";
    }

    @Component("标注在父类")
    @TestInherited("标注在父类且具有@Inherited元注解")
    @Scope(value = "标注在父类")
    static class Animal{

    }


    @TestInheritedForSuperInterface("标注在父接口且具有@Inherited元注解")
    @Component("标注在父接口")
    static interface IPerson {

    }

    @Component("直接标注")
    @EnableDrive("传参给Card")
    static class Person extends Animal implements IPerson{

    }
}
