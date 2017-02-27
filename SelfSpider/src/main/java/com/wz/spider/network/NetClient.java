package com.wz.spider.network;

import okhttp3.*;
import okhttp3.Request.Builder;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class NetClient {

    public static Response doPost(String url, JSONObject params) throws IOException {

        return doPost(url, params, genDefaultPcHeaders());
    }

    public static Response doPost(String url, JSONObject params, HashMap<String, String> headers) throws IOException {

        if (url != null && params != null) {
            Request.Builder builder = new Request.Builder();
            addHeaders(builder, headers);
            builder.url(url);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());
            builder.post(requestBody);
            Request request = builder.build();
            OkHttpClient okHttpClient = OkHttpClientManager.getInstance().getOkHttpClient();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        return null;
    }

    public static Response doPost(String url, HashMap<String, String> params) {

        return doPost(url, params, genDefaultPcHeaders());
    }

    public static Response doPost(String url, HashMap<String, String> params, HashMap<String, String> headers) {

        if (url != null && params != null) {
            Request.Builder builder = new Request.Builder();
            addHeaders(builder, headers);
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Iterator<Entry<String, String>> iterator = entrySet.iterator(); iterator.hasNext(); ) {
                Entry<String, String> entry = iterator.next();
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
            builder.post(formBodyBuilder.build());
            builder.url(url);
            Request request = builder.build();
            OkHttpClient okHttpClient = OkHttpClientManager.getInstance().getOkHttpClient();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        return null;
    }

    public static Response doGet(String url) {

        return doGet(url, genDefaultPcHeaders());
    }

    public static Response doGet(String url, HashMap<String, String> headers) {

        if (url != null) {
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            addHeaders(builder, headers);
            Request request = builder.build();
            OkHttpClient okHttpClient = OkHttpClientManager.getInstance().getOkHttpClient();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        return null;
    }

    private static HashMap<String, String> genDefaultPcHeaders() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "*/*");
        headers.put("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        return headers;
    }

    private static HashMap<String, String> genDefaultMobileHeaders() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "*/*");
        headers.put("User-Agent",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        return headers;
    }

    /**
     * addHeaders 增加Headers
     *
     * @param builder
     * @param headers
     * @throws @since 3.1.0
     * @permission void
     * @api 5
     */
    private static void addHeaders(Builder builder, HashMap<String, String> headers) {

        if (headers == null || builder == null || headers.size() == 0) {
            return;
        }

        Set<Entry<String, String>> set = headers.entrySet();
        Iterator<Entry<String, String>> iterator = set.iterator();
        for (; iterator.hasNext(); ) {
            Entry<String, String> entry = iterator.next();
            builder.addHeader(entry.getKey(), entry.getValue());
        }
    }

    /**
     * getCharSetFromResponse 从Response中获取编码
     *
     * @param response
     * @return
     * @throws @since 3.1.0
     * @permission String
     * @api 5
     */
    public static String getCharSetFromResponse(Response response) {

        return getCharSetFromResponse(response, "UTF-8");
    }


    /**
     * getCharSetFromResponse 从Response中获取编码
     *
     * @param response
     * @return
     * @throws @since 3.1.0
     * @permission String
     * @api 5
     */
    public static String getCharSetFromResponse(Response response, String defaultCharset) {

        if (response != null && response.isSuccessful()) {
            MediaType mediaType = response.body().contentType();
            return mediaType.charset(Charset.forName(defaultCharset)).toString();
        }
        return defaultCharset;
    }

    public static String getContentTypeFromResponse(Response response) {

        if (response != null && response.isSuccessful()) {
            MediaType mediaType = response.body().contentType();
            return mediaType.type() + "/" + mediaType.subtype();
        }
        return "text/html";
    }
}
