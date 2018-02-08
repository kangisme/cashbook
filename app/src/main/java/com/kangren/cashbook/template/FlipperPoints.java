package com.kangren.cashbook.template;

import com.kangren.cashbook.R;
import com.kangren.cashbook.util.Utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by kangren on 2018/2/8.
 */

public class FlipperPoints extends LinearLayout
{

    public static int DEFAULT_FLIPPER_POINT = 0;

    public FlipperPoints(Context context)
    {
        super(context);
    }

    public FlipperPoints(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    public void addPoints(int size)
    {
        addPoints(size, DEFAULT_FLIPPER_POINT);
    }

    public void addPoints(int size, int type)
    {
        if (type == DEFAULT_FLIPPER_POINT)
        {
            addPoints(size, R.drawable.flipper_point_bg, 4, 4, 4);
        }
    }

    private void addPoints(int size, int imageId, int width, int height, int margin)
    {
        removeAllViews();

        if (size <= 0)
        {
            return;
        }

        ImageView imageView;
        for (int i = 0; i < size; i++)
        {
            imageView = new ImageView(getContext());

            imageView.setBackgroundResource(imageId);
            imageView.setEnabled(false);
            addView(imageView, Utils.dp2px(getContext(), width), Utils.dp2px(getContext(), height));

            LinearLayout.LayoutParams params = (LayoutParams) imageView.getLayoutParams();
            if (i == size - 1)
            {
                params.setMargins(0, 0, 0, 0);
            }
            else
            {
                params.setMargins(0, 0, Utils.dp2px(getContext(), margin), 0);
            }

        }
    }

}
