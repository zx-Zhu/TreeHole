package com.zxzhu.show.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by zxzhu on 2017/8/16.
 */

public interface ISignPresenter {
    void sendGetFixedIntent(Uri uri, int code);
    void signup(Context context, String username, String password, String phoneNumber, Bitmap icon, String readme);
}
