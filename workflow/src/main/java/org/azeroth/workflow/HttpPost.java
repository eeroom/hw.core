package org.azeroth.workflow;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpPost {
    public static String contentJson="application/json";
    public static String contentForm="application/x-www-form-urlencoded";
}
