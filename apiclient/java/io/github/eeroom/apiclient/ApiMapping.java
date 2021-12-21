package io.github.eeroom.apiclient;

import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMapping {

    public String action();
    public HttpMetod method();
    public MediaType consumes();
    public MediaType Produces();



    public static enum HttpMetod{
        GET,
        POST
    }

    public static enum MediaType{
        Json,
        FormUrlEncoded,
        Xml,
        FormData,
        Text
    }
}
