package com.kangren.cashbook.wallpaper;

import com.kangren.cashbook.BaseActivity;
import com.kangren.cashbook.MainActivity;
import com.kangren.cashbook.R;
import com.kangren.cashbook.util.JumpUtil;
import com.kangren.cashbook.util.ScreenUtil;
import com.kangren.cashbook.util.Utils;
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
    private static final int TITLEBAR_HEIGHT = 44;

    private boolean isResourceId;

    private int resourceId;

    private Uri imgUri;

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
                if (isResourceId)
                {
                    if (resourceId != 0)
                    {
                        Utils.resourceIdSaveFile(PreviewActivity.this, resourceId);
                    }
                    else
                    {
                        Toast.makeText(PreviewActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                    }
                    JumpUtil.jumpActivity(PreviewActivity.this, MainActivity.class);
                }
                else
                {
                    if (imgUri != null)
                    {
                        Utils.uriSaveFile(PreviewActivity.this, imgUri);
                    }
                    else
                    {
                        Toast.makeText(PreviewActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                    }
                    JumpUtil.jumpActivity(PreviewActivity.this, MainActivity.class);
                }
            }
        });
        handleIntent();
    }

    /**
     * 处理传递来的Intent
     */
    private void handleIntent()
    {
        Intent data = getIntent();
        if (data != null)
        {
            int height = (int) (screenUtil.getScreenHeight() - screenUtil.getStatusHeight()
                    - TITLEBAR_HEIGHT * screenUtil.getDensity());
            int width = screenUtil.getWidth();
            Logger.e(
                    "width: " + width + "height: " + height + "titlebar :" + TITLEBAR_HEIGHT * screenUtil.getDensity());
            // 从相册打开图片传递Uri
            Uri fullPhotoUri = data.getData();
            if (fullPhotoUri != null)
            {
                Picasso.with(PreviewActivity.this).load(fullPhotoUri).resize(width, height).into(preview);
                imgUri = fullPhotoUri;
                isResourceId = false;
            }
            else
            {
                // 软件自带的壁纸预览传递的resource ID
                int id = data.getIntExtra("wallpaper", 0);
                if (id == 0)
                {
                    Logger.e("error: intent has no resource id");
                }
                else
                {
                    Picasso.with(PreviewActivity.this).load(id).resize(width, height).into(preview);
                    resourceId = id;
                    isResourceId = true;
                }
            }
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
