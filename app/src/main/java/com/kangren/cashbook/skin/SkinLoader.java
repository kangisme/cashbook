package com.kangren.cashbook.skin;

import java.io.File;

import com.kang.cashbook.skin.BuildConfig;

import android.content.Context;

/**
 * Created by kangren on 2018/2/7.
 */

public class SkinLoader
{
    /**
     * 默认间隔时间，1小时
     */
    private static final long DEFAULT_REPEAT_INTERVAL = BuildConfig.DEBUG ? 30000L : 60 * 60 * 1000L;

    /**
     * 单例
     */
    private static SkinLoader mInstance;

    /**
     * 皮肤存放路径
     */
    private final String SKIN_DIR = "skin";

    /**
     * 上次加载皮肤信息的时间
     */
    private long mLastLoadTime = 0;

    /**
     * 是否正在加载皮肤
     */
    private boolean isLoading = false;

    /**
     * contex
     */
    private Context mContext;

    /**
     * 构造函数，隐藏
     * 
     * @param context 上下文
     */
    private SkinLoader(Context context)
    {
        File file = context.getDir(SKIN_DIR, Context.MODE_PRIVATE);
        mContext = context.getApplicationContext();
    }

    /**
     * 获取单例
     * 
     * @param context 上下文
     * @return 唯一实例
     */
    public static SkinLoader getInstance(Context context)
    {
        if (mInstance == null)
        {
            synchronized (SkinLoader.class)
            {
                if (mInstance == null)
                {
                    mInstance = new SkinLoader(context);
                }
            }
        }
        return mInstance;
    }

    public String getSkinDir()
    {
        return SKIN_DIR;
    }

    /**
     * 清除上次加载皮肤的时间
     */
    public void reset()
    {
        mLastLoadTime = 0;
    }
}
