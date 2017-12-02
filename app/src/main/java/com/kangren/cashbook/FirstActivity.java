package com.kangren.cashbook;

import com.kangren.cashbook.util.JumpUtil;

import android.os.Bundle;

public class FirstActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        JumpUtil.jumpActivity(this, MainActivity.class);
    }
}
