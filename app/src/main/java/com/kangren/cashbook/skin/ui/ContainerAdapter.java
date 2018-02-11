package com.kangren.cashbook.skin.ui;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by kangren on 2018/2/11.
 */

public class ContainerAdapter extends RecyclerView.Adapter<ContainerHolder>
{

    public ContainerAdapter()
    {
        super();
    }

    @Override
    public ContainerHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(ContainerHolder holder, int position)
    {

    }

    @Override
    public int getItemViewType(int position)
    {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount()
    {
        return 0;
    }
}
