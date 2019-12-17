package com.wtlei;

import com.wtlei.service.ImageService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

/**
 * @author wtlei
 */
@SpringBootApplication
@MapperScan("com.wtlei.*")
@EnableScheduling
public class SpiderApplication {

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SpiderApplication.class, args);
    }

    @PostConstruct
    public void getBean() {
        applicationContext.getBean(ImageService.class);
    }
}
