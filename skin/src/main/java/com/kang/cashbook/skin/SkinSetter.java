package com.kang.cashbook.skin;

import android.content.res.Resources;
import android.view.View;

/**
 * Created by kangren on 2018/2/7.
 */

public class SkinSetter implements Setter
{

    @Override
    public void onSkinAttached(Resources skinRes, String skinPkg)
    {

    }

    @Override
    public void onSkinDetached()
    {

    }

    @Override
    public int getSkinStatus(View view)
    {
        return 0;
    }
}
