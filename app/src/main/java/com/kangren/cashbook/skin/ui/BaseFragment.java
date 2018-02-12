package com.kangren.cashbook.skin.ui;

import com.kangren.cashbook.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kangren on 2018/2/8.
 */

public class BaseFragment extends Fragment
{
    public static final String FRAGMENT_LINK = "fragment_link";

    private String link;

    private ViewGroup content;

    /**
     * 加载内容交给容器处理
     */
    private Container container;

    private Container.ContainerListener listener = new Container.ContainerListener()
    {
        @Override
        public void constructSuccess(SwipeRefreshLayout refreshLayout)
        {
            content.addView(refreshLayout, 0);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            link = getArguments().getString(FRAGMENT_LINK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        content = (ViewGroup) inflater.inflate(R.layout.fragment_base, container, false);
        loadData();
        return content;
    }

    private void loadData()
    {
        if (container == null)
        {
            container = new Container(this.getActivity(), link);
        }
        container.startConstruct(listener);
    }

}
