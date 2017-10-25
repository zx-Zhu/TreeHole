package com.zxzhu.show.view.fragments.inference;

import android.app.Activity;

import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/20.
 */

public interface ISquareFragment {
    void setList(List<AVObject> list);
    void getSquareData();
    void toast(String str);
    Activity getActivity();
    void hideRefresh();
    void setRollPics(List<AVObject> list);
}
