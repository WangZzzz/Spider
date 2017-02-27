package com.wz.webmagic.client;

import com.wz.webmagic.pipeline.TestPipeline;
import com.wz.webmagic.processor.TestProcessor;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;

import javax.management.JMException;

public class Client {

    private static final int CNT = 5;
    private static String[] sPageUrls = new String[CNT + 1];

    public static void main(String[] args) {
        init();
        // TODO Auto-generated method stub
        Spider picSpider = Spider.create(new TestProcessor())
                // 从"https://github.com/code4craft"开始抓
                .addUrl(sPageUrls)
                .addPipeline(new TestPipeline())
                // 开启5个线程抓取
                .thread(5);
        //添加监控
        try {
            SpiderMonitor.instance().register(picSpider);
        } catch (JMException e) {
            e.printStackTrace();
        }
        // 启动爬虫
        picSpider.run();
    }

    private static void init() {
        for (int i = 0; i <= CNT; i++) {
            sPageUrls[i] = "https://www.douban.com/group/26926/discussion?start=" + 25 * i;
        }
    }
}
