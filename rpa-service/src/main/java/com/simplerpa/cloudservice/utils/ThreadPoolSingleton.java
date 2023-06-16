package com.simplerpa.cloudservice.utils;

import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolSingleton {
    private static volatile ThreadPoolSingleton instance;
    private static final int THREAD_POOL_SIZE = 30;
    private static final int MAX_THREAD_POOL_SIZE = 30;
    private static final int KEEP_ALIVE_TIME = 1;
    private final ThreadPoolExecutor threadPoolExecutor;

    private ThreadPoolSingleton() {
        threadPoolExecutor = new ThreadPoolExecutor(
                THREAD_POOL_SIZE,
                MAX_THREAD_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.HOURS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static ThreadPoolSingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolSingleton.class) {
                if (instance == null) {
                    instance = new ThreadPoolSingleton();
                }
            }
        }
        return instance;
    }

    public void execute(Runnable runnable) {
       threadPoolExecutor.execute(runnable);
    }

    public Future<?> submit(Runnable runnable) {
        return threadPoolExecutor.submit(runnable);
    }
}