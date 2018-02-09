package com.kang.cashbook.data;

import java.io.IOException;

import com.google.gson.Gson;
import com.kang.cashbook.data.model.JsonBean;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kangren on 2018/2/9.
 */

public class DataService
{

    private static final int GET_SUCCESS = 0;

    private Context mContext;

    private Gson gson;

    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            if (msg.what == GET_SUCCESS)
            {
                String result = (String) msg.obj;
                JsonBean bean = gson.fromJson(result, JsonBean.class);
                Log.d("asd", bean.getTitle());
            }
            return false;
        }
    });

    /**
     * @param context context.getApplicationContext()
     */
    private DataService(Context context)
    {
        mContext = context;
        gson = new Gson();
    }

    /**
     * 普通Context
     * 
     * @param context every context
     * @return DataService
     */
    public static synchronized DataService get(Context context)
    {
        return new DataService(context.getApplicationContext());
    }

    public void asynGet()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://111.231.249.239:88/home").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                if (response.isSuccessful())
                {
                    String result = response.body().string();
                    Message message = new Message();
                    message.obj = result;
                    message.what = GET_SUCCESS;
                    handler.sendMessage(message);
                }
            }
        });

    }
}
