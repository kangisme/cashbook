package com.kang.cashbook.skin;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kangren on 2018/2/6.
 */

public class Skin
{
    private final List<Setter> setters;

    private final List<SkinRaw> raws;

    public Skin()
    {
        this.setters = new ArrayList<Setter>();
        this.raws = new ArrayList<>();
    }

    /**
     * 为某个控件直接设置皮肤，使用于ListView 的Item。可以在 adapter的getView方法中使用
     *
     * @param view view
     * @param resName 资源名称
     * @param isSelector 资源是否是个selector
     * @return 皮肤是否设置成功
     */
    @SuppressWarnings("deprecation")
    public static boolean set(View view, String resName, SkinType skinType, boolean isSelector)
    {
        if (view == null || view.getContext() == null)
        {
            return false;
        }
        SkinManager manager = SkinManager.getInstance(view.getContext());
        if (manager == null || !manager.isSkinInited())
        {
            return false;
        }
        // 获取皮肤包中相应资源的资源id
        int resId = manager.res.getIdentifier(resName, SkinSetter.skinType2ResType(skinType) + "", manager.pkg);
        // 设置皮肤
        return resId > 0 && SkinSetter.setSkin(view, manager.res, resId, skinType, isSelector);
    }

    /**
     * 获取皮肤资源 manager
     *
     * @param context context
     * @return 皮肤资源 manager
     */
    private SkinManager getSkinManager(Context context)
    {
        SkinManager manager = SkinManager.getInstance(context);
        if (manager == null || !manager.isSkinInited())
        {
            return null;
        }
        return manager;
    }

    /**
     * 如果可以, 皮肤替换
     *
     * @param context context
     */
    public void updateSkinIfNeeded(Context context)
    {
        SkinManager manager = getSkinManager(context);
        if (manager == null)
        {
            return;
        }
        // 设置皮肤
        for (Setter setter : setters)
        {
            if (setter == null)
            {
                continue;
            }
            if (manager.isValidate())
            {
                // 资源 attached
                setter.onSkinAttached(manager.res, manager.pkg);
            }
            // 如果皮肤资源过期了
            else
            {
                // 资源 detached
                setter.onSkinDetached();
            }
        }
        for (SkinRaw raw : raws)
        {
            if (raw == null)
            {
                continue;
            }
            if (manager.isValidate())
            {
                raw.onSkinAttached(manager.res, manager.pkg);
            }
            else
            {
                raw.onSkinDetached();
            }
        }
    }

    /**
     * 某个View的皮肤状态
     *
     * @param v view
     * @return 皮肤状态
     */
    public int getSkinStatus(View v)
    {
        if (v == null || v.getContext() == null)
        {
            return Status.UNKNOWN;
        }
        for (Setter setter : setters)
        {
            if (setter == null)
            {
                continue;
            }
            int status = setter.getSkinStatus(v);
            if (status > Status.UNKNOWN)
            {
                return status;
            }
        }
        return Status.UNKNOWN;
    }

    /**
     * 销毁数据
     */
    public void destroy()
    {
        setters.clear();
        raws.clear();
    }

    /**
     * 添加一个String 主题
     *
     * @param view view
     * @param resId string资源id
     * @return this
     */
    public Skin text(TextView view, int resId)
    {
        return add(new SkinSetter(view, SkinType.text, resId));
    }

    /**
     * 添加一个textColor主题item
     *
     * @param view view
     * @param resId color资源id
     * @param isSelector color资源是否是个selector
     * @return this
     */
    public Skin textColor(TextView view, int resId, boolean isSelector)
    {
        SkinSetter setter = new SkinSetter(view, SkinType.textColor, resId);
        return add(isSelector ? setter.isSelector() : setter);
    }

    /**
     * 添加一个背景color主题item
     *
     * @param view view
     * @param resId color资源id
     * @param isSelector color资源是否是个selector
     * @return this
     */
    @SuppressWarnings("unused")
    public Skin bgColor(View view, int resId, boolean isSelector)
    {
        SkinSetter setter = new SkinSetter(view, SkinType.bgColor, resId);
        return add(isSelector ? setter.isSelector() : setter);
    }

    /**
     * 添加一个背景图片主题item
     *
     * @param view view
     * @param resId drawable资源id
     * @param isSelector drawable资源是否是个selector
     * @return this
     */
    public Skin bgDrawable(View view, int resId, boolean isSelector)
    {
        SkinSetter setter = new SkinSetter(view, SkinType.bgDrawable, resId);
        return add(isSelector ? setter.isSelector() : setter);
    }

    /**
     * 添加一个Src图片主题item
     *
     * @param view view
     * @param resId drawable资源id
     * @param isSelector drawable资源是否是个selector
     * @return this
     */
    public Skin srcDrawable(ImageView view, int resId, boolean isSelector)
    {
        SkinSetter setter = new SkinSetter(view, SkinType.srcDrawable, resId);
        return add(isSelector ? setter.isSelector() : setter);
    }

    /**
     * 添加一个Src图片主题item
     *
     * @param view view
     * @param resName drawable资源名称
     * @return this
     */
    public Skin srcDrawable(ImageView view, String resName)
    {
        return add(new SkinSetter(view, SkinType.srcDrawable, resName));
    }

    /**
     * 添加一个raw类型皮肤
     *
     * @param view view
     * @param rawType raw资源类型
     * @param tag Tab标签
     * @return this
     */
    public Skin raw(View view, SkinType rawType, String tag)
    {
        return add(new SkinRaw(view, rawType, tag));
    }

    /**
     * 获取raw资源对应的Uri
     * 
     * @param view tabView
     * @return String类型uri
     */
    public String getRawUri(View view)
    {
        if (view == null || view.getTag() == null)
        {
            return null;
        }
        for (SkinRaw raw : raws)
        {
            if (raw == null)
            {
                continue;
            }
            String tag = (String) view.getTag();
            String uri = raw.getRawUri(tag);
            if (uri != null)
            {
                return uri;
            }
        }
        return null;
    }

    /**
     * 添加setter
     *
     * @param setter setter
     * @return this
     */
    public Skin add(Setter setter)
    {
        if (setter != null)
        {
            setters.add(setter);
        }
        return this;
    }

    /**
     * 添加gif
     * 
     * @param gif gif
     * @return this
     */
    public Skin add(SkinRaw gif)
    {
        if (gif != null)
        {
            raws.add(gif);
        }
        return this;
    }

    /**
     * 皮肤资源类型
     */
    public enum ResType
    {
        id, string, layout, anim, style, drawable, dimen, color, array, raw
    }

    /**
     * 皮肤类型
     */
    public enum SkinType
    {
        bgColor, bgDrawable, srcDrawable, textColor, text, gif
    }

    /**
     * View 使用皮肤的几个状态
     */
    public static final class Status
    {
        /**
         * 未注册皮肤
         */
        public static final int UNKNOWN = -1;

        /**
         * 已经注册，没有皮肤资源
         */
        public static final int REGISTERED = 0;

        /**
         * 已经注册，有皮肤资源，但还没有显示
         */
        public static final int READY = 1;

        /**
         * 正在显示皮肤
         */
        public static final int SHOWING = 2;

    }
}
