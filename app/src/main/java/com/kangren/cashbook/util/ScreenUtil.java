package com.kangren.cashbook.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 屏幕参数工具类 Created by kangren on 2018/1/29.
 */

public class ScreenUtil
{
    private static ScreenUtil screenUtil = null;

    private float density;

    private int width;

    private int screenHeight;

    private int statusHeight;

    private ScreenUtil(Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        density = displayMetrics.density;
        width = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        statusHeight = getStatusBarHeight(context);
    }

    public static ScreenUtil getInstance(Context context)
    {
        if (screenUtil == null)
        {
            synchronized (ScreenUtil.class)
            {
                if (screenUtil == null)
                {
                    screenUtil = new ScreenUtil(context.getApplicationContext());
                }
            }
        }
        return screenUtil;
    }

    private int getStatusBarHeight(Context context)
    {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try
        {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取屏幕密度
     * 
     * @return float类型
     */
    public float getDensity()
    {
        return density;
    }

    /**
     * 获取屏幕宽度
     * 
     * @return int类型
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * 获取屏幕高度 此高度包括屏幕上方状态栏和ActionBar（如果有），但不包括虚拟按键（如果有，华为手机）
     * 获取整个屏幕高度需要加上虚拟按键高度{@link VirtualButtonUtil}
     * 
     * @return int类型
     */
    public int getScreenHeight()
    {
        return screenHeight;
    }

    /**
     * 获取状态栏高度
     * 
     * @return int类型
     */
    public int getStatusHeight()
    {
        return statusHeight;
    }
}
