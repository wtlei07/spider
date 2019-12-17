package com.wtlei.spider;

import com.wtlei.SpiderApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SpiderApplication.class)
@Slf4j
public class SpiderTest {

    private static final String BASE_URL = "https://www.meitulu.com/item/PAGE.html";

    private static final int maxPage = 20335;

    @Autowired
    private Spider spider;

    @Test
    public void startSpider() throws Exception {

        for (int i = 3; i <= maxPage; i++) {
            String url = BASE_URL.replace("PAGE", i + "");
            spider.nextPage(url);
//            log.info("准备爬取下一个资源...");
        }
    }

    @Test
    public void startSpider2() {
        try {
            spider.nextPage("https://www.meitulu.com/item/3.html");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    void testStartSpider() {
        try {
            spider.startSpider();
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}