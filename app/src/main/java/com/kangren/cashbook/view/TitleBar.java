package com.kangren.cashbook.view;

import com.kangren.cashbook.R;
import com.orhanobut.logger.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义标题栏 Created by kangren on 2018/1/29.
 */

public class TitleBar extends RelativeLayout implements View.OnClickListener
{

    private int[] TEXT_ATTR = new int[] {android.R.attr.text};

    private TextView mTitle;

    private TextView mExtra;

    private ExtraClickListener clickListener;

    public TitleBar(Context context)
    {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.parseColor("#AAFA0000"));
        LayoutInflater.from(context).inflate(R.layout.titlebar_layout, this, true);

        mTitle = (TextView) findViewById(R.id.header_title);
        mTitle.setOnClickListener(this);
        mExtra = (TextView) findViewById(R.id.header_extra);
        mExtra.setOnClickListener(this);
        findViewById(R.id.header_back_icon).setOnClickListener(this);
        findViewById(R.id.header_back_text).setOnClickListener(this);

        TypedArray ta = context.obtainStyledAttributes(attrs, TEXT_ATTR);
        mTitle.setText(ta.getText(0));
        ta.recycle();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.header_title:
                break;
            case R.id.header_back_icon:
            case R.id.header_back_text:
                ((Activity) getContext()).onBackPressed();
                break;

            case R.id.header_extra:
                if (clickListener != null)
                {
                    clickListener.onClick(v);
                }
        }
    }

    /**
     * 抛出接口外部调用设置
     * 
     * @param extra 额外按键文字
     * @param listener 外部实现接口
     */
    public void setExtra(String extra, ExtraClickListener listener)
    {
        clickListener = listener;
        if (mExtra == null)
        {
            Logger.e("TitleBar error");
            return;
        }
        if (extra == null || TextUtils.isEmpty(extra))
        {
            Logger.e("extra can not be null or empty");
        }
        mExtra.setText(extra);
        mExtra.setVisibility(VISIBLE);
    }

    /**
     * TitleBar额外按键点击事件抛出接口
     */
    public interface ExtraClickListener
    {
        void onClick(View view);
    }
}
