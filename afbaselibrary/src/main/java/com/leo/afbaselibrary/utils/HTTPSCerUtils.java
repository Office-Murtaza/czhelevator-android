package com.leo.afbaselibrary.utils;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created By SongPeng  on 2019/12/30
 * Email : 1531603384@qq.com
 * https证书工具
 */
public class HTTPSCerUtils {

    //信任所有证书
    public static void setTrustAllCertificate(OkHttpClient.Builder okHttpClientBuilder) {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            X509TrustManager trustAllManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            sc.init(null, new TrustManager[]{trustAllManager}, new SecureRandom());
            okHttpClientBuilder.sslSocketFactory(sc.getSocketFactory(), trustAllManager);
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
