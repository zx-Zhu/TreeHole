package com.zxzhu.show.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.avos.avoscloud.AVUser;

/**
 * Created by zxzhu on 2017/8/18.
 */

public interface IUpLoadModel {
    void upLoadPic(Context context, String picPath, String miniPicPath, String description, Boolean isAnonymity, UpLoadModel.UpLoadListener loadListener);
    String saveBitmap(Bitmap bitmap, String name);
    void upLoadVoice(Context context, String picPath, String miniPicPath, String description, String voicePath, String audioTime, Boolean isAnonymity, final UpLoadModel.UpLoadListener listener);
    void upLoadComment(String objectId, String text, String audioPath, UpLoadModel.UpLoadListener loadListener);
    void gerUserData(String username, final GetDataModel.GetDataListener<AVUser> listener);
}
