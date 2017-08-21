package com.zxzhu.show.view.Inference;

import android.app.Activity;

/**
 * Created by zxzhu on 2017/8/16.
 */

public interface ILoginActivity {
    void setLogin(String username , String password);
    void jumpToMain();
    void toast(String str);
    Activity getActivity();
    void showDialog();
    void hideDialog();
}
