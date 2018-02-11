package com.kang.cashbook.data;

import java.io.IOException;

import com.google.gson.Gson;
import com.kang.cashbook.data.model.JsonBean;
import com.orhanobut.logger.Logger;

import android.content.Context;

import okhttp3.Call;
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
     * @param context any context
     * @return DataService
     */
    public static synchronized DataService get(Context context)
    {
        return new DataService(context.getApplicationContext());
    }

    /**
     * 获取url返回的数据
     * 
     * @param url 请求url
     * @return String类型结果
     */
    public String data(String url)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        String result = null;
        try
        {
            Response response = call.execute();
            if (response.isSuccessful())
            {
                result = response.body().string();
            }
            else
            {
                Logger.e("response is unsuccessful");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Logger.e("okhttp execute io exception");
        }
        return result;
    }

    /**
     * 获取url返回的数据，并格式化
     * 
     * @param url 请求url
     * @return JsonBean类型结果
     */
    public JsonBean dataFormat(String url)
    {
        String result = data(url);
        JsonBean bean = null;
        if (result != null)
        {
            bean = gson.fromJson(result, JsonBean.class);
        }
        return bean;
    }
}
