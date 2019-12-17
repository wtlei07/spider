package com.wtlei.utils;

import com.wtlei.factory.NameThreadFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * <p>Title: ThreadPoolFactoryUtil
 * <p>Description:
 *
 * @author wtlei
 * @version 1.0
 * @date 2019/12/16 21:19
 */

@Slf4j
@AllArgsConstructor
public class ThreadPoolFactoryUtil {

    public static ExecutorService getExecutorService(int nThreads){
        log.info("初始化线程池...");
        return new ThreadPoolExecutor(nThreads, nThreads, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(512), new NameThreadFactory(),new ThreadPoolExecutor.DiscardPolicy());
    }
}
