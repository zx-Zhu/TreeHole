package com.zxzhu.show.model;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxzhu on 2017/8/20.
 */

public class GetDataModel implements IGetDataModel {
    public interface GetDataListener<T> {
        void onStart();

        void onError(AVException e);

        void onFinish(List<T> list);
    }

    @Override
    public void getSquareData(final GetDataListener<AVObject> listener) {
        listener.onStart();
        AVQuery<AVObject> query = new AVQuery<>("Square");
        query.whereContains("username", "");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    listener.onFinish(list);
                } else {
                    Log.d("]]]", "done: " + e);
                    listener.onError(e);
                }
            }
        });
    }


    @Override
    public void gerUserData(String username, final GetDataListener<AVUser> listener) {
        listener.onStart();
        AVQuery<AVUser> query = new AVQuery<>("_User");
        query.whereEqualTo("username", username);
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

    @Override
    public void getUserById(String id, final GetObjectListener listener) {
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject object, AVException e) {
                if (e == null) {
                    listener.done(object);
                } else {
                    Log.d("'''", "done: " + e.getMessage());
                }
            }
        });
    }

    public interface GetObjectListener {
        void done(AVObject obj);
    }

    @Override
    public void getObjectById(String id, final GetObjectListener listener) {
        AVQuery<AVObject> query = new AVQuery<>("Square");
        query.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject object, AVException e) {
                if (e == null) {
                    listener.done(object);
                } else {
                    Log.d("'''", "done: " + e.getMessage());
                }
            }
        });
    }

    public interface GetCommentsListener {
        void finish(List<String> list, HashMap<String, Object> map);
    }

    @Override
    public void getComments(AVObject object, GetCommentsListener listener) {
        List<String> list = (List<String>) object.get("comment_tx");
        HashMap<String, Object> map = (HashMap<String, Object>) object.get("comment_audio");
        listener.finish(list, map);
    }

    @Override
    public void getFollowers(String id, final GetDataListener<AVUser> listener) {
        listener.onStart();
        AVQuery<AVUser> followerQuery = AVUser.followerQuery(id, AVUser.class);
        followerQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> avObjects, AVException e) {
                if (e == null) {
                    listener.onFinish(avObjects);
                } else {
                    listener.onError(e);
                }
            }
        });
    }

    @Override
    public void getFollowees(String id, final GetDataListener<AVUser> listener) {
        listener.onStart();
        AVQuery<AVUser> followerQuery = AVUser.followeeQuery(id, AVUser.class);
        followerQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> avObjects, AVException e) {
                if (e == null) {
                    listener.onFinish(avObjects);
                } else {
                    listener.onError(e);
                }
            }
        });
    }

    @Override
    public void getRecently(String username, final GetDataListener<AVObject> listener) {
        listener.onStart();
        AVQuery<AVObject> query = new AVQuery<>("Square");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.d("]]]", "done: " + list.size());
                    listener.onFinish(list);
                } else {
                    Log.d("]]]", "done: " + e);
                    listener.onError(e);
                }
            }
        });
    }

    @Override
    public void getRollPics(final GetDataListener<AVObject> listener) {
        listener.onStart();
        AVQuery<AVObject> query = new AVQuery<>("RollPics");
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    listener.onFinish(list);
                } else {
                    listener.onError(e);
                }
            }
        });
    }


}
