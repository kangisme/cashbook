package com.kang.cashbook.skin;

import com.kang.cashbook.skin.Skin.ResType;
import com.kang.cashbook.skin.Skin.SkinType;
import com.orhanobut.logger.Logger;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kangren on 2018/2/7.
 */

public class SkinSetter implements Setter
{

    private View view;

    /**
     * 皮肤类型
     */
    private Skin.SkinType skinType;

    /**
     * 资源类型
     */
    private Skin.ResType resType;

    /**
     * 资源id，本地资源id，非皮肤包资源id
     */
    private int resId;

    /**
     * 资源名称
     */
    private String resName;

    /**
     * 资源是否是个选择器
     */
    private boolean isSelector;

    /**
     * 皮肤使用状态
     */
    private int status = Skin.Status.REGISTERED;

    public SkinSetter(View view, Skin.SkinType type, int resId)
    {
        this.view = view;
        this.skinType = type;
        if (resId > 0)
        {
            Resources resources = view.getResources();
            resType = Skin.ResType.valueOf(resources.getResourceTypeName(resId));
            resName = resources.getResourceEntryName(resId);
        }
        this.resId = resId;
        if (SkinManager.getInstance(view.getContext()).isSkinInited())
        {
            status = Skin.Status.READY;
        }
    }

    public SkinSetter(View view, Skin.SkinType skinType, String resName)
    {
        this.view = view;
        this.skinType = skinType;
        this.resType = skinType2ResType(skinType);
        this.resName = resName;
        if (resType != null)
        {
            this.resId = getResourceId(view.getContext(), resType.name(), resName);
        }
        if (SkinManager.getInstance(view.getContext()).isSkinInited())
        {
            status = Skin.Status.READY;
        }
    }

    /**
     * 为某个控件直接设置皮肤
     *
     * @param view view
     * @param res 资源
     * @param resId 资源id
     * @param isSelector 资源是否是个selector
     * @return 皮肤是否设置成功
     */
    @SuppressWarnings("deprecation")
    public static boolean setSkin(View view, Resources res, int resId, SkinType skinType, boolean isSelector)
    {
        try
        {
            return setRes(view, res, resId, skinType, isSelector, false);
        }
        catch (Exception e)
        {
            Logger.e("setSkin error" + e);
        }
        return false;
    }

    /**
     * 为某个控件直接设置资源
     *
     * @param view view
     * @param res 资源
     * @param resId 资源id
     * @param selector 资源是否是个selector
     * @param isLocal 是否是本地资源
     * @return 是否设置成功
     */
    @SuppressWarnings("deprecation")
    private static boolean setRes(View view, Resources res, int resId, SkinType type, boolean selector, boolean isLocal)
    {
        if (view == null || res == null || type == null || resId <= 0)
        {
            return false;
        }
        switch (type)
        {
            case bgColor: // 背景色
                // 如果是本地资源，直接使用id就行
                if (isLocal)
                {
                    view.setBackgroundColor(resId);
                    break;
                }
                // 不是本地资源，必须获取资源才行
                if (selector)
                {
                    ColorStateList stateList = res.getColorStateList(resId);
                    if (stateList == null)
                    {
                        break;
                    }
                    StateListDrawable bg = new StateListDrawable();
                    // 获取默认颜色
                    int defaultColor = stateList.getDefaultColor();
                    bg.addState(new int[] {}, new ColorDrawable(defaultColor));
                    // 选中色
                    int[] selectSet = {android.R.attr.state_selected};
                    bg.addState(selectSet, new ColorDrawable(stateList.getColorForState(selectSet, defaultColor)));
                    // 按下色
                    selectSet = new int[] {android.R.attr.state_pressed};
                    bg.addState(selectSet, new ColorDrawable(stateList.getColorForState(selectSet, defaultColor)));
                    // checked色
                    selectSet = new int[] {android.R.attr.state_checked};
                    bg.addState(selectSet, new ColorDrawable(stateList.getColorForState(selectSet, defaultColor)));
                    view.setBackgroundDrawable(bg);
                    break;
                }
                view.setBackgroundColor(res.getColor(resId));
                break;
            case bgDrawable: // 背景图
                // 如果是本地资源，直接使用id就行
                if (isLocal)
                {
                    view.setBackgroundResource(resId);
                    break;
                }
                // 不是本地资源，必须获取资源才行
                view.setBackgroundDrawable(res.getDrawable(resId));
                break;
            case srcDrawable: // 资源图
                if (!(view instanceof ImageView))
                {
                    return false;
                }
                // 如果是本地资源，直接使用id就行
                if (isLocal)
                {
                    ((ImageView) view).setImageResource(resId);
                    break;
                }
                // 不是本地资源，必须获取资源才行
                ((ImageView) view).setImageDrawable(res.getDrawable(resId));
                break;
            case textColor: // 字体颜色
                if (!(view instanceof TextView))
                {
                    return false;
                }
                if (selector)
                {
                    ((TextView) view).setTextColor(res.getColorStateList(resId));
                }
                else
                {
                    ((TextView) view).setTextColor(res.getColor(resId));
                }
                break;
            case text: // 文案
                if (!(view instanceof TextView))
                {
                    return false;
                }
                // 如果是本地资源，直接使用id就行
                if (isLocal)
                {
                    ((TextView) view).setText(resId);
                    break;
                }
                // 不是本地资源，必须获取资源才行
                ((TextView) view).setText(res.getString(resId));
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * 获取资源id, ** 注意： 返回的是当前应用中的资源id,而不是皮肤包中的资源id **
     *
     * @param context context
     * @param resType 资源类型
     * @param resName 资源名称
     * @return 资源id ; ** 注意： 返回的是当前应用中的资源id,而不是皮肤包中的资源id **
     */
    private static int getResourceId(Context context, String resType, String resName)
    {
        if (context == null || TextUtils.isEmpty(resType) || TextUtils.isEmpty(resName))
        {
            return -1;
        }
        try
        {
            Resources resources = context.getResources();
            return resources.getIdentifier(resName, resType, context.getPackageName());
        }
        catch (Exception e)
        {
            Logger.e("getResources error" + e);
        }
        return -1;
    }

    /**
     * 根据皮肤类型获取资源类型
     *
     * @param skinType 皮肤类型
     * @return 资源类型
     */
    public static ResType skinType2ResType(SkinType skinType)
    {
        if (skinType == null)
        {
            return null;
        }
        switch (skinType)
        {
            case textColor: // 字体颜色
            case bgColor: // 背景色
                return ResType.color;
            case bgDrawable:// 背景图
            case srcDrawable:// 资源图
                return ResType.drawable;
            case text: // 文案
                return ResType.string;
            case gif: // 动画
                return ResType.raw;
        }
        return null;
    }

    @Override
    public void onSkinAttached(Resources skinRes, String skinPkg)
    {
        if (skinRes == null || TextUtils.isEmpty(skinPkg))
        {
            return;
        }
        // 获取皮肤包中相应资源的资源id
        int resId = skinRes.getIdentifier(resName, resType + "", skinPkg);
        boolean success = onSetSkin(skinRes, resId);
        // 如果设置皮肤成功
        if (success)
        {
            status = Skin.Status.SHOWING;
        }
        else
        {
            // 如果皮肤没有设置成功，设置为默认值
            onSkinDetached();
        }
    }

    @Override
    public void onSkinDetached()
    {
        status = Skin.Status.READY;
        // 如果没有默认资源
        if (resId <= 0)
        {
            return;
        }
        // 还原默认资源
        try
        {
            setRes(view, view.getResources(), this.resId, skinType, isSelector, true);
        }
        catch (Exception e)
        {
            Logger.e("setRes error" + e);
        }
    }

    @Override
    public int getSkinStatus(View v)
    {
        if (v != view || v == null)
        {
            return Skin.Status.UNKNOWN;
        }
        return status;
    }

    /**
     * 资源是一个selector
     */
    public SkinSetter isSelector()
    {
        isSelector = true;
        return this;
    }

    /**
     * 设置皮肤
     *
     * @param res 皮肤资源
     * @param resId 皮肤资源id
     */
    protected boolean onSetSkin(Resources res, int resId)
    {
        // 设置皮肤
        return setSkin(view, res, resId, skinType, isSelector);
    }
}
