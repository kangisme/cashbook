package com.kangren.cashbook.login;

import java.util.Random;

import com.kangren.cashbook.R;
import com.kangren.cashbook.util.ScreenUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * 验证码控件 Created by kangren on 2018/2/7.
 */

public class VerifyCodeView extends View
{

    /**
     * 验证码文本
     */
    private String mCodeText;

    /**
     * 验证码文本大小
     */
    private int mCodeTextSize;

    /**
     * 验证码长度
     */
    private int mCodeLength;

    /**
     * 验证码背景颜色
     */
    private int mBackground;

    /**
     * 验证码是否包含字母
     */
    private boolean isContainChar;

    /**
     * 验证码中点个数
     */
    private int mPointNum;

    /**
     * 验证码中线的个数
     */
    private int mLineNum;

    private Paint mPaint;

    private Rect mBound;

    private Bitmap bitmap;

    private int mWidth;

    private int mHeight;

    private Random mRandom = new Random();

    public VerifyCodeView(Context context)
    {
        super(context);
    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        loadValue(context, attrs);
        initData();
    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 获取宽高的具体值
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置宽高默认为建议的最小宽高
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        }
        else
        {
            mPaint.setTextSize(mCodeTextSize);
            mPaint.getTextBounds(mCodeText, 0, mCodeText.length(), mBound);
            width = getPaddingLeft() + mBound.width() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        }
        else
        {
            mPaint.setTextSize(mCodeTextSize);
            mPaint.getTextBounds(mCodeText, 0, mCodeText.length(), mBound);
            height = getPaddingTop() + mBound.height() + getPaddingBottom();
        }

        // 设置测量的宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();

        if (bitmap == null)
        {
            bitmap = createBitmapValidate();
        }
        canvas.drawBitmap(bitmap, 0, 0, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            // 点击刷新
            refresh();
        }
        return super.onTouchEvent(event);
    }

    private void initData()
    {
        mCodeText = getVerifyCode(mCodeLength, isContainChar);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBound = new Rect();
        // 计算文字所在矩形，可以得到宽高
        mPaint.getTextBounds(mCodeText, 0, mCodeText.length(), mBound);
    }

    private void loadValue(Context context, @Nullable AttributeSet attrs)
    {
        // 这里设置各个属性的默认值
        mCodeTextSize = (int) (22 * ScreenUtil.getInstance(context).getDensity());
        mCodeLength = 6;
        mBackground = 0xFFF6F6F6;
        mPointNum = 1000;
        mLineNum = 6;
        isContainChar = true;

        // 这里加载自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeView);
        for (int i = 0; i < ta.getIndexCount(); i++)
        {
            int index = ta.getIndex(i);
            switch (index)
            {
                case R.styleable.VerifyCodeView_codeTextSize:
                    // 默认设置为16sp，TypeValue类 px转sp 一个转换类
                    mCodeTextSize = ta.getDimensionPixelSize(index,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.VerifyCodeView_codeBackground:
                    mBackground = ta.getColor(index, Color.WHITE);
                    break;
                case R.styleable.VerifyCodeView_codeLength:
                    mCodeLength = ta.getInteger(index, 4);
                    break;
                case R.styleable.VerifyCodeView_isContainChar:
                    isContainChar = ta.getBoolean(index, false);
                    break;
                case R.styleable.VerifyCodeView_pointNum:
                    mPointNum = ta.getInteger(index, 100);
                    break;
                case R.styleable.VerifyCodeView_lineNum:
                    mLineNum = ta.getInteger(index, 3);
                    break;
            }
        }
        ta.recycle();
    }

    /**
     * 创建图片验证码
     * 
     * @return
     */
    private Bitmap createBitmapValidate()
    {
        if (bitmap != null && !bitmap.isRecycled())
        {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
        // 创建图片
        Bitmap sourceBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        // 创建画布
        Canvas canvas = new Canvas(sourceBitmap);
        // 画上背景颜色
        canvas.drawColor(mBackground);
        // 初始化文字画笔
        mPaint.setStrokeWidth(3f);
        mPaint.setTextSize(mCodeTextSize);
        // 测量验证码字符串显示的宽度值
        float textWidth = mPaint.measureText(mCodeText);
        // 画上验证码
        int length = mCodeText.length();
        // 计算一个字符的所占位置
        float charLength = textWidth / length;
        for (int i = 1; i <= length; i++)
        {
            int offsetDegree = mRandom.nextInt(15);
            // 这里只会产生0和1，如果是1那么正旋转正角度，否则旋转负角度
            offsetDegree = mRandom.nextInt(2) == 1 ? offsetDegree : -offsetDegree;
            // 用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。
            canvas.save();
            // 设置旋转
            canvas.rotate(offsetDegree, mWidth / 2, mHeight / 2);
            // 给画笔设置随机颜色
            mPaint.setARGB(255, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20);
            // 设置字体的绘制位置
            canvas.drawText(String.valueOf(mCodeText.charAt(i - 1)), (i - 1) * charLength + 5, mHeight * 4 / 5f,
                    mPaint);
            // 用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。
            canvas.restore();
        }

        // 重新设置画笔
        mPaint.setARGB(255, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20);
        mPaint.setStrokeWidth(1);
        // 产生干扰效果1 －－ 干扰点
        for (int i = 0; i < mPointNum; i++)
        {
            drawPoint(canvas, mPaint);
        }
        // 生成干扰效果2 －－ 干扰线
        for (int i = 0; i < mLineNum; i++)
        {
            drawLine(canvas, mPaint);
        }
        return sourceBitmap;
    }

    /**
     * 生成干扰点
     */
    private void drawPoint(Canvas canvas, Paint paint)
    {
        PointF pointF = new PointF(mRandom.nextInt(mWidth) + 10, mRandom.nextInt(mHeight) + 10);
        canvas.drawPoint(pointF.x, pointF.y, paint);
    }

    /**
     * 生成干扰线
     */
    private void drawLine(Canvas canvas, Paint paint)
    {
        int startX = mRandom.nextInt(mWidth);
        int startY = mRandom.nextInt(mHeight);
        int endX = mRandom.nextInt(mWidth);
        int endY = mRandom.nextInt(mHeight);
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    /**
     * 获取验证码
     * 
     * @param length 长度
     * @param contain 是否包含字母
     * @return 验证码
     */
    public String getVerifyCode(int length, boolean contain)
    {
        StringBuilder var = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++)
        {
            if (contain)
            {
                // 字母或数字
                if (random.nextBoolean())
                {
                    // 大写或小写
                    int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                    var.append((char) (choice + random.nextInt(26)));
                    continue;
                }
            }
            var.append(String.valueOf(random.nextInt(10)));
        }
        return var.toString();
    }

    /**
     * 判断验证码是否一致 忽略大小写
     */
    public Boolean isEqualsIgnoreCase(String CodeString)
    {
        return mCodeText.equalsIgnoreCase(CodeString);
    }

    /**
     * 判断验证码是否一致 不忽略大小写
     */
    public Boolean isEquals(String CodeString)
    {
        return mCodeText.equals(CodeString);
    }

    /**
     * 提供外部调用的刷新方法
     */
    public void refresh()
    {
        mCodeText = getVerifyCode(mCodeLength, isContainChar);
        bitmap = createBitmapValidate();
        invalidate();
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
