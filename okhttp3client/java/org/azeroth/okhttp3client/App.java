package org.azeroth.okhttp3client;


import okhttp3.*;

import java.util.Collections;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Throwable {
        invokeWithX509();
        invokeByProxy();
        invokeWithNTLM();
        System.out.println( "Hello World!" );


    }

    //NTLM认证的demo,服务端启用windows基本认证/window集成认证
    private static void invokeWithNTLM() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .authenticator(new NTLMAuthenticator("china\\eeroom", "123456"))
                // .some other init here if necessary
                .build();
    }

    //代理方式调用api,不涉及认证
    private static void invokeByProxy() {
        var client=new HttpChannel<IHome>(IHome.class,"http://localhost/home.asmx").client();
        var rt= client.doWork(3);
    }

    //单向认证和双向认证的demo
    private static void invokeWithX509() throws Exception {
        //服务端公钥
        String certPath = "D:\\iiscer.cer";
        //客户端私钥
        String p12Path ="D:\\wchCert.pfx";
        //双向认证，服务端把客户端的公钥设置为信任的证书，windows上使用证书管理器
        SSLSocketFactoryWrapper sSLSocketFactoryWrapper = SSLSocketFactoryWrapper.newInstance(certPath, p12Path, "123456");
        //单向认证，客户端认证服务器
        SSLSocketFactoryWrapper sSLSocketFactoryWrapper2 = SSLSocketFactoryWrapper.newInstance(certPath);
        String url = "https://localhost/WcfTwoWayAuthentication/Home.svc/DoWork";
        ConnectionSpec connectionSpec=new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS).tlsVersions(TlsVersion.TLS_1_2).build();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.sslSocketFactory(sSLSocketFactoryWrapper.socketFactory, sSLSocketFactoryWrapper.x509TrustManager)
                .connectionSpecs(java.util.Collections.singletonList(connectionSpec))//指定tls版本
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))//指定http版本
                .hostnameVerifier((x,y)->true);//解决报错javax.net.ssl.SSLPeerUnverifiedException: Hostname 127.0.0.1 not verified
        OkHttpClient client = clientBuilder.build();


        RequestBody body=new FormBody(Collections.EMPTY_LIST,Collections.EMPTY_LIST);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .method("POST",body);
        Request request = builder.build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        System.out.println(result);
    }
}
