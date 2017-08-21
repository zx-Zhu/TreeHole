package com.zxzhu.show.view.Inference;

import android.app.Activity;

/**
 * Created by zxzhu on 2017/8/17.
 */

public interface IMainActivity {
    void toast(String str);
    Activity getActivity();
    void setViewPager();
    void back();
    void setHeader();
}
