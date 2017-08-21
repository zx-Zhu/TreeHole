package com.zxzhu.show.model;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by zxzhu on 2017/8/18.
 */

public interface IUpLoadModel {
    void upLoadPic(Context context, String picPath, String miniPicPath, String description, UpLoadModel.UpLoadListener loadListener);
    String saveBitmap(Bitmap bitmap, String name);
    void upLoadVoice(Context context, String picPath, String miniPicPath, String description, String voicePath,String audioTime,final UpLoadModel.UpLoadListener listener);
}
