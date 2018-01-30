package com.kangren.cashbook.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by kangren on 2018/1/29.
 */

public class SysInfo
{
    private static SysInfo sysInfo = null;

    private Context mContext;

    private float density;

    private SysInfo(Context context)
    {
        this.mContext = context;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        density = displayMetrics.density;
    }

    public static SysInfo getInstance(Context context)
    {
        if (sysInfo == null)
        {
            synchronized (SysInfo.class)
            {
                if (sysInfo == null)
                {
                    sysInfo = new SysInfo(context.getApplicationContext());
                }
            }
        }
        return sysInfo;
    }

    public float getDensity()
    {
        return density;
    }
}
