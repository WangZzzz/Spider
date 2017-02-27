package com.wz.spider.util;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtil {

    /**
     * getAbsoluteUrl 获取URL全路径
     *
     * @param srcUrl 当前网页的url
     * @param tmpUrl 获取的href
     * @return
     * @throws
     * @permission String
     * @api 5
     * @since 3.1.0
     */
    public static String getAbsoluteUrl(String srcUrl, String tmpUrl) {

        if (srcUrl == null || tmpUrl == null) {
            return null;
        }
        try {
            URI uri = new URI(srcUrl);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            if (tmpUrl.startsWith("http") || tmpUrl.startsWith("https")) {
                return tmpUrl;
            } else if (tmpUrl.startsWith(host)) {
                return scheme + "://" + tmpUrl;
            } else if (tmpUrl.startsWith("/")) {
                return scheme + "://" + host + tmpUrl;
            } else {
                return scheme + "://" + host + "/" + tmpUrl;
            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    public static String getImgNameFromUrl(String url) {

        if (url == null || url == "") {
            return System.currentTimeMillis() + ".jpg";
        }
        int startIndex = url.lastIndexOf("/");
        int endIndex = url.length();
        if (url.contains("?")) {
            endIndex = url.indexOf("?");
        }
        return url.substring(startIndex, endIndex);
    }
}
