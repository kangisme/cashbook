package com.kangren.cashbook.skin.ui;

import com.kang.cashbook.data.model.JsonBean.ModulesBean;
import com.kangren.cashbook.R;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by kangren on 2018/2/8.
 */

public abstract class BaseView extends LinearLayout
{

    public String moduleId;

    protected Context mContext;

    protected String templateId;

    /**
     * 分割线
     */
    protected View dividerView;

    public BaseView(Context context, String templateId)
    {
        super(context);
        mContext = context;
        this.templateId = templateId;
    }

    /**
     * 创建控件
     */
    abstract public void addView();

    abstract public ModulesBean getData();

    abstract public void setData(ModulesBean data);

    abstract public void fillData(ModulesBean data);


    /**
     * 返回自己的类型
     */
    public String getTemplateId()
    {
        return templateId;
    }

    /**
     * 返回模块id
     */
    public String getModuleId()
    {
        return moduleId;
    }

    /**
     * 添加分割线
     */
    public void addDividerView()
    {
        if (dividerView == null)
        {
            dividerView = new View(mContext);
            dividerView.setBackgroundColor(mContext.getResources().getColor(R.color.model_divider));
            dividerView.setLayoutParams(new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                    mContext.getResources().getDimensionPixelOffset(R.dimen.module_divider)));
            this.addView(dividerView);
        }
    }

    public void adjustViewByData(ModulesBean data)
    {
        if (data == null)
        {
            return;
        }

    }
}
