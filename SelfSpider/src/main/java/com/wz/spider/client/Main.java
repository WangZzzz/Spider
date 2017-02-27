package com.wz.spider.client;

import com.wz.spider.network.NetClient;
import com.wz.spider.util.ImageUtil;
import com.wz.spider.util.StringUtil;
import com.wz.spider.util.UrlUtil;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int MAX_THREAD_SIZE = 10;
    private static final String SAVE_DIR = "D:/spider";
    private static Set<String> sVisitedUrls = new HashSet<String>();
    private static Queue<String> sNewUrls = new LinkedList<String>();
    private static Set<String> sImgUrls = new HashSet<String>();
    private static ExecutorService sExecutor;

    public static void main(String[] args) throws IOException {

        init();
        while (sNewUrls.size() > 0) {
            try {
                String newUrl = sNewUrls.poll();
                System.out.println("当前需要爬取的网页数量--->" + sNewUrls.size() + "，正在爬取的网页地址--->" + newUrl);
                getLinks(newUrl);
                if (sImgUrls.size() > 0) {
                    for (Iterator<String> iterator = sImgUrls.iterator(); iterator.hasNext(); ) {
                        String imgUrl = iterator.next();
                        sExecutor.execute(new GetImageThread(SAVE_DIR, imgUrl));
                    }
                    // awaitTermination返回false即超时会继续循环，返回true即线程池中的线程执行完成主线程跳出循环往下执行，每隔10秒循环一次
                    if (!(sExecutor.awaitTermination(100, TimeUnit.SECONDS))) {
                        sExecutor.shutdownNow();
                        System.out.println("shutdoww--->继续执行");
                    }
                    sImgUrls.clear();
                }
            } catch (Exception e) {
                // TODO: handle exception
                continue;
            }
        }
        System.out.println("爬虫停止！");
    }


    private static void init() {

        String startUrl = "http://www.qq.com";
        sNewUrls.add(startUrl);
        sExecutor = Executors.newFixedThreadPool(MAX_THREAD_SIZE);
        File file = new File(SAVE_DIR);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
    }


    private static void getLinks(String url) throws IOException {

        sVisitedUrls.add(url);
        Response response = NetClient.doGet(url);
        if (response != null && response.isSuccessful()) {
            if ("text/html".equals(NetClient.getContentTypeFromResponse(response))) {
                String res = StringUtil.responseToString(response, NetClient.getCharSetFromResponse(response));
                // System.out.println( res );
                Document document = Jsoup.parse(res);
                // 获取新网页链接
                Elements elements = document.getElementsByTag("a");
                ListIterator<Element> iterator = elements.listIterator();
                for (; iterator.hasNext(); ) {
                    Element element = iterator.next();
                    String href = element.attr("href");
                    String newUrl = UrlUtil.getAbsoluteUrl(url, href);
                    if (!sVisitedUrls.contains(newUrl)) {
                        System.out.println("新加入网页--->" + element.text() + "，网页地址--->" + newUrl);
                        sNewUrls.add(newUrl);
                    }
                }
                // 获取图片链接
                Elements imgElements = document.getElementsByTag("img");
                ListIterator<Element> imgIterator = imgElements.listIterator();
                for (; imgIterator.hasNext(); ) {
                    Element element = imgIterator.next();
                    String src = element.attr("src");
                    String newUrl = UrlUtil.getAbsoluteUrl(url, src);
                    if (!sVisitedUrls.contains(newUrl)) {
                        System.out.println("新获取图片链接--->" + newUrl);
                        sImgUrls.add(newUrl);
                    }
                }
            }
        }
    }


    private static class GetImageThread extends Thread {

        private String mSaveDir;
        private String mImgUrl;


        public GetImageThread(String saveDir, String imgUrl) {

            mSaveDir = saveDir;
            mImgUrl = imgUrl;
        }


        @Override
        public void run() {

            // TODO Auto-generated method stub
            Response response = NetClient.doGet(mImgUrl);
            if (response != null && response.isSuccessful()) {
                String savePath = mSaveDir + File.separator + UrlUtil.getImgNameFromUrl(mImgUrl);
                System.out.println("正在下载图片--->" + mImgUrl + "，保存地址--->" + savePath);
                ImageUtil.saveImageToDisk(savePath, response.body().byteStream());
            }
        }
    }

}
