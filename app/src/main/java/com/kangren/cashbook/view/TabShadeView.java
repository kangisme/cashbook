package com.kangren.cashbook.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by kangren on 2017/11/20.
 */

public class TabShadeView extends View
{
    /**
     * 字体默认大小
     */
    private final int DEFAULT_TEXT_SIZE = 14;

    /**
     * Tab选中时字体大小
     */
    private final int SELECTED_TEXT_SIZE = 15;

    private final int DEFAULT_TEXT_COLOR = 0xFF333333;

    /**
     * Tab选中时字体颜色
     */
    private final int SELECTED_TEXT_COLOR = 0xFFFFFF00;

    private Context mContext;

    /**
     * 图标
     */
    private Bitmap iconBitmap;

    /**
     * 标题
     */
    private String mTitle;

    /**
     * 图标绘制范围
     */
    private Rect iconRect;

    /**
     * 文字笔画
     */
    private Paint textPaint;

    /**
     * 文字范围
     */
    private Rect textBound;

    /**
     * 透明度（0.0-1.0）
     */
    private float mAlpha;

    private boolean isSelected;

    public TabShadeView(Context context)
    {
        this(context, null);
    }

    public TabShadeView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TabShadeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        if (mTitle == null || TextUtils.isEmpty(mTitle))
        {
            return;
        }
        textBound = new Rect();
        textPaint = new Paint();
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE,
                getResources().getDisplayMetrics()));
        textPaint.setColor(DEFAULT_TEXT_COLOR);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.getTextBounds(mTitle, iconBitmap.getWidth(), mTitle.length(), textBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();

        int iconWidth = iconBitmap.getWidth();
        int width = iconWidth + textBound.width();
        // 获取图标的绘制范围
        iconRect = new Rect(0, 0, iconWidth, height);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        // 进一取整
        int alpha = (int) Math.ceil((255 * mAlpha));
        // 绘制图标
        canvas.drawBitmap(iconBitmap, null, iconRect, null);
        drawSourceText(canvas, alpha);
        drawTargetText(canvas, alpha);

    }

    /**
     * 绘制默认状态下的字体
     *
     * @param canvas Canvas
     * @param alpha 字体颜色透明度
     */
    private void drawSourceText(Canvas canvas, int alpha)
    {
        textPaint.setColor(DEFAULT_TEXT_COLOR);
        textPaint.setAlpha(255 - alpha);
        canvas.drawText(mTitle, iconRect.left + iconRect.width() / 2 - textBound.width() / 2,
                iconRect.bottom + textBound.height(), textPaint);
    }

    /**
     * 绘制滑动到该标签时的字体
     *
     * @param canvas Canvas
     * @param alpha 字体颜色透明度
     */
    private void drawTargetText(Canvas canvas, int alpha)
    {
        textPaint.setColor(SELECTED_TEXT_COLOR);
        textPaint.setAlpha(alpha);
        canvas.drawText(mTitle, iconRect.left + iconRect.width() / 2 - textBound.width() / 2,
                iconRect.bottom + textBound.height(), textPaint);
    }

    /**
     * 设置图标透明度并重绘
     *
     * @param alpha 透明度
     */
    public void setAlpha(float alpha)
    {
        if (mAlpha != alpha)
        {
            this.mAlpha = alpha;
            invalidateView();
        }
    }

    /**
     * 设置当前tab是否被选中
     * 
     * @param selected 选中状态
     */
    public void setSelected(boolean selected)
    {
        if (isSelected != selected)
        {
            this.isSelected = selected;
            invalidate();
        }
    }

    /**
     * 判断当前是否为UI线程，是则直接重绘，否则调用postInvalidate()利用Handler来重绘
     */
    private void invalidateView()
    {
        if (Looper.getMainLooper() == Looper.myLooper())
        {
            invalidate();
        }
        else
        {
            postInvalidate();
        }
    }

    public class Builder
    {
        public Builder(Context context)
        {
            mContext = context;
        }

        public Builder loadImage(String url)
        {
            return this;
        }

        public Builder setBitmap(Bitmap bitmap)
        {
            iconBitmap = bitmap;
            return this;
        }

        public Builder setTitle(String title)
        {
            mTitle = title;
            return this;
        }

        public TabShadeView build()
        {
            return new TabShadeView(mContext);
        }
    }
}
