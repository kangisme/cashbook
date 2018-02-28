package com.kangren.cashbook;

import android.content.Context;

/**
 * Created by kangren on 2018/2/28.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler
{

    /**
     * 单例
     */
    public static CrashHandler instance;

    private Context context;

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public static CrashHandler getInstance()
    {
        if (instance == null)
        {
            synchronized (CrashHandler.class)
            {
                if (instance == null)
                {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    public void init(Context context)
    {
        this.context = context;
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {

    }
}
