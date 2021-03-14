package com.wtlei.task;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author wtlei
 * @name SpiderTask
 * @date 2019-12-14 23:30
 */

public class SpiderTask {

    @Scheduled(cron = "* * * * * *")
    private void getImages(){
        int num = 11;
    }


}
