package com.kang.cashbook.imageloader;

import android.view.View;

/**
 * 图片加载完后回调
 */
public interface OnLoadingCompleteListener
{
    /**
     * @param ok 加载是否成功
     * @param view 当前view
     * @param animPeriod 动画时长，非动图默认为0
     */
    void onResult(boolean ok, View view, int animPeriod);
}
