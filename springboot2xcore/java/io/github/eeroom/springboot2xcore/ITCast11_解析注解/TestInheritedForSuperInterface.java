package io.github.eeroom.springboot2xcore.ITCast11_解析注解;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public  @interface TestInheritedForSuperInterface{
    String value() default "";
}
