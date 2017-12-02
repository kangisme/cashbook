package com.kangren.cashbook.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by kangren on 2017/11/20.
 */

public class JumpUtil
{
    public static void jumpActivity(Context context, Class clazz)
    {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }
}
