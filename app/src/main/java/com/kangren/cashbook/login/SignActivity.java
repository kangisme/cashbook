package com.kangren.cashbook.login;

import com.kangren.cashbook.R;
import com.kangren.cashbook.common.BaseActivity;
import com.kangren.cashbook.common.TitleBar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kangren on 2018/2/8.
 */

public class SignActivity extends BaseActivity
{
    @BindView(R.id.signup_titlebar)
    TitleBar signupTitlebar;

    @BindView(R.id.user_name_e)
    EditText userNameE;

    @BindView(R.id.user_password_e)
    EditText userPasswordE;

    @BindView(R.id.verify_code_e)
    EditText verifyCodeE;

    @BindView(R.id.login_button)
    TextView loginButton;

    @BindView(R.id.verify_code_view)
    VerifyCodeView verifyCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        signupTitlebar.setExtra("返回登录", new TitleBar.ExtraClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    @OnClick(R.id.login_button)
    public void onViewClicked()
    {
        String user = userNameE.getText().toString();
        String password = userPasswordE.getText().toString();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(password))
        {
            Toast.makeText(SignActivity.this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!verifyCodeView.isEquals(verifyCodeE.getText().toString()))
        {
            Toast.makeText(SignActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
            verifyCodeView.refresh();
            return;
        }

    }
}
