package com.kangren.cashbook.skin.ui;

import com.kangren.cashbook.R;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

/**
 * Created by kangren on 2018/2/11.
 */

public class Container
{

    private Activity context;

    /**
     * 容器主体
     */
    private RecyclerView recyclerView;

    public Container(Activity context)
    {
        this.context = context;

        initRecyclerView();
    }

    private void initRecyclerView()
    {
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) this.context.getLayoutInflater().inflate(
                R.layout.container, null);
        recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.recyclerView);
        // 设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        swipeRefreshLayout.setProgressViewOffset(true, 50, 200);
        // 设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);

        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        // 通过 setEnabled(false) 禁用下拉刷新
        swipeRefreshLayout.setEnabled(false);

        // 设定下拉圆圈的背景
        swipeRefreshLayout.setProgressBackgroundColor(R.color.colorAccent);

        /*
         * 设置手势下拉刷新的监听
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                // 刷新动画开始后回调到此方法

            }
        });
    }
}
