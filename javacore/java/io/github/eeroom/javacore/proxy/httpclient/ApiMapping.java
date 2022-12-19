package io.github.eeroom.javacore.proxy.httpclient;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMapping {
    public static String NullAction="__";
    public static ApiMapping Default=new ApiMapping(){
        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }

        @Override
        public String action() {
            return NullAction;
        }

        @Override
        public HttpMetod method() {
            return HttpMetod.POST;
        }

        @Override
        public MediaType consumes() {
            return MediaType.Json;
        }

        @Override
        public MediaType produces() {
            return MediaType.Json;
        }

        @Override
        public Class<?> wrapperType() {
            return void.class;
        }
    };
    public String action() default "__";
    public HttpMetod method() default HttpMetod.POST;
    public MediaType consumes() default MediaType.Json;
    public MediaType produces() default MediaType.Json;
    public Class<?> wrapperType() default void.class;

    public static enum HttpMetod{
        GET,
        POST
    }

    public static enum MediaType{
        Json("application/json"),
        FormUrlEncoded("application/x-www-form-urlencoded"),
        Xml("application/xml"),
        FormData(""),
        Text("text/plain");
        MediaType(String contentType){
            this.contentType=contentType;
        }
        String contentType;
        public String getContentType(){
            return  this.contentType;
        }
    }
}
