package com.zxzhu.show.presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.model.IGetDataModel;
import com.zxzhu.show.model.IUserModel;
import com.zxzhu.show.model.MessageModel;
import com.zxzhu.show.model.UserModel;
import com.zxzhu.show.view.Inference.IUserActivity;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/24.
 */

public class UserPresenter implements IUserActivityPresenter {
    private IGetDataModel model;
    private IUserModel userModel;
    private IUserActivity activity;

    public UserPresenter(IUserActivity activity) {
        this.activity = activity;
        model = new GetDataModel();
        userModel = new UserModel();
    }

    @Override
    public void getUserData(String id, GetDataModel.GetObjectListener listener) {
        model.getUserById(id, listener);
    }

    @Override
    public void getFollowees(String id) {
        model.getFollowees(id, new GetDataModel.GetDataListener<AVUser>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(AVException e) {
                Log.d("---", "onError: "+e.getMessage());
            }

            @Override
            public void onFinish(List<AVUser> list) {
                activity.setFollowees(list);
            }
        });
    }

    @Override
    public void getFollowers(String id) {
        model.getFollowers(id, new GetDataModel.GetDataListener<AVUser>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(AVException e) {
                Log.d("---", "onError: "+e.getMessage());
            }

            @Override
            public void onFinish(List<AVUser> list) {
                activity.setFollowers(list);
            }
        });
    }

    @Override
    public void followById(String id) {
        userModel.followUser(id, new UserModel.UserListener() {
            @Override
            public void onSuccess() {
                Log.d("---", "关注成功");
            }

            @Override
            public void onError(AVException e) {
                Log.d("---", "onError: "+e.getMessage());
            }
        });
    }

    @Override
    public void unFollowById(String id) {
        userModel.unFollowUser(id, new UserModel.UserListener() {
            @Override
            public void onSuccess() {
                Log.d("---", "onSuccess: 取关成功");
            }

            @Override
            public void onError(AVException e) {
                Log.d("---", "onError: "+e.getMessage());
            }
        });
    }

    @Override
    public void getRecently(String username) {
        model.getRecently(username, new GetDataModel.GetDataListener<AVObject>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(AVException e) {
                Log.d("---", "onError: "+e.getMessage());
            }

            @Override
            public void onFinish(List<AVObject> list) {
                activity.setRecently(list);
            }
        });
    }

    @Override
    public void getConversation(final String username, final String nickname) {
        MessageModel.getInstance().getConversationByTargetName(username, new MessageModel.QueryCallback() {
            @Override
            public void start() {

            }

            @Override
            public void error(AVIMException e) {
                Log.d("---", "error: "+e.getMessage());
            }

            @Override
            public void finish(List<AVIMConversation> list) {
                activity.setMessage(list.get(0), username, nickname);

            }
        });
    }
}
