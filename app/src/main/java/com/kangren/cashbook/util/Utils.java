package com.kangren.cashbook.util;

import android.content.Context;
import android.net.Uri;

/**
 * Created by kangren on 2018/1/31.
 */

public class Utils
{
    public static final String ANDROID_RESOURCE = "android.resource://";

    public static final String FOREWARD_SLASH = "/";

    public static Uri resourceIdToUri(Context context, int resourceId)
    {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }
}
