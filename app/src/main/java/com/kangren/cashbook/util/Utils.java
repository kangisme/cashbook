package com.kangren.cashbook.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import com.kangren.cashbook.BuildConfig;
import com.orhanobut.logger.Logger;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by kangren on 2018/1/31.
 */

public class Utils
{
    public static final String ANDROID_RESOURCE = "android.resource://";

    public static final String FOREWARD_SLASH = "/";

    public static int dp2px(Context context, float dipValue)
    {
        float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 小的为高度
     *
     * @param context
     * @return
     */
    public static int screenHeightPx(Context context)
    {
        int widthPx = context.getResources().getDisplayMetrics().widthPixels;
        int heightPx = context.getResources().getDisplayMetrics().heightPixels;
        return widthPx > heightPx ? heightPx : widthPx;
    }

    public static Uri resourceIdToUri(Context context, int resourceId)
    {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

    /**
     * File转Uri
     * 
     * @param context 上下文
     * @param file File
     * @return Uri
     */
    public static Uri fileToUri(Context context, File file)
    {
        Uri uri = null;
        if (file.exists())
        {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    private static String getRealFilePath(final Context context, final Uri uri)
    {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme))
        {
            data = uri.getPath();
        }
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme))
        {
            Cursor cursor = context.getContentResolver().query(uri, new String[] {MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor)
            {
                if (cursor.moveToFirst())
                {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1)
                    {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 将Uri指向的文件存入file目录
     * 
     * @param context
     * @param uri
     */
    public static void uriSaveFile(Context context, Uri uri)
    {
        File file = new File(getRealFilePath(context, uri));
        String path = context.getFilesDir().getPath() + "/" + BuildConfig.WALLPAPER_FILE;
        copyFile(file, path);
    }

    /**
     * 根据文件路径拷贝文件
     * 
     * @param src 源文件
     * @param destPath 目标文件路径
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile(File src, String destPath)
    {
        boolean result = false;
        if ((src == null) || (destPath == null))
        {
            return false;
        }
        File dest = new File(destPath);
        if (dest.exists())
        {
            dest.delete(); // delete file
        }
        try
        {
            dest.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        FileChannel srcChannel = null;
        FileChannel dstChannel = null;

        try
        {
            srcChannel = new FileInputStream(src).getChannel();
            dstChannel = new FileOutputStream(dest).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
            result = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            try
            {
                if (srcChannel != null)
                {
                    srcChannel.close();
                }
                if (dstChannel != null)
                {
                    dstChannel.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将resource图片存入file
     * 
     * @param context 上下文
     * @param resourceId id
     */
    public static void resourceIdSaveFile(Context context, int resourceId)
    {
        BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawable(resourceId);
        Bitmap bitmap = drawable.getBitmap();
        String path = context.getFilesDir().getPath() + "/" + BuildConfig.WALLPAPER_FILE;
        try
        {
            OutputStream os = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
        }
        catch (FileNotFoundException e)
        {
            Logger.e("tan90");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
