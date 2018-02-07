package com.kangren.cashbook.login;

import com.kangren.cashbook.BaseActivity;
import com.kangren.cashbook.R;
import com.kangren.cashbook.util.JumpUtil;
import com.kangren.cashbook.view.TitleBar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kangren on 2018/2/1.
 */

public class LoginActivity extends BaseActivity
{

    @BindView(R.id.login_titlebar)
    TitleBar titleBar;

    @BindView(R.id.forget_password)
    TextView forgetPassword;

    @BindView(R.id.login_button)
    TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        titleBar.setExtra("注册", new TitleBar.ExtraClickListener()
        {
            @Override
            public void onClick(View view)
            {
                JumpUtil.jumpActivity(LoginActivity.this, SignupActivity.class);
            }
        });
    }

    @OnClick(R.id.forget_password)
    void forgetPassword()
    {
        Toast.makeText(this, "forget", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.login_button)
    void loginButton()
    {
        Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
    }
}
