package com.kangren.cashbook.common.recyclerview;

import com.kangren.cashbook.R;
import com.kangren.cashbook.common.listview.IPullToRefreshListViewHeader;
import com.kangren.cashbook.common.listview.PTRGifHeader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by kangren on 2018/3/19.
 */

public class XRecyclerView extends RecyclerView
{

    private Context mContext;

    // used for scroll back
    private Scroller mScroller;

    // -- header view
    private IPullToRefreshListViewHeader mHeaderView;

    public XRecyclerView(Context context)
    {
        super(context);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        return super.onTouchEvent(e);
    }

    private void initWithContext(Context context)
    {
        mContext = context;
        mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(new OnScrollListener()
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
        });

        mHeaderView = new PTRGifHeader(context);
        ((PTRGifHeader) mHeaderView).setGifImage(R.raw.loading_gif);

    }

}
