package com.zxzhu.show.presenter;

import com.avos.avoscloud.AVUser;
import com.zxzhu.show.model.GetDataModel;

/**
 * Created by zxzhu on 2017/8/20.
 */

public interface ISquarePresenter {
    void getSquareData();
    void getUserData(String username, GetDataModel.GetDataListener<AVUser> listener);

}
