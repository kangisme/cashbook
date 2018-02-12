package com.kangren.cashbook.skin.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kangren on 2018/2/12.
 */

public class TemplateManager
{
    public static final int FLIPPER_TEMPLATE = 1;

    public static RecyclerView.ViewHolder getViewHolder(View itemView, int viewType)
    {
        switch (viewType)
        {
            case FLIPPER_TEMPLATE:
                return new FlipperTemplateHolder(itemView);
        }
        return null;
    }

    public static BaseView getTemplateView(Context context, int viewType)
    {
        switch (viewType)
        {
            case FLIPPER_TEMPLATE:
                return new FlipperTemplate(context, viewType);
        }
        return null;
    }

}
