package com.kangren.cashbook.skin;

import java.util.ArrayList;
import java.util.List;

import com.kangren.cashbook.BaseActivity;
import com.kangren.cashbook.R;
import com.kangren.cashbook.template.BaseFragment;

import android.os.Bundle;
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

    @BindView(R.id.skin_tabs)
    PagerSlidingTabStrip skinTabs;

    @BindView(R.id.skin_view_pager)
    ViewPager skinViewPager;

    private SkinFragmentAdapter adapter;

    private List<BaseFragment> fragmentList;

    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
        ButterKnife.bind(this);

        initFragments();

        initViewPager();
    }

    private void initFragments()
    {
        fragmentList = new ArrayList<>();
        titles = new String[] {"推荐", "分类", "排行", "我的"};
        for (String title : titles)
        {
            BaseFragment baseFragment = new BaseFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Title", title);
            baseFragment.setArguments(bundle);
            fragmentList.add(baseFragment);
        }
    }

    private void initViewPager()
    {
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
            return titles[position];
        }
    }
}
