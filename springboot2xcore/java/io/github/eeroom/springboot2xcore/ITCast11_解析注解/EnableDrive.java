package io.github.eeroom.springboot2xcore.ITCast11_解析注解;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Card
public  @interface  EnableDrive{
    @AliasFor(annotation = Card.class,attribute = "value")
    String value();
}
