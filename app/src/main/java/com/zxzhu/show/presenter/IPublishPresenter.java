package com.zxzhu.show.presenter;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * Created by zxzhu on 2017/8/18.
 */

public interface IPublishPresenter {
    void upLoad(Context context, String picPath, String miniPicPath, String description, Boolean isAnonymity);
    void upLoad(Context context, String picPath, String miniPicPath, String description,String time, String voicePath, Boolean isAnonymity);
    void getPicInfo(String bitmap64, AssetManager assetManager);
    void initTensorFlow(AssetManager assetManager);
}
