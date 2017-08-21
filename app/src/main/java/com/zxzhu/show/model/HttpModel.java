package com.zxzhu.show.model;

import com.zxzhu.show.Beans.ImgSenceBean;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zxzhu on 2017/8/19.
 */

public class HttpModel {
    public static final String URL = "https://api-cn.faceplusplus.com/";
    public static final String API_KEY = "ETQHaN_yiMujAvnTIfmE07QzYo3W9_ze";
    public static final String API_Secret = "PP8fQuvPMVVYLhYxV-tZzGzWIchD0tGT";
    private static final int DEFAULT_TIMEOUT = 5;
    private static Retrofit retrofit;
    private static Services service;
    //private Context context;


    /**
     * 获取单例
     *
     * @return
     */
    private HttpModel() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(URL)
                .build();

        service = retrofit.create(Services.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpModel INSTANCE = new HttpModel();
    }

    //获取单例
    public static HttpModel bulid(){
        return SingletonHolder.INSTANCE;
    }


    /**
     * 获取图像识别数据
     * @param subscriber
     */
    public void getPhotoInfo(String image_base64, Subscriber<ImgSenceBean> subscriber) {
        service.getPhotoInfo(image_base64,API_KEY,API_Secret)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
