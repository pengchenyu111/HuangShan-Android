package com.example.huangshan.http;

import com.example.huangshan.constans.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit单例管理
 */
public class RetrofitManager {

    private static RetrofitManager sInstance;
    private Retrofit mRetrofit;


    /**
     * 静态初始化RetrofitManager
     * @return
     */
    public static RetrofitManager getInstance() {
        if (null == sInstance) {
            synchronized (RetrofitManager.class) {
                if (null == sInstance) {
                    sInstance = new RetrofitManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化mRetrofit
     */
    public void init() {
        if(mRetrofit == null) {
            //初始化一个OkHttpClient
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(30000, TimeUnit.MILLISECONDS)
                    .readTimeout(30000, TimeUnit.MILLISECONDS)
                    .writeTimeout(30000, TimeUnit.MILLISECONDS);
            OkHttpClient okHttpClient = builder.build();

            //使用该OkHttpClient创建一个Retrofit对象
            mRetrofit = new Retrofit.Builder()
                    //添加Gson数据格式转换器支持
                    .addConverterFactory(GsonConverterFactory.create())
                    //添加RxJava语言支持
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    //指定网络请求client
                    .client(okHttpClient)
                    .baseUrl(Constant.URL)
                    .build();
        }
    }

    /**
     * 获取一个Retrofit实例
     * @return
     */
    public Retrofit getRetrofit() {
        if(mRetrofit == null) {
            throw  new IllegalStateException("Retrofit instance hasn't init!");
        }
        return mRetrofit;
    }
}