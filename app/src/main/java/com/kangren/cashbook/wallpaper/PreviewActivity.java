package com.kangren.cashbook.wallpaper;

import com.kangren.cashbook.BaseActivity;
import com.kangren.cashbook.R;
import com.kangren.cashbook.view.TitleBar;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        preview = (ImageView) findViewById(R.id.preview_img);
        titleBar = (TitleBar) findViewById(R.id.preview_titlebar);
        Intent date = getIntent();
        if (date != null)
        {
            int height = getResources().getDisplayMetrics().heightPixels - titleBar.getHeight();
            int width = getResources().getDisplayMetrics().widthPixels;
            Logger.e("width: " + width + "height: " + height + "titlebar: " + titleBar.getHeight());
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
