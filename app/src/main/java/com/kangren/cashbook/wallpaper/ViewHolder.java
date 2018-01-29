package com.kangren.cashbook.wallpaper;

import com.kangren.cashbook.R;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by kangren on 2018/1/29.
 */

public class ViewHolder extends RecyclerView.ViewHolder
{
    public ImageView imageView;

    public ViewHolder(View itemView)
    {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.recycler_item_img);
    }
}
