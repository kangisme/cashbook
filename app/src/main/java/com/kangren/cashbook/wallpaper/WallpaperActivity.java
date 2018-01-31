package com.kangren.cashbook.wallpaper;

import com.kangren.cashbook.BaseActivity;
import com.kangren.cashbook.R;
import com.kangren.cashbook.util.Utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kangren on 2018/1/29.
 */

public class WallpaperActivity extends BaseActivity
{

    private static final int REQUEST_IMAGE_GET = 0;

    private RecyclerView recyclerView;

    private int[] wallpapers = new int[] {R.mipmap.bg_overcast, R.mipmap.wallpaper_2, R.mipmap.wallpaper_3,
            R.mipmap.wallpaper_4, R.mipmap.wallpaper_5, R.mipmap.wallpaper_6, R.mipmap.wallpaper_7,
            R.mipmap.wallpaper_8, R.mipmap.wallpaper_9,};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        initView();
    }

    private void initView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        GridLayoutManager glm = new GridLayoutManager(WallpaperActivity.this, 3);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(new SelectAdapter(wallpapers, glm, new ClickListener()
        {
            @Override
            public void onImageClick(int position)
            {
                Intent intent = new Intent(WallpaperActivity.this, PreviewActivity.class);
                Uri uri = Utils.resourceIdToUri(WallpaperActivity.this, wallpapers[position]);
                intent.setData(uri);
                startActivity(intent);
            }
        }));

        findViewById(R.id.select).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_IMAGE_GET);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            Intent intent = new Intent(WallpaperActivity.this, PreviewActivity.class);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private class SelectAdapter extends RecyclerView.Adapter<ViewHolder>
    {
        private int[] mWallPapers;

        private GridLayoutManager manager;

        private ClickListener clickListener;

        public SelectAdapter(int[] wallPapers, GridLayoutManager glm, ClickListener listener)
        {
            mWallPapers = wallPapers;
            manager = glm;
            clickListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView = LayoutInflater.from(WallpaperActivity.this).inflate(R.layout.recycler_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position)
        {
            holder.imageView.setImageResource(wallpapers[position]);
            ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
            int width = manager.getWidth() / manager.getSpanCount()
                    - 2 * ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
            layoutParams.height = width * 5 / 3;
            if (clickListener != null)
            {
                holder.imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        clickListener.onImageClick(position);
                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            return mWallPapers.length;
        }
    }
}
