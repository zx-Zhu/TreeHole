package com.zxzhu.show.model;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by zxzhu on 2017/8/20.
 */

public interface IGetDataModel {
    void getSquareData(GetDataModel.GetDataListener<AVObject> listener);
    void gerUserData(String username, GetDataModel.GetDataListener<AVUser> listener);
    void getUserById(String id, final GetDataModel.GetObjectListener listener);
    void getObjectById(String id, final GetDataModel.GetObjectListener listener);
    void getComments(AVObject object, GetDataModel.GetCommentsListener listener);
    void getFollowers(String id, GetDataModel.GetDataListener<AVUser> listener);
    void getFollowees(String id, GetDataModel.GetDataListener<AVUser> listener);
    void getRecently(String username, GetDataModel.GetDataListener<AVObject> listener);
}
