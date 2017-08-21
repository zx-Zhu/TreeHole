package com.zxzhu.show.view.Inference;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * Created by zxzhu on 2017/8/16.
 */

public interface ISignActivity {
    void sign(String username, String password, String phoneNumber, Bitmap icon, String readme);
    void jumpToLogin();
    void toast(String str);
    void showDialog();
    void hideDialog();
    Activity getActivity();
}
