package io.github.eeroom.javacore.证书认证和NTLM认证;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class SSLSocketFactoryWrapper {

    SSLSocketFactory socketFactory;
    X509TrustManager x509TrustManager;

    /**
     * 单向认证，客户端认证服务器
     * @param cerPath 服务端公钥
     * @return
     * @throws Exception
     */
    public static SSLSocketFactoryWrapper newInstance(String cerPath) throws Exception {
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

        SSLSocketFactoryWrapper sSLSocketFactoryWrapper =new SSLSocketFactoryWrapper();
        sSLSocketFactoryWrapper.socketFactory=socketFactory;
        sSLSocketFactoryWrapper.x509TrustManager=(X509TrustManager)trustManagers[0];
        return sSLSocketFactoryWrapper;
    }

    /**
     * 双向认证
     * @param cerPath 服务端公钥
     * @param p12Path 客户端私钥
     * @param password 私钥的密码
     * @return
     * @throws Exception
     */
    public static SSLSocketFactoryWrapper newInstance(String cerPath, String p12Path, String password) throws Exception {

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

        SSLSocketFactoryWrapper sSLSocketFactoryWrapper =new SSLSocketFactoryWrapper();
        sSLSocketFactoryWrapper.socketFactory=socketFactory;
        sSLSocketFactoryWrapper.x509TrustManager=(X509TrustManager)tms[0];
        return sSLSocketFactoryWrapper;
    }

}
