package com.wz.spider.util;

import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    public static void saveImageToDisk(String filePath, InputStream is) {

        FileUtil.saveInputStreamToFile(filePath, is);
    }


    public static void showPic(String imgPath) {

        try {
            Runtime.getRuntime().exec("rundll32 c:\\Windows\\System32\\shimgvw.dll,ImageView_Fullscreen " + imgPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}