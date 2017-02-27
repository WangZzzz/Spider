package com.wz.spider.network;

import okhttp3.*;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OkHttpClientManager {

    private static final int CONNECT_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIEMOUT = 30;
    private static final int MAX_CONNECT_NUMS = 20;
    private static final int KEEP_ALIVE_TIMES = 20;
    private static volatile OkHttpClientManager sInstance = null;
    X509TrustManager xtm = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {

        }


        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {

        }


        @Override
        public X509Certificate[] getAcceptedIssuers() {

            X509Certificate[] x509Certificates = new X509Certificate[0];
            return x509Certificates;
        }
    };
    private OkHttpClient mClient;
    // 管理、存储cookie
    private HashMap<String, List<Cookie>> mCookieStore = new HashMap<String, List<Cookie>>();


    private OkHttpClientManager() {

    }

    public static OkHttpClientManager getInstance() {

        if (sInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (sInstance == null) {
                    sInstance = new OkHttpClientManager();
                }
            }
        }
        return sInstance;
    }

    public synchronized OkHttpClient getOkHttpClient() {

        if (mClient == null) {
            initOkHttpClient();
        }
        return mClient;
    }

    private void initOkHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置超时时间：连接超时、读取超时、写入超时
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIEMOUT, TimeUnit.SECONDS);
        // 设置最大连接数和存活时间
        builder.connectionPool(new ConnectionPool(MAX_CONNECT_NUMS, KEEP_ALIVE_TIMES, TimeUnit.SECONDS));

        // 忽略https认证
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new X509TrustManagerImpl()}, new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        builder.hostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {

                // TODO Auto-generated method stub
                return true;
            }
        });

        // 自动处理cookies
        builder.cookieJar(new CookieJar() {

            @Override
            public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

                // TODO Auto-generated method stub
                List<Cookie> oldCookies = mCookieStore.get(url.host());
                if (cookies != null && cookies.size() > 0) {
                    if (oldCookies != null) {
                        // CMBCLog.d( TAG, url.toString() +
                        // " saveFromResponse1 : " + Arrays.toString(
                        // oldCookies.toArray() ) );
                        List<Cookie> needRemove = new ArrayList<Cookie>();
                        for (Cookie newCookie : cookies) {
                            for (Cookie oldCookie : oldCookies) {
                                if (newCookie != null && oldCookie != null) {
                                    if (newCookie.name().equals(oldCookie.name())) {
                                        needRemove.add(oldCookie);
                                    }
                                }
                            }
                        }
                        oldCookies.removeAll(needRemove);
                        oldCookies.addAll(cookies);
                        mCookieStore.put(url.host(), oldCookies);
                    } else {
                        mCookieStore.put(url.host(), cookies);
                    }
                }
            }


            @Override
            public synchronized List<Cookie> loadForRequest(HttpUrl url) {

                // TODO Auto-generated method stub
                List<Cookie> cookies = mCookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });

        mClient = builder.build();
    }

    public List<Cookie> getCookies(String host) {

        List<Cookie> cookies = null;
        if (mCookieStore != null && !mCookieStore.isEmpty()) {
            cookies = mCookieStore.get(host);
        }
        return cookies;
    }


    public List<Cookie> getAllCookies() {

        List<Cookie> cookies = null;
        if (mCookieStore != null && !mCookieStore.isEmpty()) {
            Set<String> urls = mCookieStore.keySet();
            for (String url : urls) {
                if (cookies == null) {
                    cookies = mCookieStore.get(url);
                } else {
                    cookies.addAll(mCookieStore.get(url));
                }
            }
        }
        return cookies;
    }


    public void cancelCallsWithTag(Object tag) {

        if (tag == null || mClient == null) {
            return;
        }

        synchronized (OkHttpClientManager.class) {
            for (Call call : mClient.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag()))
                    call.cancel();
            }

            for (Call call : mClient.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag()))
                    call.cancel();
            }
        }
    }


    public void clean() {

        mClient = null;
        // 清除cookie
        mCookieStore.clear();
    }


    public void cleanCookies() {

        mCookieStore.clear();
    }
}
