package com.kang.cashbook.skin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.view.View;

/**
 * Created by kangren on 2018/2/26.
 */

public class SkinRaw
{

    private Skin.SkinType skinType;

    /**
     * 识别View的tag
     */
    private String tag;

    /**
     * 缓存raw资源目录
     */
    private String cachePath;

    private String rawUri;

    public SkinRaw(View view, Skin.SkinType rawType, String tag)
    {
        this.skinType = rawType;
        this.tag = tag;
        cachePath = view.getContext().getCacheDir().getAbsolutePath();
        view.invalidate();
    }

    public void onSkinAttached(Resources skinRes, String skinPkg)
    {
        // 获取皮肤包中对应Tag的资源id
        int resId = skinRes.getIdentifier(tag, Skin.ResType.string + "", skinPkg);
        // 找不到tag对应的字符串资源
        if (resId == 0)
        {
            return;
        }
        // 获取resId对应的字符串，即gif文件名
        String resName = skinRes.getString(resId);
        String path = cachePath + File.separator + resName + ".gif";
        int rawId = skinRes.getIdentifier(resName, SkinSetter.skinType2ResType(skinType) + "", skinPkg);
        InputStream inputStream = skinRes.openRawResource(rawId);
        // 将gif文件存入cache中
        readInputStream(path, inputStream);

        rawUri = "file://" + path;
    }

    public void onSkinDetached()
    {

    }

    public String getRawUri(String tag)
    {
        if (this.tag.equals(tag))
        {
            return rawUri;
        }
        return null;
    }

    /**
     * 将res/raw中的文件存入其它目录
     * 
     * @param path 目标文件路径
     * @param inputStream raw产生的InputStream流
     */
    private void readInputStream(String path, InputStream inputStream)
    {
        File file = new File(path);
        // 如果文件存在，则删除
        if (file.exists())
        {
            file.delete();
        }
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[inputStream.available()];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1)
            {
                fos.write(buffer, 0, length);
            }
            // 刷新缓存区
            fos.flush();
            // 关闭流
            fos.close();
            inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
