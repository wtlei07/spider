package com.wtlei.factory;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wtlei
 * @name NameThreadFactory
 * @date 2019-12-15 23:06
 */

@Slf4j
public class NameThreadFactory implements ThreadFactory {

    private static final AtomicInteger M_THREAD_NUM = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "线程" + M_THREAD_NUM.getAndIncrement());
        return t;
    }
}
