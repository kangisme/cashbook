package com.kangren.cashbook;

import com.kangren.cashbook.setting.SettingActivity;
import com.kangren.cashbook.util.JumpUtil;
import com.kangren.cashbook.wallpaper.WallpaperActivity;
import com.kangren.cashbook.wechat.WechatActivity;
import com.orhanobut.logger.Logger;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends BaseActivity
{
    private long lastBackPressed;

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();// 初始化状态
        drawerLayout.addDrawerListener(drawerToggle);

        // 设置导航栏NavigationView的点击事件
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.item_one:
                        JumpUtil.jumpActivity(MainActivity.this, WallpaperActivity.class);
                        break;
                    case R.id.item_two:
                        JumpUtil.jumpActivity(MainActivity.this, SettingActivity.class);
                        break;
                    case R.id.item_three:
                        JumpUtil.jumpActivity(MainActivity.this, WechatActivity.class);
                        break;
                }
                drawerLayout.closeDrawers();// 关闭抽屉
                return true;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        long current = SystemClock.elapsedRealtime();
        Logger.d(current);
        if (current - lastBackPressed < 2000)
        {
            super.onBackPressed();
        }
        else
        {
            lastBackPressed = current;
            Toast.makeText(MainActivity.this, "再次返回退出应用", Toast.LENGTH_SHORT).show();
        }
    }
}
