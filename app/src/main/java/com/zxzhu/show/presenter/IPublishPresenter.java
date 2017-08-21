package com.zxzhu.show.presenter;

import android.content.Context;

/**
 * Created by zxzhu on 2017/8/18.
 */

public interface IPublishPresenter {
    void upLoad(Context context, String picPath, String miniPicPath, String description);
    void upLoad(Context context, String picPath, String miniPicPath, String description,String time, String voicePath);
    void getPicInfo(String imageBase64);
}
