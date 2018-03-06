/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.kangren.cashbook.recyclerview;

import com.kangren.cashbook.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PullToRefreshListViewFooter extends LinearLayout
{
    public final static int STATE_NORMAL = 0;

    public final static int STATE_READY = 1;

    public final static int STATE_LOADING = 2;

    public final static int STATE_NO_MORE_DATA = 3;

    private Context mContext;

    private View mContentView;

    private View mProgressBar;

    private TextView mHintView;

    public PullToRefreshListViewFooter(Context context)
    {
        super(context);
        initView(context);
    }

    public PullToRefreshListViewFooter(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }

    public void setState(int state)
    {
        if (state == STATE_NORMAL)
        {// 正常时候，隐藏foor
            mContentView.setVisibility(View.INVISIBLE);
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText("松开载入更多");
        }
        else if (state == STATE_LOADING)
        {
            mContentView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText("正在加载更多");
        }
        else if (state == STATE_NO_MORE_DATA)
        {
            mContentView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText("没有更多数据");
        }
    }

    public void setGoneOrVisible(int visible)
    {
        mContentView.setVisibility(visible);
    }

    public int getBottomMargin()
    {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        return lp.bottomMargin;
    }

    public void setBottomMargin(int height)
    {
        if (height < 0)
            return;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    /**
     * normal status
     */
    public void normal()
    {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading()
    {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide()
    {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show()
    {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    private void initView(Context context)
    {
        mContext = context;
        RelativeLayout moreView = (RelativeLayout) LayoutInflater.from(mContext).inflate(
                R.layout.app_pull_listview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
        mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
    }

}
