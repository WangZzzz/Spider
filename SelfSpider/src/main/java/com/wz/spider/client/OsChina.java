package com.wz.spider.client;

import com.wz.spider.db.OsChinaDb;
import com.wz.spider.entity.Article;
import com.wz.spider.network.NetClient;
import com.wz.spider.util.StringUtil;
import com.wz.spider.util.UrlUtil;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by wz on 2017/1/19.
 * 爬取OsChina每日资讯
 */
public class OsChina {
    private static Queue<String> sUrls = new LinkedList<String>();
    private static List<Article> sResList = new ArrayList<Article>();
    private static final int CNT = 20;

    public static void main(String[] args) {
        init();
        while (sUrls.size() > 0) {
            String newUrl = sUrls.poll();
            System.out.println("****************************************************************************************");
            System.out.println("当前正在爬取--->" + newUrl);
            System.out.println("****************************************************************************************");
            getNews(newUrl);
        }
        System.out.println("总共爬取到" + sResList.size() + "条记录！");
        OsChinaDb db = new OsChinaDb();
        db.insert(sResList);
        System.out.println("爬取完毕！");
    }

    private static void getNews(String pageUrl) {
        Response response = NetClient.doGet(pageUrl);
        if (response != null && response.isSuccessful()) {
            String html = StringUtil.responseToString(response, NetClient.getCharSetFromResponse(response));
            Document document = Jsoup.parse(html);
            if (document != null) {
                Elements elements = document.select("div[class=main-info box-aw]");
                for (Element element : elements) {
                    String link = UrlUtil.getAbsoluteUrl(pageUrl, element.select("a[class=title]").attr("href"));
                    String title = element.select("span[class=text-ellipsis]").text();
                    String summary = element.select("div[class=sc sc-text text-gradient wrap summary]").text();
                    String author = element.select("span[class=mr] > a").text();
                    String date = element.select("span[class=mr]").get(0).text().replace(author, "").replace(" ", "");
                    System.out.println("链接：" + link);
                    System.out.println("标题：" + title);
                    System.out.println("作者：" + author);
                    System.out.println("发布时间：" + date);
                    System.out.println("摘要：" + summary);
                    Article article = new Article();
                    article.author = author;
                    article.link = link;
                    article.date = date;
                    article.summary = summary;
                    article.title = title;
                    sResList.add(article);
                    System.out.println("--------------------------------------------------------------------------------");
                }
            }

        }
    }


    private static void init() {
        for (int i = 1; i <= CNT; i++) {
            String url = "https://www.oschina.net/action/ajax/get_more_news_list?newsType=&p=" + i;
            sUrls.add(url);
        }
    }
}
