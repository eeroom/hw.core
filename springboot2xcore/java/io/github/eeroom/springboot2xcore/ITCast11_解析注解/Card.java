package io.github.eeroom.springboot2xcore.ITCast11_解析注解;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Component("元注解标注")
public   @interface  Card{
    String value() default "";
}
