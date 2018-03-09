package com.kangren.cashbook.login;

import com.kangren.cashbook.R;
import com.kangren.cashbook.common.BaseActivity;
import com.kangren.cashbook.common.TitleBar;
import com.kangren.cashbook.util.JumpUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

    @BindView(R.id.user_name_e)
    EditText userNameE;

    @BindView(R.id.user_password_e)
    EditText userPasswordE;

    private RelativeLayout signLayout;

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
                JumpUtil.jumpActivity(LoginActivity.this, SignActivity.class);
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
        String user = userNameE.getText().toString();
        String password = userPasswordE.getText().toString();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
