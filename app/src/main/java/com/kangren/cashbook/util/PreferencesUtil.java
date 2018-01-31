package com.kangren.cashbook.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * sp存储工具类 Created by kangren on 2018/1/31.
 */

public class PreferencesUtil
{
    public static final String FIRST_ENTER = "first_enter";

    /**
     * 是否第一次进入应用
     * 
     * @param context 上下文
     * @return boolean类型
     */
    public static boolean isFirstEnter(Context context)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(FIRST_ENTER, true);
    }

    /**
     * 设置FIRST_ENTER为false
     * 
     * @param context 上下文
     */
    public static void setFirstEnter(Context context)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(FIRST_ENTER, false);
        editor.apply();
    }
}
