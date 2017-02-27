package com.wz.spider.network;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class X509TrustManagerImpl implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {

        // TODO Auto-generated method stub
    }


    @Override
    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {

        // TODO Auto-generated method stub
    }


    @Override
    public X509Certificate[] getAcceptedIssuers() {

        // TODO Auto-generated method stub
        X509Certificate[] certificates = new X509Certificate[]{};
        return certificates;
    }
}
