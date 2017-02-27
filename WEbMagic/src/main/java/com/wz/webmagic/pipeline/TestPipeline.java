package com.wz.webmagic.pipeline;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;
import java.util.Set;

/**
 * Created by wz on 2017/1/18.
 */
public class TestPipeline implements Pipeline {

    public void process(ResultItems resultItems, Task task) {
        System.out.println(resultItems.getRequest().getUrl() + " ### ");
        Map<String, Object> map = resultItems.getAll();
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            System.out.println(entry.getKey() + "--->" + entry.getValue());
        }
    }
}
