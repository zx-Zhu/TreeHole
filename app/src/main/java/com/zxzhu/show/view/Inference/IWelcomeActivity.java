package com.zxzhu.show.view.Inference;

import android.app.Activity;

/**
 * Created by zxzhu on 2017/8/16.
 */

public interface IWelcomeActivity {
    void autoLogin();
    void jumpToLogin();
    void jumpToMain();
    Activity getActivity();
}
