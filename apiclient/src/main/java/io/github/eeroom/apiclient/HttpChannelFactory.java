package io.github.eeroom.apiclient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HttpChannelFactory<T> {
    String baseurl;
    public T createChannel( String baseurl,Class<T> ...meta){
       var proxyEntity= (T)java.lang.reflect.Proxy.newProxyInstance(HttpChannelFactory.class.getClassLoader(),meta,new MyInvocationHandler());
       this.baseurl=baseurl;
       return proxyEntity;
    }

    class  MyInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

            return null;
        }
    }
}
