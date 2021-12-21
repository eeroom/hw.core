package io.github.eeroom.apiclient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HttpChannelFactory {
    String baseurl;
    MyInvocationHandler myInvocationHandler;
    Class<?> meta;
    HttpChannelFactory( String baseurl,Class<?> meta){
        this.baseurl=baseurl;
         this.myInvocationHandler= new MyInvocationHandler();
         this.meta=meta;
    }
    public static <T> T  createChannel( String baseurl,Class<T> ...meta){
        var factory=new HttpChannelFactory(baseurl,meta[0]);
       var proxyEntity= (T)java.lang.reflect.Proxy.newProxyInstance(HttpChannelFactory.class.getClassLoader(),meta,factory.myInvocationHandler);
       return proxyEntity;
    }

    class  MyInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

            return null;
        }
    }
}
