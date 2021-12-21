package org.azeroth.okhttp3client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/*
HTTP请求的代理客户端
 */
public class HttpChannel<T> implements InvocationHandler {
    Class<T> meta;
    String baseAddress;
    public  HttpChannel(Class<T> meta,String baseAddress){
        this.meta=meta;
        this.baseAddress=baseAddress;
    }

    public  T client(){
        T client= (T)java.lang.reflect.Proxy.newProxyInstance(this.meta.getClassLoader(),new Class[]{this.meta},this);
        return client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
