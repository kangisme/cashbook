package com.kangren.cashbook.skin.ui;

import java.util.List;

import com.kang.cashbook.data.model.JsonBean;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kangren on 2018/2/11.
 */

public class ContainerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<JsonBean.ModulesBean> modulesList;

    public ContainerAdapter(JsonBean bean)
    {
        super();
        modulesList = bean.getModules();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = TemplateManager.getTemplateView(parent.getContext(), viewType);
        RecyclerView.ViewHolder holder = TemplateManager.getViewHolder(view, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder.itemView instanceof BaseView)
        {
            ((BaseView) (holder.itemView)).setData(modulesList.get(position));
            ((BaseView) (holder.itemView)).setModuleId(modulesList.get(position).getId());
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return modulesList.get(position).getType();
    }

    @Override
    public int getItemCount()
    {
        return modulesList.size();
    }
}
