package com.kangren.cashbook.wallpaper;

import com.kangren.cashbook.BaseActivity;
import com.kangren.cashbook.R;
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

    private ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        preview = (ImageView) findViewById(R.id.preview_img);
        Intent date = getIntent();
        if (date != null)
        {
            Uri fullPhotoUri = date.getData();
            Picasso.with(PreviewActivity.this).load(fullPhotoUri).into(preview);
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
