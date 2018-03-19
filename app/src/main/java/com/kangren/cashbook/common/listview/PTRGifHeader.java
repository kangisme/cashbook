package com.kangren.cashbook.common.listview;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import com.kang.cashbook.imageloader.AsyncImageUtils;
import com.kang.cashbook.imageloader.BitmapCallback;
import com.kangren.cashbook.R;
import com.kangren.cashbook.common.listview.gif.GifDecoder;
import com.kangren.cashbook.common.listview.gif.GifFrame;
import com.kangren.cashbook.util.Utils;
import com.orhanobut.logger.Logger;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 动画总体上就是下拉从1-9帧开始变化，如果一直拉就停在第9帧上， 然后松手回弹到固定的高度开始从10帧循环执行动画到刷新结束，
 * 刷新结束后再从9的状态收回去
 */
public class PTRGifHeader implements IPullToRefreshListViewHeader
{

    public final static int SCROLL_DURATION = 400; // scroll back duration

    /**
     * 下拉刷新过程中可播放的帧数
     */
    private static final int MAX_PULL_FRAME_INDEX = 9;

    /**
     * 动画持续的最小周期数
     */
    private static final int MIN_ANIMATION_REPEAT = 2;

    /**
     * 为了防止频繁地decode同一张gif图片，使用一个全局对象存储GifDecoder，
     * 每个gif图片对应一个decoder，相同图片复用同一个decoder，
     * 当外部不再使用当前Header的时候，有义务调用release()释放占用的资源。
     */
    private static SparseArray<RefRecord> refRecords = new SparseArray<RefRecord>();

    private Context context;

    private View headerView;

    private View containerView;

    private ImageView imageView;

    private View contentView;

    private View gifContentView;

    private View sloganImageView;

    private GifDecoder gifDecoder;

    private GifPlayHandler gifPlayHandler;

    private State state;

    private int contentHeight;

    private int gifResID;

    private OnAnimationStopListener stopListener;

    public PTRGifHeader(Context context)
    {
        this.context = context.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        headerView = inflater.inflate(R.layout.pull_to_refresh_gif_header, null);
        containerView = headerView.findViewById(R.id.container);
        imageView = (ImageView) headerView.findViewById(R.id.image);
        contentView = headerView.findViewById(R.id.content);
        gifContentView = headerView.findViewById(R.id.gif_content);
        sloganImageView = headerView.findViewById(R.id.slogan_image);

        LinearLayout.LayoutParams contentParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        contentParams.height = Utils.dp2px(context, 82);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        contentHeight = params.height + params.topMargin + params.bottomMargin;

        gifPlayHandler = new GifPlayHandler(this);
    }

    @Override
    public void setState(State state)
    {
        if (this.state != state)
        {
            switch (state)
            {
                case NORMAL:
                    if (this.state == State.REFRESHING)
                    {
                        stopPlayGif();
                    }
                    break;
                case READY:
                    if (this.state == State.REFRESHING)
                    {
                        stopPlayGif();
                    }
                    setCurrentFrame(getMaxPullIndex() - 1);
                    break;
                case REFRESHING:
                    startPlayGif();
                    break;
                case STOP:
                    if (this.state == State.REFRESHING)
                    {
                        stopPlayGif();
                    }
                    break;
            }
            this.state = state;
        }
    }

    @Override
    public int getHeight()
    {
        return headerView.getHeight();
    }

    @Override
    public void setHeight(int height)
    {
        if (height < 0)
            height = 0;
        ViewGroup.LayoutParams params = containerView.getLayoutParams();
        params.height = height;
        containerView.setLayoutParams(params);

        if (state == State.NORMAL)
        {
            float percent = getPercentage();
            int frameIndex = (int) ((getMaxPullIndex() - 1) * percent);
            setCurrentFrame(frameIndex);
        }
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
        return headerView;
    }

    @Override
    public TextView getTimeView()
    {
        return null;
    }

    /**
     * 释放持有的gif资源。 使同一个gifResID对应的引用计数减一，当引用等于0时，释放资源。 外部使用者有义务调用此方法释放gif资源。
     */
    public void release()
    {
        stopPlayGif();

        RefRecord ref = refRecords.get(gifResID);
        if (ref != null)
        {
            // 引用减1
            ref.count--;
            if (ref.count <= 0)
            {
                releaseInternal();
            }
        }
        else
        {
            releaseInternal();
        }
    }

    @Override
    public boolean needWaitForAnimation()
    {
        return true;
    }

    @Override
    public void setOnAnimationStopListener(OnAnimationStopListener listener)
    {
        stopListener = listener;
    }

    private void releaseInternal()
    {
        // 释放资源
        gifDecoder.free();
        imageView.setImageBitmap(null);
    }

    private int getMaxPullIndex()
    {
        int totalCount = gifDecoder.getFrameCount();
        return totalCount < MAX_PULL_FRAME_INDEX ? totalCount : MAX_PULL_FRAME_INDEX;
    }

    public void setGifImage(int resID)
    {
        gifResID = resID;
        startDecode();
    }

    private void startDecode()
    {
        RefRecord ref = refRecords.get(gifResID);
        if (ref != null && ref.count > 0)
        {
            gifDecoder = ref.decoder;
            ref.count++;
            return;
        }

        Resources r = context.getResources();
        try
        {
            InputStream is = r.openRawResource(gifResID);
            if (gifDecoder != null && gifDecoder.isAlive())
            {
                return;
            }
            gifDecoder = new GifDecoder(null);
            gifDecoder.setGifImage(is);
            gifDecoder.start();

            RefRecord record = new RefRecord();
            record.count = 1;
            record.decoder = gifDecoder;
            refRecords.put(gifResID, record);
        }
        catch (Exception e)
        {
            Logger.e("no resId " + gifResID);
        }
    }

    private float getPercentage()
    {
        float percent;
        percent = (float) getHeight() / getContentHeight();
        if (percent > 1f)
        {
            percent = 1f;
        }
        return percent;
    }

    private void setCurrentFrame(int frame)
    {
        if (gifDecoder.parseOk())
        {
            if (frame >= gifDecoder.getFrameCount())
            {
                frame = gifDecoder.getFrameCount() - 1;
            }

            Bitmap bitmap = gifDecoder.getFrameImage(frame);
            imageView.setImageBitmap(bitmap);
        }
    }

    private void startPlayGif()
    {
        if (gifDecoder.parseOk())
        {
            if (!gifPlayHandler.hasMessages(GifPlayHandler.MSG_NEXT_FRAME))
            {
                gifPlayHandler.sendEmptyMessage(GifPlayHandler.MSG_START);
            }
        }
        else
        {
            stopPlayGif();
        }
    }

    private void stopPlayGif()
    {
        gifPlayHandler.sendEmptyMessage(GifPlayHandler.MSG_STOP);
    }

    @Override
    public void setHeaderBackground(int color)
    {
        headerView.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundImageUrl(String url, int defaultId)
    {
        if (gifContentView != null)
        {
            // 加载图片
            AsyncImageUtils.downloadBitmap(context, url, new BitmapCallback()
            {
                @Override
                public void onLoadingComplete(String url, Bitmap loadedImage)
                {
                    if (loadedImage != null)
                    {
                        float scale = (float) loadedImage.getWidth() / loadedImage.getHeight();
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
                        params.width = Utils.screenHeightPx(context);
                        params.height = (int) (params.width / scale) + sloganImageView.getLayoutParams().height;
                        gifContentView.setBackgroundDrawable(new BitmapDrawable(context.getResources(), loadedImage));
                    }
                }

                @Override
                public void onLoadingFail(String url)
                {

                }
            });
        }
    }

    private static class GifPlayHandler extends Handler
    {
        private static final int MSG_STOP = 0;

        private static final int MSG_START = 1;

        private static final int MSG_NEXT_CYCLE = 2;

        private static final int MSG_NEXT_FRAME = 3;

        private WeakReference<PTRGifHeader> headerRef;

        private int frameIndex;

        private int roundIndex;

        private boolean stopFlag;

        public GifPlayHandler(PTRGifHeader header)
        {
            headerRef = new WeakReference<PTRGifHeader>(header);
        }

        @Override
        public void handleMessage(Message msg)
        {
            PTRGifHeader header = headerRef.get();
            if (header == null)
            {
                return;
            }

            if (msg.what == MSG_START)
            {
                stopFlag = false;
                roundIndex = 0;
                frameIndex = header.getMaxPullIndex() - 1;
                sendEmptyMessage(GifPlayHandler.MSG_NEXT_CYCLE);
            }
            else if (msg.what == MSG_STOP)
            {
                stopFlag = true;
            }
            else if (msg.what == MSG_NEXT_CYCLE)
            {
                roundIndex++;
                sendEmptyMessage(GifPlayHandler.MSG_NEXT_FRAME);
            }
            else if (msg.what == MSG_NEXT_FRAME)
            {
                int delay = 50;
                GifFrame frame = header.gifDecoder.getFrame(frameIndex);
                if (frame != null)
                {
                    header.imageView.setImageBitmap(frame.image);
                    delay = frame.delay;
                }

                frameIndex++;
                if (frameIndex < header.gifDecoder.getFrameCount())
                {
                    sendEmptyMessageDelayed(MSG_NEXT_FRAME, delay);
                }
                else
                {
                    if (roundIndex < MIN_ANIMATION_REPEAT || !stopFlag)
                    {
                        frameIndex = 0;
                        sendEmptyMessage(GifPlayHandler.MSG_NEXT_CYCLE);
                    }
                    else
                    {
                        if (header.stopListener != null)
                        {
                            header.stopListener.onAnimationStopped();
                        }
                    }
                }
            }
        }
    }

    static class RefRecord
    {
        int count;

        GifDecoder decoder;
    }
}
