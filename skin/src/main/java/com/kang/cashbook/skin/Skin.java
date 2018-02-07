package com.kang.cashbook.skin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangren on 2018/2/6.
 */

public class Skin
{
    public static final String SKIN_STRING = "SKIN MODULE";

    /**
     * setters
     */
    private final List<Setter> setters;

    public Skin()
    {
        this.setters = new ArrayList<Setter>();
    }

    /**
     * 皮肤资源类型
     */
    public enum ResType
    {
        id, string, layout, anim, style, drawable, dimen, color, array
    }

    /**
     * 皮肤类型
     */
    public enum SkinType
    {
        bgColor, bgDrawable, srcDrawable, textColor, text
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
