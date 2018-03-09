package com.kangren.cashbook.common.recyclerview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

public class PlayerProgress extends View
{
    private int ballWidth;

    private int ballHeight;

    private float desity;

    private Paint mPaint;

    private int currentPostion;

    private long beginTime;

    private int duation = 800;

    public PlayerProgress(Context context)
    {
        super(context);
        init();

    }

    public PlayerProgress(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        desity = getResources().getDisplayMetrics().density;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ballWidth = (int) (8 * desity);
        ballHeight = (int) (8 * desity);
        beginTime = SystemClock.elapsedRealtime();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension((int) (ballWidth * 3), (int) (ballHeight * 2));
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.translate(ballWidth / 2, 0);
        int w = getWidth() - ballWidth;
        int h = getHeight();
        long time = SystemClock.elapsedRealtime();
        int delta = (int) ((time - beginTime) % duation);
        int a = (int) (Math.sin(2 * Math.PI * delta / duation) * ballHeight / 4) + ballHeight / 2;
        int b = ballHeight - a;

        currentPostion = delta % (duation / 2) * w / (duation / 2);
        if (delta > duation / 2)
        {
            currentPostion = w - currentPostion;
        }

        if (a > b)
        {
            mPaint.setColor(0xfffc9500);
            canvas.drawCircle(w - currentPostion, h / 2, b, mPaint);
            mPaint.setColor(0xff0094eb);
            canvas.drawCircle(currentPostion, h / 2, a, mPaint);
        }
        else
        {
            mPaint.setColor(0xff0094eb);
            canvas.drawCircle(currentPostion, h / 2, a, mPaint);
            mPaint.setColor(0xfffc9500);
            canvas.drawCircle(w - currentPostion, h / 2, b, mPaint);
        }
        postInvalidateDelayed(50);
    }

}
