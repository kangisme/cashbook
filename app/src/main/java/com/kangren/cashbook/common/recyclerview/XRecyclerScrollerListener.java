package com.kangren.cashbook.common.recyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * Created by kangren on 2018/3/19.
 */

public abstract class XRecyclerScrollerListener extends RecyclerView.OnScrollListener
{
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState)
    {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);
    }

    public abstract void onLoadMore();
}
