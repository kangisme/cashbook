/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.kangren.cashbook.recyclerview;

import java.text.SimpleDateFormat;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kangren.cashbook.R;
import com.orhanobut.logger.Logger;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

public class PullToRefreshListView extends ListView implements OnScrollListener
{
    private static final int MSG_STOP_REFRESH = 1;

    private final static int SCROLLBACK_HEADER = 0;

    private final static int SCROLLBACK_FOOTER = 1;

    // private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >=
    // 50px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull

    private static final int LAST_REFRESH_TIME = 800;

    protected boolean mEnablePullRefresh = true;

    boolean pauseOnScroll = false; // or true

    boolean pauseOnFling = true; // or false

    private float mLastY = -1; // save event y

    // header view content, use it to calculate the Header's height. And hide
    // it
    // when disable pull refresh.
    // private RelativeLayout mHeaderViewContent;
    private Scroller mScroller; // used for scroll back

    // private int mHeaderViewHeight; // header view's height
    private OnScrollListener mScrollListener; // user's scroll listener
    // the interface to trigger refresh and load more.

    private PullAndRefreshListViewListener pullAndRefreshListViewListener;

    private PullAndRefreshCompleteListener pullAndRefreshCompleteListener;

    // -- header view
    private IPullToRefreshListViewHeader mHeaderView;

    private TextView mHeaderTimeView;

    private boolean mPullRefreshing = false; // is refreashing.

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");

    // -- footer view
    private PullToRefreshListViewFooter mFooterView;

    private boolean mEnablePullLoad;

    private boolean mPullLoading;

    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // feature.
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;

    private long refreshTime = 0;

    private Context context = null;

    private String timeTag = "";

    private int totalItemCount = 1000;

    private Handler delayStopHandler;

    /***
     * 列表飞驰过程中是否停止加载图片(如果界面内有使用CommentTextView加载gif表情，则必须设置为false)
     */
    private boolean isFresoStopAtFling = true;

    /**
     * @param context
     */
    public PullToRefreshListView(Context context)
    {
        super(context);
        initWithContext(context);
    }

    // public void setTimeTag(String timeTag)
    // {
    // this.timeTag = timeTag;
    // if (mHeaderTimeView != null)
    // {
    // String last = getLastRefreshTime();
    // mHeaderTimeView.setText(last);
    // }
    // }

    public PullToRefreshListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initWithContext(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    public String getTimeTag()
    {
        return timeTag;
    }

    public int getHeaderHeight()
    {
        return mHeaderView == null ? 0 : mHeaderView.getHeight();
    }

    private void initWithContext(Context context)
    {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        // PauseOnScrollListener listener = new
        // PauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll,
        // pauseOnFling, this);

        super.setOnScrollListener(this);
        this.context = context;

        mHeaderView = new PTRGifHeader(context);
        ((PTRGifHeader) mHeaderView).setGifImage(R.raw.loading_gif);
        // init header view
        initHeaderView();

        // init footer view
        mFooterView = new PullToRefreshListViewFooter(context);
        mFooterView.hide();

        delayStopHandler = new DelayStopHandler();
    }

    private void initHeaderView()
    {
        // init header view
        /*
         * mHeaderViewContent = (RelativeLayout)
         * mHeaderView.findViewById(R.id.xlistview_header_content);
         * mHeaderTimeView = (TextView)
         * mHeaderView.findViewById(R.id.xlistview_header_time);
         * addHeaderView(mHeaderView);
         */
        mHeaderTimeView = mHeaderView.getTimeView();
        addHeaderView(mHeaderView.getView());
        // init header height
        /*
         * mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new
         * OnGlobalLayoutListener() {
         * @Override public void onGlobalLayout() { mHeaderViewHeight =
         * mHeaderViewContent.getHeight();
         * getViewTreeObserver().removeGlobalOnLayoutListener(this); } });
         */
    }

    public void setHeaderView(IPullToRefreshListViewHeader headerView)
    {
        if (headerView == null)
        {
            return;
        }

        mHeaderView.release();

        removeHeaderView(mHeaderView.getView());
        mHeaderView = headerView;
        initHeaderView();
    }

    @Override
    public void setAdapter(ListAdapter adapter)
    {
        // make sure XListViewFooter is the last footer view, and only add
        // once.
        if (mIsFooterReady == false)
        {
            mIsFooterReady = true;
            addFooterView(mFooterView);
        }
        super.setAdapter(adapter);
    }

    /**
     * enable or disable pull down refresh feature.
     * 
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable)
    {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh)
        { // disable, hide the content
          // mHeaderViewContent.setVisibility(View.INVISIBLE);
            mHeaderView.getView().setVisibility(INVISIBLE);
        }
        else
        {
            // mHeaderViewContent.setVisibility(View.VISIBLE);
            mHeaderView.getView().setVisibility(VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     * 
     * @param enable
     */
    public void setPullLoadEnable(boolean enable)
    {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad)
        {
            mFooterView.hide();
        }
        else
        {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(PullToRefreshListViewFooter.STATE_NORMAL);
        }
    }

    /**
     * make footer display "no more data" and disable pull up load more
     * feature.
     */
    public void setFooterNoMoreData()
    {
        mEnablePullLoad = false;
        mFooterView.show();
        mFooterView.setState(PullToRefreshListViewFooter.STATE_NO_MORE_DATA);
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh()
    {
        if (mPullRefreshing == true)
        {
            long times = System.currentTimeMillis() - refreshTime;
            if (times >= LAST_REFRESH_TIME)
            {
                stopRefreshInternal(false);
            }
            else
            {
                delayStopHandler.sendEmptyMessageDelayed(MSG_STOP_REFRESH, LAST_REFRESH_TIME - times);
            }
        }
    }

    /**
     * stopRefresh带有延迟效果，刷新动画的持续时间至少为LAST_REFRESH_TIME 中指定的时间，该方法会立即停止刷新
     */
    public void stopRefreshImmediately()
    {
        if (mPullRefreshing == true)
        {
            stopRefreshInternal(true);
        }

        // 如果之前调用了stopRefresh产生了一个延时的stop消息，去除这个消息
        delayStopHandler.removeMessages(MSG_STOP_REFRESH);
    }

    private void stopRefreshInternal(boolean immediately)
    {
        mHeaderView.setState(IPullToRefreshListViewHeader.State.STOP);
        if (!immediately && mHeaderView.needWaitForAnimation())
        {
            mHeaderView.setOnAnimationStopListener(new IPullToRefreshListViewHeader.OnAnimationStopListener()
            {
                @Override
                public void onAnimationStopped()
                {
                    mPullRefreshing = false;
                    resetHeaderHeight();
                    // changeRefreshTime();
                    if (pullAndRefreshCompleteListener != null)
                    {
                        pullAndRefreshCompleteListener.onRefreshComplete();
                    }
                }
            });
        }
        else
        {
            mPullRefreshing = false;
            resetHeaderHeight();
            // changeRefreshTime();
            if (pullAndRefreshCompleteListener != null)
            {
                pullAndRefreshCompleteListener.onRefreshComplete();
            }
        }
    }

    // private void changeRefreshTime()
    // {
    // // 2 重置刷新时间
    // setRefreshTime();
    // if (mHeaderTimeView != null)
    // {
    // String last = getLastRefreshTime();
    // mHeaderTimeView.setText(last);
    // }
    // }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore()
    {
        if (mPullLoading == true)
        {
            mPullLoading = false;
            mFooterView.hide();
            mFooterView.setState(PullToRefreshListViewFooter.STATE_NORMAL);
        }
    }

    private void invokeOnScrolling()
    {
        if (mScrollListener instanceof OnXScrollListener)
        {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    @Override
    protected void onDetachedFromWindow()
    {
        Logger.d("pulltorefresh: onDetachedFromWindow--- ");

        super.onDetachedFromWindow();

        if (Fresco.getImagePipeline().isPaused())
        {
            Fresco.getImagePipeline().resume();
        }

    }

    protected void updateHeaderHeight(float delta)
    {
        mHeaderView.setHeight((int) delta + mHeaderView.getHeight());

        if (mEnablePullRefresh && !mPullRefreshing)
        { // 未处于刷新状态，更新箭头
            if (mHeaderView.isFullyShow())
            {
                mHeaderView.setState(IPullToRefreshListViewHeader.State.READY);
            }
            else
            {
                mHeaderView.setState(IPullToRefreshListViewHeader.State.NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight()
    {
        int height = mHeaderView.getHeight();
        if (!mHeaderView.isVisible()) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && !mHeaderView.isFullyShow())
        {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && mHeaderView.isFullyShow())
        {
            finalHeight = mHeaderView.getContentHeight();
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, PTRGifHeader.SCROLL_DURATION);
        Logger.d("KL++++++++++test" + "height : " + height + "  " + "finalHeight :" + finalHeight);
        // trigger computeScroll
        invalidate();
    }

    public void showHeaderAndRefresh()
    {
        // 1 改变当前显示的时间，以便下一次显示
        if (mHeaderTimeView != null)
        {
            // String last = getLastRefreshTime();
            // mHeaderTimeView.setText(last);
        }
        mPullRefreshing = true;
        mHeaderView.setState(IPullToRefreshListViewHeader.State.REFRESHING);
        if (pullAndRefreshListViewListener != null)
        {
            refreshTime = System.currentTimeMillis();
            pullAndRefreshListViewListener.onRefresh();
        }
        mScroller.startScroll(0, 0, 0, mHeaderView.getContentHeight(), 1);
        invalidate();
    }

    public void refresh()
    {
        if (pullAndRefreshListViewListener != null)
        {
            refreshTime = System.currentTimeMillis();
            pullAndRefreshListViewListener.onRefresh();
        }
        if (Fresco.getImagePipeline().isPaused())
        {
            Fresco.getImagePipeline().resume();
        }
    }

    public void setHeaderBackground(String url, int defaultId)
    {
        if (mHeaderView != null)
        {
            mHeaderView.setBackgroundImageUrl(url, defaultId);
        }
    }

    private void startLoadMore()
    {
        mPullLoading = true;
        mFooterView.setState(PullToRefreshListViewFooter.STATE_LOADING);
        if (pullAndRefreshListViewListener != null)
        {
            mFooterView.show();
            pullAndRefreshListViewListener.onLoadMore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {

        if (mLastY == -1)
        {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (mHeaderView.isVisible() || deltaY > 0) && mEnablePullRefresh)
                {
                    // the first item is showing, header has shown or pull
                    // down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                }
                else if (getLastVisiblePosition() == mTotalItemCount - 2)
                {
                    // last item, already pulled up or want to pull up.
                    // updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0)
                {
                    // invoke refresh
                    if (!mPullRefreshing && mEnablePullRefresh && mHeaderView.isFullyShow())
                    {
                        mPullRefreshing = true;
                        mHeaderView.setState(IPullToRefreshListViewHeader.State.REFRESHING);
                        if (pullAndRefreshListViewListener != null)
                        {
                            refreshTime = System.currentTimeMillis();
                            pullAndRefreshListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                }
                else if (getLastVisiblePosition() == mTotalItemCount - 2)
                {
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            if (mScrollBack == SCROLLBACK_HEADER)
            {
                mHeaderView.setHeight(mScroller.getCurrY());
            }
            else
            {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l)
    {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        if (isFresoStopAtFling)
        {
            if (scrollState == OnScrollListener.SCROLL_STATE_FLING)
            {
                Fresco.getImagePipeline().pause();
            }
            else if (Fresco.getImagePipeline().isPaused())
            {
                Fresco.getImagePipeline().resume();

            }
        }
        if (mScrollListener != null)
        {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
        int lastVisiPos = this.getLastVisiblePosition();
        if (lastVisiPos == this.totalItemCount - 1 && !mPullLoading && !mPullRefreshing && mEnablePullLoad)
        {
            startLoadMore();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null)
        {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        this.totalItemCount = totalItemCount;

    }

    public void setFrescoStopAtFling(boolean stopAtFling)
    {
        isFresoStopAtFling = stopAtFling;
    }

    public void setPullAndRefreshListViewListener(PullAndRefreshListViewListener l)
    {
        pullAndRefreshListViewListener = l;
    }

    public void setPullAndRefreshCompleteListener(PullAndRefreshCompleteListener l)
    {
        pullAndRefreshCompleteListener = l;
    }

    public void hideFooterView()
    {
        if (null != mFooterView)
        {
            mFooterView.hide();
        }
    }

    public void setHeaderBackground(@ColorInt int color)
    {
        mHeaderView.setHeaderBackground(color);
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener
    {
        public void onXScrolling(View view);
    }

    // private void setRefreshTime()
    // {
    // long currentTime = System.currentTimeMillis();
    // PreferencesUtils.setPreferences(this.context, "PullToRefresh", timeTag,
    // currentTime);
    //
    // }
    //
    // private String getLastRefreshTime()
    // {
    // long lastRefreshTime = PreferencesUtils.getPreference(this.context,
    // "PullToRefresh", timeTag,
    // System.currentTimeMillis());
    // String time = dateFormat.format(lastRefreshTime);
    // return time;
    // }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface PullAndRefreshListViewListener
    {
        public void onRefresh();

        public void onLoadMore();
    }

    public interface PullAndRefreshCompleteListener
    {
        public void onRefreshComplete();
    }

    class DelayStopHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == MSG_STOP_REFRESH)
            {
                stopRefreshInternal(false);
            }
        }
    }
}
