package com.wz.spider.client;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.*;
import java.util.Base64;

public class Test {

    /**
     * main(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
     *
     * @param args
     * @throws InterruptedException
     * @throws @since               3.1.0
     * @permission void
     * @api 5
     */
    public static void main(String[] args) {
        File file = new File("D:/test.der");
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            File oFile = new File("D:/output.txt");
            if (oFile.exists()) {
                oFile.delete();
            }
            oFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(oFile);
            BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(fos));
            byte[] bytes = bos.toByteArray();
            String tmpKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxEaQcRtwUBrIb/731jm0JP8/PRxmXuUCuxsC8VnNP8Xcd5BwqBvX+BTJvFQ8QJ+bVL1XensDiPhebGdmileezhC+QQogn7YpfyH5Ma8opP3mZhH7AkG3o5jKuJ45X40HAOGZ3f68hJOh3B4+MmtNDRvRtKuyRgVeUiA86Z61pmwIDAQAB";
            PublicKey publicKey = geneneratePublicKey(Base64.getDecoder().decode(tmpKey));
            System.out.println(publicKey.getAlgorithm());
            System.out.println(publicKey.getEncoded());
            System.out.println(publicKey.toString());
//            String res = byte2hex(bos.toByteArray());
            String res = Base64.getEncoder().encodeToString(bos.toByteArray());
            System.out.println(res);
            bfw.write(res);
            bfw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String byte2hex(byte[] b) {

        StringBuffer sb = new StringBuffer();
        String tmp = "";
        for (int i = 0; i < b.length; i++) {
            tmp = Integer.toHexString(b[i] & 0XFF);
            if (tmp.length() == 1) {
                sb.append("0" + tmp);
            } else {
                sb.append(tmp);
            }

        }
        return sb.toString();
    }

    private static void testJDBC() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:D:/test.db");
            statement = connection.createStatement();
            statement.executeUpdate("create table if not exists article(id integer primary key autoincrement, title text, author text, link text, summary text, date text);");
            ResultSet resultSet = statement.executeQuery("select * from article;");
            while (resultSet.next()) {
                String author = resultSet.getString("author");
                String title = resultSet.getString("title");
                String link = resultSet.getString("link");
                String date = resultSet.getString("date");
                String summary = resultSet.getString("summary");
                System.out.println("链接：" + link);
                System.out.println("标题：" + title);
                System.out.println("作者：" + author);
                System.out.println("发布时间：" + date);
                System.out.println("摘要：" + summary);
                System.out.println("--------------------------------------------------------------------------------");
            }
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PublicKey geneneratePublicKey(byte[] key) {
        KeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
