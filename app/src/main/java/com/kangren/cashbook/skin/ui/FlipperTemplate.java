package com.kangren.cashbook.skin.ui;

import com.kang.cashbook.data.model.JsonBean.ModulesBean;
import com.kangren.cashbook.R;
import com.kangren.cashbook.util.ScreenUtil;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by kangren on 2018/2/11.
 */

public class FlipperTemplate extends BaseView
{
    private static final float DEFAULT_SCALE = 0.4667F;

    private ImageView imageView;

    private float scale = DEFAULT_SCALE;

    public FlipperTemplate(Context context, int viewType)
    {
        super(context, viewType);
        addView();
    }

    @Override
    public void refresh(ModulesBean data)
    {
        super.refresh(data);
    }

    @Override
    public void addView()
    {
        if (imageView == null)
        {
            imageView = (ImageView) View.inflate(mContext, R.layout.template_flipper, null);
            this.addView(imageView);
            int width = ScreenUtil.getInstance(mContext).getWidth();
            imageView.getLayoutParams().width = width;
            imageView.getLayoutParams().height = (int) (width * scale);
        }
    }

    @Override
    public ModulesBean getData()
    {
        return null;
    }

    @Override
    public void setData(ModulesBean data)
    {
        fillData(data);
    }

    @Override
    public void fillData(ModulesBean data)
    {
        if (data == null)
        {
            Logger.e("module data is null");
            return;
        }
        setModuleId(data.getId());
        setOnShowing(true);

        // scale = data.getScale() == 0 ? data.getScale() : DEFAULT_SCALE;
        imageView.getLayoutParams().height = (int) (imageView.getLayoutParams().width * scale);

        if (!TextUtils.isEmpty(data.getUrl()) && imageView != null)
        {
            Picasso.with(mContext).load(data.getUrl()).into(imageView);
            imageView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(mContext, "click on image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
