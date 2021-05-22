package org.azeroth.http;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class SSLSocketFactoryWrapper {

    SSLSocketFactory socketFactory;
    X509TrustManager x509TrustManager;
}
