package com.kangren.cashbook;

import com.kangren.cashbook.util.PreferencesUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class FirstActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        if (PreferencesUtil.isFirstEnter(this))
        {
            // 第一次进入配置

            PreferencesUtil.setFirstEnter(this);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(new Intent(FirstActivity.this, MainActivity.class));
                finish();
            }
        }, 100);
    }

    @Override
    public void onBackPressed()
    {
        // FirstActivity禁用Back回退键
    }
}
