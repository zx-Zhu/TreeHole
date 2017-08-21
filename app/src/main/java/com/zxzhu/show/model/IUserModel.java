package com.zxzhu.show.model;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by zxzhu on 2017/8/16.
 */

public interface IUserModel {
    void login(String username, String password, UserModel.UserListener listener);
    void signUp(Context context, String username, String password, String phoneNumber, Bitmap icon, String readme, UserModel.UserListener listener);
    void qqSign(String id, String nickname, String icon, String readme, UserModel.UserListener listener);
    void wbSign(String id, String nickname, String icon, String readme, UserModel.UserListener listener);
    void setIcon(String icon);
    void setReadme(String readme);
    Bitmap getIcon(String username);
    String getPhoneNumber(String username);
    void changePassword(String username, String password_pre, String password_new);
    String saveBitmap(Bitmap bitmap, String name);
}
