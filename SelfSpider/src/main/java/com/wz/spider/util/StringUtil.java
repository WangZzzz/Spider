package com.wz.spider.util;

import okhttp3.Response;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

public class StringUtil {

    public static String responseToString(Response response) {

        return responseToString(response, "utf-8");
    }

    public static String responseToString(Response response, String charset) {

        if (response != null) {
            BufferedReader bfr = null;
            InputStreamReader isr = null;
            StringBuilder sBuilder = new StringBuilder();
            try {
                InputStream is = null;
                try {
                    is = new GZIPInputStream(response.body().byteStream());
                } catch (ZipException e) {
                    e.printStackTrace();
                    is = response.body().byteStream();
                }
                isr = new InputStreamReader(is, charset);
                bfr = new BufferedReader(isr);
                String line = null;
                while ((line = bfr.readLine()) != null) {
                    sBuilder.append(line);
                    sBuilder.append("\n");
                }
                return sBuilder.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (bfr != null) {
                        bfr.close();
                    }
                    if (isr != null) {
                        isr.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
