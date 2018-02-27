package com.kangren.cashbook;

import com.kang.cashbook.data.util.ThreadPool;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import android.app.Application;

/**
 * Created by kangren on 2018/1/26.
 */

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        FormatStrategy strategy = PrettyFormatStrategy.newBuilder().showThreadInfo(false).tag("cashbookTAG").build();
        Logger.addLogAdapter(new AndroidLogAdapter(strategy)
        {
            @Override
            public boolean isLoggable(int priority, String tag)
            {
                return BuildConfig.DEBUG;
            }
        });

        Logger.d("application create");
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        ThreadPool.shutdown();
    }
}
