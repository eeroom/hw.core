package io.github.eeroom.javacore.代理和声明式远程调用.httpclient;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.eeroom.javacore.序列化.ByJackson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    interface Supplier<T>{
        T get() throws Throwable;
    }

    class  MyInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            var apimapping=method.getAnnotation(ApiMapping.class);
            if(apimapping==null)
                apimapping=ApiMapping.Default;
            var url=this.parseUrl(method,apimapping);
            var payload=this.parsePayload(objects,apimapping);
            if(apimapping.method().equals(ApiMapping.HttpMetod.GET)){
                url=String.format("%s?%s",url,new String(payload,StandardCharsets.UTF_8));
            }
            var httpcnn=(HttpURLConnection)new URL(url).openConnection();
            httpcnn.setRequestMethod(apimapping.method().name());
            if(apimapping.method().equals(ApiMapping.HttpMetod.POST)){
                httpcnn.setDoOutput(true);
                httpcnn.setRequestProperty("Content-Type",apimapping.consumes().getContentType());
                httpcnn.getOutputStream().write(payload,0,payload.length);
            }
            var sb=new StringBuilder();
            int bufferSize=8192,datalength=0;
            var buffer=new char[bufferSize];
            Supplier<InputStream> getstream=httpcnn.getResponseCode()==200?httpcnn::getInputStream:httpcnn::getErrorStream;
            try (var reader=new InputStreamReader(getstream.get(),StandardCharsets.UTF_8)){
                while ((datalength=reader.read(buffer,0,bufferSize))>0){
                    sb.append(buffer,0,datalength);
                }
            }
            if(httpcnn.getResponseCode()!=200)
                throw new RuntimeException(String.format("api服务端异常，httpcode:%d,msg:%s",httpcnn.getResponseCode(),sb.toString()));
            var value=this.parseResponse(sb.toString(),method,apimapping);
            return value;

        }

        private Object parseResponse(String resvalue, Method method, ApiMapping apimapping) {
            switch (apimapping.produces()){
                case Json:
                    if(method.getReturnType().equals(void.class))
                        return null;
                    return ByJackson.deSerializeObject(resvalue, new TypeReference<List<Object>>() {
                        @Override
                        public Type getType() {
                            return method.getGenericReturnType();
                        }
                    });
                default:
                    if(method.getReturnType().equals(String.class))
                        return resvalue;
                    throw new RuntimeException("parseResponse代码还未完成");
            }
        }


        private byte[] parsePayload(Object[] objects, ApiMapping apimapping) {
            if(objects.length<1)
                return "".getBytes(StandardCharsets.UTF_8);
            switch (apimapping.consumes()){
                case Json:
                    return ByJackson.serializeObject(objects[0]).getBytes(StandardCharsets.UTF_8);
                default:
                    throw new RuntimeException("parsePayload代码还未完成");
            }
        }

        private String parseUrl(Method method, ApiMapping apimapping) {
            String flag="/";
            if(apimapping.action()==null || apimapping.action().equals(ApiMapping.NullAction)){
                var lstsegment=new ArrayList<String>();
                lstsegment.add(HttpChannelFactory.this.baseurl);
                //接口和mvc控制器的额外处理
                var cName=HttpChannelFactory.this.meta.getSimpleName().toLowerCase();
                if(cName.startsWith("i"))
                    cName=cName.substring(1,cName.length());
                if(cName.endsWith("controller"))
                    cName=cName.substring(0,cName.length()-"controller".length());
                lstsegment.add(cName);
                lstsegment.add(method.getName());
                var lst2= lstsegment.stream().map(x->this.trim(x,flag)).collect(Collectors.toList());
                return String.join(flag,lst2);
            }else {
                var lstsegment=new ArrayList<String>();
                lstsegment.add(HttpChannelFactory.this.baseurl);
                lstsegment.add(apimapping.action());
                var lst2=lstsegment.stream().map(x->this.trim(x,flag)).collect(Collectors.toList());
                return String.join(flag,lst2);
            }
        }

        private String trim(String value,String flag) {
            var tmp=this.trimStart(value,flag);
            var tmp2=this.trimEnd(tmp,flag);
            return tmp2;
        }

        private String trimEnd(String value, String flag) {
            if(!value.endsWith(flag))
                return value;
            var tmp= value.substring(0,value.length()-flag.length());
            return trimEnd(tmp,flag);
        }

        private String trimStart(String value, String flag) {
            if(!value.startsWith(flag))
                return value;
            var tmp=value.substring(flag.length(),value.length());
            return trimStart(tmp,flag);
        }
    }
}
