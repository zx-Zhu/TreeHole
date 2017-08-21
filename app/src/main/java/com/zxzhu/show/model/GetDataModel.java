package com.zxzhu.show.model;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/20.
 */

public class GetDataModel implements IGetDataModel {
    public interface GetDataListener<T extends AVObject> {
        void onStart();
        void onError(AVException e);
        void onFinish(List<T> list);
    }

    @Override
    public void getSquareData(final GetDataListener<AVObject> listener) {
        listener.onStart();
        AVQuery<AVObject> query = new AVQuery<>("Square");
        query.whereContains("username","");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    listener.onFinish(list);
                } else {
                    Log.d("]]]", "done: "+e);
                    listener.onError(e);
                }
            }
        });
    }

    @Override
    public void gerUserData(String username, final GetDataListener<AVUser> listener) {
        listener.onStart();
        AVQuery<AVUser> query = new AVQuery<>("_User");
        query.whereEqualTo("username",username);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    listener.onFinish(list);
                } else {
                    listener.onError(e);
                }
            }
        });
    }
}
