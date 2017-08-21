package com.zxzhu.show.model;

import com.zxzhu.show.Beans.ImgSenceBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zxzhu on 2017/8/19.
 */

public interface Services {

    @FormUrlEncoded//标识为form表单
    @POST("imagepp/beta/detectsceneandobject")
    Observable<ImgSenceBean> getPhotoInfo(@Field("image_base64") String image_base64, @Field("api_key") String api_key, @Field("api_secret") String api_secret);

}
