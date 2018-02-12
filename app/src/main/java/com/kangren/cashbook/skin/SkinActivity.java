package com.kangren.cashbook.skin;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.kang.cashbook.data.DataService;
import com.kang.cashbook.data.UrlConstants;
import com.kang.cashbook.data.model.JsonBean;
import com.kangren.cashbook.BaseActivity;
import com.kangren.cashbook.R;
import com.kangren.cashbook.skin.ui.BaseFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kangren on 2018/2/8.
 */

public class SkinActivity extends BaseActivity
{
    private static final int LOAD_HOME_FAIL = 0;

    private static final int LOAD_HOME_SUCCESS = 1;

    @BindView(R.id.skin_tabs)
    PagerSlidingTabStrip skinTabs;

    @BindView(R.id.skin_view_pager)
    ViewPager skinViewPager;

    private SkinFragmentAdapter adapter;

    private List<BaseFragment> fragmentList;

    /**
     * tab标题
     */
    private List<String> titles;

    /**
     * tab对应的链接 BaseFragment根据不同链接加载不同的数据
     */
    private List<String> links;

    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case LOAD_HOME_FAIL:
                    break;
                case LOAD_HOME_SUCCESS:
                    if (titles == null)
                    {
                        titles = new ArrayList<>();
                    }

                    titles.clear();
                    JsonBean bean = (JsonBean) msg.obj;
                    List<JsonBean.ModulesBean.DlistBean> dlistBeans = bean.getModules().get(0).getDlist();
                    for (JsonBean.ModulesBean.DlistBean dlistBean : dlistBeans)
                    {
                        titles.add(dlistBean.getTitle());
                    }

                    if (links == null)
                    {
                        links = new ArrayList<>();
                    }

                    links.clear();
                    for (JsonBean.ModulesBean.DlistBean dlistBean : dlistBeans)
                    {
                        links.add(dlistBean.getDlink());
                    }
                    initFragments();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
        ButterKnife.bind(this);

        loadData();
    }

    private void loadData()
    {
        new Thread(new LoadTask(SkinActivity.this)).start();
    }

    private void initFragments()
    {
        if (fragmentList == null)
        {
            fragmentList = new ArrayList<>();
        }
        fragmentList.clear();
        for (String title : links)
        {
            BaseFragment baseFragment = new BaseFragment();
            Bundle bundle = new Bundle();
            bundle.putString(BaseFragment.FRAGMENT_LINK, title);
            baseFragment.setArguments(bundle);
            fragmentList.add(baseFragment);
        }
        adapter = new SkinFragmentAdapter(getSupportFragmentManager());
        skinViewPager.setAdapter(adapter);
        skinTabs.setViewPager(skinViewPager);
    }

    private class SkinFragmentAdapter extends FragmentStatePagerAdapter
    {
        public SkinFragmentAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return titles.get(position);
        }
    }

    private class LoadTask implements Runnable
    {
        private WeakReference<SkinActivity> refActivity;

        public LoadTask(SkinActivity activity)
        {
            refActivity = new WeakReference<SkinActivity>(activity);
        }

        @Override
        public void run()
        {
            if (refActivity.get() == null)
            {
                return;
            }
            // 从DataService获取模块数据
            JsonBean bean = DataService.get(refActivity.get()).dataFormat(UrlConstants.HOME);
            if (refActivity.get() == null || refActivity.get().isFinishing())
            {
                return;
            }
            if (bean == null)
            {
                handler.sendEmptyMessage(LOAD_HOME_FAIL);
            }
            else
            {
                Message message = new Message();
                message.obj = bean;
                message.what = LOAD_HOME_SUCCESS;
                handler.sendMessage(message);
            }
        }
    }
}
