package com.kang.cashbook.data.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.orhanobut.logger.Logger;

/**
 * 线程池 Created by kangren on 2018/2/27.
 */

public class ThreadPool
{
    private static final String CLASSTAG = ThreadPool.class.getSimpleName();

    private static final int POOL_SIZE = 10;

    private static ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

    /**
     * 不允许实例化
     */
    private ThreadPool()
    {
    }

    /**
     * 任务加入线程池
     * 
     * @param runnable 任务
     */
    public static void add(Runnable runnable)
    {
        try
        {
            executorService.submit(runnable);
        }
        catch (Exception e)
        {
            Logger.e(CLASSTAG + e.getMessage());
        }
    }

    public static <T> Future<T> add(Runnable runnable, T result)
    {
        try
        {
            return executorService.submit(runnable, result);
        }
        catch (Exception e)
        {
            Logger.e(CLASSTAG + e.getMessage());
        }
        return null;
    }

    public static void shutdown()
    {
        executorService.shutdownNow();
    }
}
