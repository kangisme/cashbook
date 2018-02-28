package com.kangren.cashbook;

import java.lang.ref.WeakReference;

import com.kang.cashbook.data.util.ThreadPool;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by kangren on 2018/1/26.
 */

public class MyApplication extends Application
{
    public static int count;

    private WeakReference<Activity> mActivity;

    public static boolean isAppVisible()
    {
        return count > 0;
    }

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

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks()
        {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState)
            {

            }

            @Override
            public void onActivityStarted(Activity activity)
            {
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity)
            {
                mActivity = new WeakReference<Activity>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity)
            {

            }

            @Override
            public void onActivityStopped(Activity activity)
            {
                count--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState)
            {

            }

            @Override
            public void onActivityDestroyed(Activity activity)
            {

            }
        });
    }

    public Activity getCurrentActivity()
    {
        if (mActivity == null)
        {
            return null;
        }
        return mActivity.get();
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        ThreadPool.shutdown();
    }
}
