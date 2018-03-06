package com.kangren.cashbook.skin.ui;

import java.lang.ref.WeakReference;

import com.kang.cashbook.data.DataService;
import com.kang.cashbook.data.UrlConstants;
import com.kang.cashbook.data.model.JsonBean;
import com.kangren.cashbook.R;
import com.orhanobut.logger.Logger;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by kangren on 2018/2/11.
 */

public class Container
{
    private static final int LOAD_FAIL = 0;

    private static final int LOAD_SUCCESS = 1;

    private Activity context;

    private SwipeRefreshLayout refreshLayout;
    /**
     * 容器主体
     */
    private RecyclerView recyclerView;

    private ContainerListener listener;

    private ContainerAdapter adapter;

    /**
     * 请求链接
     */
    private String link;

    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case LOAD_FAIL:
                    Logger.e("load fail");
                    break;
                case LOAD_SUCCESS:
                    Logger.e("load success");
                    JsonBean bean = (JsonBean) msg.obj;
                    initRecyclerView(bean);
                    break;
            }
            return false;
        }
    });

    public Container(Activity context, String link)
    {
        this.context = context;
        this.link = link;
    }

    public void startConstruct(ContainerListener listener)
    {
        this.listener = listener;
        loadDate();
    }

    private void loadDate()
    {
        new Thread(new LoadData(context)).start();
    }

    private void initRecyclerView(JsonBean bean)
    {
        refreshLayout = (SwipeRefreshLayout) this.context.getLayoutInflater().inflate(
                R.layout.container, null);
        recyclerView = (RecyclerView) refreshLayout.findViewById(R.id.recyclerView);
        // 设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        refreshLayout.setProgressViewOffset(true, 50, 200);
        // 设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        refreshLayout.setSize(SwipeRefreshLayout.LARGE);

        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        // 通过 setEnabled(false) 禁用下拉刷新
        refreshLayout.setEnabled(true);

        // 设定下拉圆圈的背景
        refreshLayout.setProgressBackgroundColor(R.color.colorAccent);

        /*
         * 设置手势下拉刷新的监听
         */
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                // 刷新动画开始后回调到此方法

            }
        });

        adapter = new ContainerAdapter(bean);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        if (listener != null)
        {
            listener.constructSuccess(refreshLayout);
        }
    }

    public interface ContainerListener
    {
        void constructSuccess(SwipeRefreshLayout refreshLayout);
    }

    private class LoadData implements Runnable
    {
        private WeakReference<Activity> refActivity;

        public LoadData(Activity activity)
        {
            refActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void run()
        {
            if (refActivity.get() == null)
            {
                return;
            }
            // 从DataService获取模块数据
            JsonBean bean = DataService.get(refActivity.get()).dataFormat(UrlConstants.BASE_URL + "/" + link);
            if (refActivity.get() == null || refActivity.get().isFinishing())
            {
                return;
            }
            if (bean == null)
            {
                handler.sendEmptyMessage(LOAD_FAIL);
            }
            else
            {
                Message message = new Message();
                message.obj = bean;
                message.what = LOAD_SUCCESS;
                handler.sendMessage(message);
            }
        }
    }

}
