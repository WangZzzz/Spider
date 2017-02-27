package com.wz.webmagic.downloader;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;

/**
 * Created by wz on 2017/1/18.
 */
public class TestDownloader implements Downloader {
    @Override
    public Page download(Request request, Task task) {
        return null;
    }

    @Override
    public void setThread(int i) {

    }
}
