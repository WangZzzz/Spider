package com.wz.spider.util;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class FileUtil {

    public static File createNewFile(String filePath) {

        return createNewFile(filePath, false);
    }


    public static File createNewFile(String filePath, boolean delExists) {

        if (filePath == null || filePath == "") {
            return null;
        }

        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (delExists) {
                file.delete();
            } else {
                return file;
            }
        }
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    public static void writeStringToFile(String filePath, String content) {

        File file = createNewFile(filePath);
        if (file != null) {
            BufferedWriter bfw = null;
            try {
                bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                bfw.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bfw != null) {
                        bfw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void saveInputStreamToFile(String filePath, InputStream is) {

        if (is == null) {
            return;
        }
        File file = createNewFile(filePath, false);
        if (file != null) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void writeCSV(String filePath, String key, String value) {

        writeCSV(filePath, key, value, true, "UTF-8");
    }


    public static void writeCSV(String filePath, String key, String value, String charset) {

        writeCSV(filePath, key, value, true, charset);
    }


    public static void writeCSV(String filePath, String key, String value, boolean append) {

        writeCSV(filePath, key, value, append, "UTF-8");
    }


    public static void writeCSV(String filePath, String key, String value, boolean append, String charset) {

        File file = new File(filePath);
        if (file != null && file.exists() && file.isFile()) {
            BufferedWriter bfw = null;
            try {
                bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));
                bfw.write(key + "," + value);
                bfw.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bfw != null) {
                        bfw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void writeCSV(String filePath, HashMap<String, String> data) {

        writeCSV(filePath, data, true, "UTF-8");
    }


    public static void writeCSV(String filePath, HashMap<String, String> data, boolean append) {

        writeCSV(filePath, data, append, "UTF-8");
    }


    public static void writeCSV(String filePath, HashMap<String, String> data, String charset) {

        writeCSV(filePath, data, true, charset);
    }


    public static void writeCSV(String filePath, HashMap<String, String> data, boolean append, String charset) {

        File file = new File(filePath);
        if (file != null && file.exists() && file.isFile()) {
            BufferedWriter bfw = null;
            try {
                bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));
                Set<Entry<String, String>> entrySet = data.entrySet();
                for (Iterator<Entry<String, String>> iterator = entrySet.iterator(); iterator.hasNext(); ) {
                    Entry<String, String> entry = iterator.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    bfw.write(key + "," + value);
                    bfw.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bfw != null) {
                        bfw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
