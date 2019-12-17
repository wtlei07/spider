package com.wtlei.task;

import com.wtlei.spider.Spider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>Title: StartService
 * <p>Description:
 *
 * @author wtlei
 * @version 1.0
 * @date 2019/12/17 20:23
 */

@Component
@Slf4j
public class StartService implements ApplicationRunner {

    @Resource
    private Spider spider;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        spider.startSpider();
    }
}
