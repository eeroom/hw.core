package org.azeroth.http;


import okhttp3.*;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collections;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Throwable {
        //httpsRequest();
        httpchanelRequest();
        NtlmDemo();
        System.out.println( "Hello World!" );


    }

    //服务端启用windows基本认证/window集成认证
    private static void NtlmDemo() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .authenticator(new NTLMAuthenticator("china\\eeroom", "123456"))
                // .some other init here if necessary
                .build();
    }

    private static void httpchanelRequest() {
        var client=new HttpChannel<IHome>(IHome.class,"http://localhost/home.asmx").client();
        var rt= client.doWork(3);
    }

    private static void httpsRequest() throws Exception {
        //服务端公钥
        String certPath = "D:\\iiscer.cer";
        //客户端私钥
        String p12Path ="D:\\wchCert.pfx";
        RT rt= getSocketFactory(certPath, p12Path, "123456");
        //RT rt= getSocketFactory(certPath);
        String url = "https://localhost/WcfTwoWayAuthentication/Home.svc/DoWork";
        ConnectionSpec connectionSpec=new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS).tlsVersions(TlsVersion.TLS_1_2).build();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.sslSocketFactory(rt.socketFactory,rt.x509TrustManager)
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

    /**
     * 单向认证，客户端认证服务器
     * @param cerPath
     * @return
     * @throws Exception
     */
    private static RT getSocketFactory(String cerPath) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        InputStream inputStream = new FileInputStream(new File(cerPath));
        Certificate ca = certificateFactory.generateCertificate(inputStream);

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("server", ca);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, new SecureRandom());
        var socketFactory = sslContext.getSocketFactory();

        RT rt=new RT();
        rt.socketFactory=socketFactory;
        rt.x509TrustManager=(X509TrustManager)trustManagers[0];
        return rt;
    }

    /**
     * 双向认证
     * @param cerPath
     * @param p12Path
     * @param password
     * @return
     * @throws Exception
     */
    public static RT getSocketFactory(String cerPath, String p12Path, String password) throws Exception {

        var p12InputStream = new FileInputStream(new File(p12Path));
        KeyStore kmks = KeyStore.getInstance("PKCS12");
        kmks.load(p12InputStream, password.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(kmks, password.toCharArray());
        KeyManager[] kms = keyManagerFactory.getKeyManagers();

        var cerInputStream = new FileInputStream(new File(cerPath));
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate ca = certificateFactory.generateCertificate(cerInputStream);

        KeyStore kstm = KeyStore.getInstance("PKCS12");
        kstm.load(null, null);
        kstm.setCertificateEntry("server", ca);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(kstm);
        TrustManager[] tms = trustManagerFactory.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kms, tms, new SecureRandom());
        var socketFactory = sslContext.getSocketFactory();

        cerInputStream.close();
        p12InputStream.close();

        RT rt=new RT();
        rt.socketFactory=socketFactory;
        rt.x509TrustManager=(X509TrustManager)tms[0];
        return rt;
    }

}
