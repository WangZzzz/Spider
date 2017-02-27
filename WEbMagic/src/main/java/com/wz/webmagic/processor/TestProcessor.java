package com.wz.webmagic.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class TestProcessor implements PageProcessor {
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me()
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
            .setRetryTimes(3)
            .setSleepTime(100);

    public Site getSite() {
        // TODO Auto-generated method stub
        return site;
    }

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        // TODO Auto-generated method stub
        // 部分二：定义如何抽取页面信息，并保存下来
        page.putField("titles", page.getHtml().xpath("//td[@class='title']/a/text()").all());
        page.putField("content", page.getHtml().xpath("//*[@id=\"link-report\"]/div/p[1]/text()"));
        // 部分三：从页面发现后续的url地址来抓取
        page.addTargetRequests(page.getHtml().xpath("//td[@class='title']/a/@href").all());

    }

}