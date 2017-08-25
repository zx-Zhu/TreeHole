package com.zxzhu.show.presenter;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.zxzhu.show.model.GetDataModel;

/**
 * Created by zxzhu on 2017/8/23.
 */

public interface ISquareContentPresenter {
    void getObjectById(String id, GetDataModel.GetObjectListener listener);
    void getComments(AVObject obj);
    void getUserData(String username, GetDataModel.GetDataListener<AVUser> listener);
    void getFollowees(final String id, GetDataModel.GetDataListener<AVUser> listener);
    void followById(String id);
    void unFollowById(String id);
    void getUser(String name, GetDataModel.GetDataListener<AVUser> listener);
}
