package com.zxzhu.show.presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.model.IGetDataModel;
import com.zxzhu.show.model.IUserModel;
import com.zxzhu.show.model.UserModel;
import com.zxzhu.show.view.fragments.inference.ISquareFragment;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/20.
 */

public class SquarePresenter implements ISquarePresenter{
    private ISquareFragment fragment;
    private IGetDataModel model;
    private IUserModel userModel;

    public SquarePresenter(ISquareFragment fragment) {
        this.fragment = fragment;
        model = new GetDataModel();
        userModel = new UserModel();
    }

    @Override
    public void getSquareData() {
        model.getSquareData(new GetDataModel.GetDataListener<AVObject>() {
            @Override
            public void onStart() {
                Log.d("getSquareData", "onStart: ");
            }

            @Override
            public void onError(AVException e) {
                Log.d("getSquareDataError", "onError: "+e.getMessage());
            }

            @Override
            public void onFinish(List list) {
                fragment.hideRefresh();
                fragment.setList(list);
            }
        });
    }

    @Override
    public void getUserData(String username, GetDataModel.GetDataListener<AVUser> listener){
        model.gerUserData(username,listener);
    }

    @Override
    public void getRollPics() {
        model.getRollPics(new GetDataModel.GetDataListener<AVObject>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(AVException e) {

            }

            @Override
            public void onFinish(List<AVObject> list) {
                fragment.setRollPics(list);
            }
        });
    }


}
