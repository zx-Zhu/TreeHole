package com.zxzhu.show.model;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by zxzhu on 2017/8/20.
 */

public interface IGetDataModel {
    void getSquareData(GetDataModel.GetDataListener<AVObject> listener);
    void gerUserData(String username, GetDataModel.GetDataListener<AVUser> listener);
}
