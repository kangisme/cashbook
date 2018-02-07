package com.kang.cashbook.skin;

import java.io.File;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by kangren on 2018/2/7.
 */

public class SkinManager
{
    /**
     * 单例类引用
     */
    private static SkinManager mInstance;

    /**
     * 皮肤资源
     */
    public Resources res;

    /**
     * 皮肤包包名
     */
    public String pkg;

    /**
     * 皮肤资源apk路径
     */
    public String apk;

    /**
     * context
     */
    private Context mContext;

    /**
     * 皮肤有效时间
     */
    private long mValidateTime = 0;

    /**
     * 皮肤是否有效
     */
    private boolean isValidate;

    private SkinManager(Context context)
    {
        mContext = context.getApplicationContext();
    }

    public static SkinManager getInstance(Context context)
    {
        if (mInstance == null)
        {
            synchronized (SkinManager.class)
            {
                if (mInstance == null)
                {
                    mInstance = new SkinManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取皮肤包信息
     *
     * @param apkPath apk文件路径
     * @return 皮肤资源
     */
    private static PackageInfo getSkinPackageInfo(Context outContext, String apkPath)
    {
        try
        {
            // 获取packageInfo
            return outContext.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        }
        catch (Throwable e)
        {
            Log.d("theme", "getSkinPackageInfo error" + e);
        }
        return null;
    }

    /**
     * 获取资源资源
     *
     * @param outContext 外部资源Context
     * @param apkPath apk文件路径
     * @return 皮肤资源
     */
    private static Resources getSkinResources(Context outContext, String apkPath)
    {
        try
        {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkPath);
            DisplayMetrics metrics = outContext.getResources().getDisplayMetrics();
            Configuration con = outContext.getResources().getConfiguration();
            return new Resources(assetManager, metrics, con);
        }
        catch (Throwable e)
        {
            Log.d("theme", "getSkinResources error" + e);
        }
        return null;
    }

    /**
     * 皮肤资源是否准备好
     *
     * @return 是否准备好
     */
    public boolean isSkinInited()
    {
        return res != null && !TextUtils.isEmpty(pkg);
    }

    /**
     * 皮肤信息是否有效
     *
     * @return 是否有效
     */
    public boolean isValidate()
    {
        return isValidate;
    }

    /**
     * 强制清除, 只能在退出应用的时候调用
     */
    public void clearWhatever()
    {
        res = null;
        pkg = null;
        apk = null;
        mValidateTime = 0;
        isValidate = false;
    }

    /**
     * 清理过期的资源
     */
    public void clearIfNeeded()
    {
        // 如果情况
        if (!isValidate)
        {
            res = null;
            pkg = null;
            apk = null;
            mValidateTime = 0;
        }
    }

    /**
     * 替换皮肤源（只有在当前没有使用皮肤或当前皮肤过期时有效）
     *
     * @param info 皮肤apk信息
     */
    public void changeSkinSource(SkinInfo info)
    {
        // 无条件修改当前皮肤有效期
        isValidate = isSkinInited() && mValidateTime > System.currentTimeMillis();
        // 如果当前在使用的皮肤有效, 或者要设置的皮肤已经过期
        if (isValidate || info == null || TextUtils.isEmpty(info.path) || info.end < System.currentTimeMillis())
        {
            return;
        }
        // 如果路径没变
        if (info.path.equals(apk))
        {
            // 只更新有效时间
            mValidateTime = info.end;
            isValidate = true;
            return;
        }
        // 清除数据
        clearIfNeeded();
        // 重新设置
        setSkin(info);
    }

    /**
     * 设置皮肤
     *
     * @param info 皮肤apk信息
     */
    private void setSkin(SkinInfo info)
    {
        // 一些参数预检测
        if (mContext == null || info == null || TextUtils.isEmpty(info.path) || info.end < System.currentTimeMillis())
        {
            return;
        }

        File f = new File(info.path);
        if (!f.exists() || f.isDirectory() || !f.canRead())
        {
            return;
        }
        // 获取皮肤包信息
        PackageInfo packageInfo = getSkinPackageInfo(mContext, info.path);
        // 获取皮肤资源信息
        Resources resources = getSkinResources(mContext, info.path);
        if (packageInfo != null && resources != null)
        {
            res = resources;
            pkg = packageInfo.packageName;
            apk = info.path;
            mValidateTime = info.end;
            isValidate = true;
        }
    }
}
