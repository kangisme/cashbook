package com.kangren.cashbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.orhanobut.logger.Logger;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

/**
 * Created by kangren on 2018/2/28.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler
{

    /**
     * 单例
     */
    public static CrashHandler instance;

    /**
     * Log信息存储地址
     */
    private static String LOG_PATH;

    private Context mContext;

    /**
     * 收集错误信息
     */
    private Map<String, String> mInfoMap = new HashMap<>();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 系统默认的UncaughtExceptionHandler
     */
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public static CrashHandler getInstance()
    {
        if (instance == null)
        {
            synchronized (CrashHandler.class)
            {
                if (instance == null)
                {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    public void init(Context context)
    {
        this.mContext = context;
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        // 如果外存卡可以读写，放在外部存储器，否则放在内部存储器上
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            LOG_PATH = mContext.getExternalFilesDir("CARCH_LOG").getPath();
        }
        else
        {
            LOG_PATH = mContext.getFilesDir().getPath();
        }

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件至本地
        postService(saveCrashLog(ex));

        // 默认处理
        if (!BuildConfig.DEBUG)
        {
            uncaughtExceptionHandler.uncaughtException(thread, ex);
            return;
        }

        MyApplication.getApplication().exitApp();
    }

    /**
     * 发送错误日志至服务端
     *
     * @param fileName log路径
     */
    private void postService(String fileName)
    {
        if (fileName != null)
        {
            // 发送崩溃日志至服务器
        }
    }

    /**
     * 收集崩溃设备参数信息
     */
    public void collectDeviceInfo(Context context)
    {
        try
        {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null)
            {
                String appName = packageInfo.applicationInfo.packageName;
                String versionName = packageInfo.versionName + "";
                String versionCode = packageInfo.versionCode + "";
                mInfoMap.put("包名", appName);
                mInfoMap.put("版本名", versionName);
                mInfoMap.put("版本号", versionCode);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Logger.e("收集设备信息出错" + e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields)
        {
            try
            {
                field.setAccessible(true);
                mInfoMap.put(field.getName(), field.get(null).toString());
            }
            catch (Exception e)
            {
                Logger.e("收集崩溃日志出错", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称
     */
    private String saveCrashLog(Throwable ex)
    {

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : mInfoMap.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuilder.append(key + " = " + value + "\n");
        }
        // 异常写入stringBuilder
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        // 异常原因写入stringBuilder
        Throwable cause = ex.getCause();
        if (cause != null)
        {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        String result = writer.toString();
        stringBuilder.append(result);
        try
        {
            String time = simpleDateFormat.format(new Date(System.currentTimeMillis()));
            String fileName = time + ".txt";
            File dir = new File(LOG_PATH);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(dir.getAbsolutePath() + File.separator + fileName);
            fos.write(stringBuilder.toString().getBytes());
            fos.close();
            return fileName;
        }
        catch (Exception e)
        {
            Logger.e("写入文件异常", e);
        }
        return null;
    }
}
