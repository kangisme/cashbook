package com.kang.cashbook.skin;

import android.content.res.Resources;
import android.view.View;

/**
 * Created by kangren on 2018/2/7.
 */

public interface Setter
{
    /**
     * 皮肤包加载ok
     *
     * @param skinRes 皮肤包资源
     * @param skinPkg 皮肤包包名
     */
    void onSkinAttached(Resources skinRes, String skinPkg);

    /**
     * 皮肤资源被卸载了
     */
    void onSkinDetached();

    /**
     * 获取某个View的皮肤使用状态
     *
     * @param view View
     * @return 状态
     * @see Skin.Status
     */
    int getSkinStatus(View view);
}
