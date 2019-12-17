package com.wtlei.task;

import com.wtlei.spider.Spider;
import com.wtlei.utils.BeanContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wtlei
 * @name ThreadTask
 * @date 2019-12-16 20:48
 */

@Slf4j
public class ThreadTask implements Runnable {

    private Spider spider;

    private String url;

    public ThreadTask(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        try {
            log.info(Thread.currentThread().getName() + "正在执行...");
            this.spider = BeanContext.getApplicationContext().getBean(Spider.class);
            spider.nextPage(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
