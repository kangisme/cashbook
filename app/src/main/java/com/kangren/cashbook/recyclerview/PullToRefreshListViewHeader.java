/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.kangren.cashbook.recyclerview;

import com.kangren.cashbook.R;
import com.kangren.cashbook.recyclerview.view.PlayerProgress;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PullToRefreshListViewHeader extends LinearLayout implements IPullToRefreshListViewHeader
{
    public final static int STATE_NORMAL = 0;

    public final static int STATE_READY = 1;

    public final static int STATE_REFRESHING = 2;

    private final int ROTATE_ANIM_DURATION = 180;

    private LinearLayout mContainer;

    private View content;

    private ImageView mArrowImageView;

    private PlayerProgress mProgressBar;

    private TextView mHintTextView;

    private TextView mTimeView;

    private int mState = STATE_NORMAL;

    private State state = State.NORMAL;

    private Animation mRotateUpAnim;

    private Animation mRotateDownAnim;

    private int contentHeight;

    public PullToRefreshListViewHeader(Context context)
    {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public PullToRefreshListViewHeader(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }

    protected void initView(Context context)
    {
        // 初始情况，设置下拉刷新view高度为0
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.app_pull_listview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        content = findViewById(R.id.xlistview_header_content);
        mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
        mProgressBar = (PlayerProgress) findViewById(R.id.xlistview_header_progress);
        mTimeView = (TextView) findViewById(R.id.xlistview_header_time);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                contentHeight = content.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public void setState(int state)
    {
        if (state == mState)
            return;

        if (state == STATE_REFRESHING)
        { // 显示进度
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else
        { // 显示箭头图片
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state)
        {
            case STATE_NORMAL:
                if (mState == STATE_READY)
                {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING)
                {
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText("下拉刷新");
                break;
            case STATE_READY:
                if (mState != STATE_READY)
                {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText("松手后刷新");
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setText("正在刷新");
                break;
            default:
        }

        mState = state;
    }

    public int getVisiableHeight()
    {
        return mContainer.getHeight();
    }

    public void setVisiableHeight(int height)
    {
        if (height < 0)
            height = 0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    @Override
    public void setState(IPullToRefreshListViewHeader.State state)
    {
        if (state == this.state)
            return;

        if (state == IPullToRefreshListViewHeader.State.REFRESHING)
        { // 显示进度
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else
        { // 显示箭头图片
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state)
        {
            case NORMAL:
                if (this.state == IPullToRefreshListViewHeader.State.READY)
                {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (this.state == IPullToRefreshListViewHeader.State.REFRESHING)
                {
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText("下拉刷新");
                break;
            case READY:
                if (this.state != IPullToRefreshListViewHeader.State.READY)
                {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText("松手后刷新");
                }
                break;
            case REFRESHING:
                mHintTextView.setText("正在刷新");
                break;
            default:
        }

        this.state = state;
    }

    @Override
    public void setHeight(int height)
    {
        if (height < 0)
            height = 0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    @Override
    public int getContentHeight()
    {
        return contentHeight;
    }

    @Override
    public boolean isVisible()
    {
        return getHeight() > 0;
    }

    @Override
    public boolean isFullyShow()
    {
        return getHeight() >= getContentHeight();
    }

    @Override
    public View getView()
    {
        return this;
    }

    @Override
    public TextView getTimeView()
    {
        return mTimeView;
    }

    @Override
    public void release()
    {

    }

    @Override
    public boolean needWaitForAnimation()
    {
        return false;
    }

    @Override
    public void setOnAnimationStopListener(OnAnimationStopListener listener)
    {

    }

    @Override
    public void setBackgroundImageUrl(String url, int defaultId)
    {

    }

    @Override
    public void setHeaderBackground(int color)
    {

    }
}
