package com.zxzhu.show.presenter;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.model.IGetDataModel;
import com.zxzhu.show.model.IUserModel;
import com.zxzhu.show.model.UserModel;
import com.zxzhu.show.view.Inference.ISquareContentActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxzhu on 2017/8/23.
 */

public class SquareContentPresenter implements ISquareContentPresenter {
    private IGetDataModel model;
    private IUserModel userModel;
    private ISquareContentActivity activity;

    public SquareContentPresenter(ISquareContentActivity activity) {
        this.activity = activity;
        model = new GetDataModel();
        userModel = new UserModel();
    }

    @Override
    public void getObjectById(String id, GetDataModel.GetObjectListener listener) {
        model.getObjectById(id, listener);
    }

    @Override
    public void getComments(AVObject obj) {
        model.getComments(obj, new GetDataModel.GetCommentsListener() {
            @Override
            public void finish(List<String> list, HashMap<String, Object> map) {
                activity.hideRefresh();
                activity.setList(list, map);
            }
        });
    }

    @Override
    public void getUserData(String username, GetDataModel.GetDataListener<AVUser> listener) {
        model.gerUserData(username, listener);
    }

    @Override
    public void getFollowees(final String id, GetDataModel.GetDataListener<AVUser> listener) {
        model.getFollowees(AVUser.getCurrentUser().getObjectId(), listener);
    }

    @Override
    public void followById(String id) {
        userModel.followUser(id, null);
    }

    @Override
    public void unFollowById(String id) {
        userModel.unFollowUser(id, null);
    }

    public void getUser(String name, GetDataModel.GetDataListener<AVUser> listener) {
        model.gerUserData(name, listener);
    }
}
