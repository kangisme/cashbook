package com.kangren.cashbook.wallpaper;

import com.kangren.cashbook.BaseActivity;
import com.kangren.cashbook.R;
import com.kangren.cashbook.util.ScreenUtil;
import com.kangren.cashbook.view.TitleBar;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by kangren on 2018/1/29.
 */

public class PreviewActivity extends BaseActivity
{
    /**
     * 标题栏高度44dp
     */
    private final int TITLEBAR_HEIGHT = 44;

    private ImageView preview;

    private TitleBar titleBar;

    private ScreenUtil screenUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        screenUtil = ScreenUtil.getInstance(this);
        preview = (ImageView) findViewById(R.id.preview_img);
        titleBar = (TitleBar) findViewById(R.id.preview_titlebar);
        ((TitleBar) findViewById(R.id.preview_titlebar)).setExtra("使用", new TitleBar.ExtraClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(PreviewActivity.this, "使用", Toast.LENGTH_SHORT).show();
            }
        });
        Intent date = getIntent();
        if (date != null)
        {
            int height = (int) (screenUtil.getScreenHeight() - screenUtil.getStatusHeight()
                    - TITLEBAR_HEIGHT * screenUtil.getDensity());
            int width = screenUtil.getWidth();
            Logger.e(
                    "width: " + width + "height: " + height + "titlebar :" + TITLEBAR_HEIGHT * screenUtil.getDensity());
            Uri fullPhotoUri = date.getData();
            Picasso.with(PreviewActivity.this).load(fullPhotoUri).resize(width, height).into(preview);
        }
    }

    // 原图，可能会产生OOM
    // private Bitmap getBitmapFromUri(Uri uri) {
    // try {
    // // 读取uri所在的图片
    // return MediaStore.Images.Media.getBitmap(this.getContentResolver(),
    // uri);
    // } catch (Exception e) {
    // Logger.e("error 图像目录" + uri);
    // e.printStackTrace();
    // return null;
    // }
    // }
}
