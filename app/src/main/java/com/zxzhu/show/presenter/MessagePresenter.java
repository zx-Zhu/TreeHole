package com.zxzhu.show.presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.model.IGetDataModel;
import com.zxzhu.show.model.MessageModel;
import com.zxzhu.show.view.fragments.inference.IMessageFragment;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/21.
 */

public class MessagePresenter implements IMessagePresenter {
    IMessageFragment fragment;
    IGetDataModel model;

    public MessagePresenter(IMessageFragment fragment){
        this.fragment = fragment;
        model = new GetDataModel();
    }

    @Override
    public void getMessage(MessageModel.MessageListener listener) {
        MessageModel.getInstance().setMessageListener(listener);
    }

    public void getConversation(){
        MessageModel.getInstance().getConversation(new MessageModel.QueryCallback() {
            @Override
            public void start() {

            }

            @Override
            public void error(AVIMException e) {
                fragment.hideRefresh();
                Log.d("MessagePresenterTest", "error: "+e.getCode()+e.getMessage().toString());
            }

            @Override
            public void finish(List<AVIMConversation> list) {
                fragment.setList(list);
                fragment.hideRefresh();
            }
        });
    }
    @Override
    public void getUserData(String username, GetDataModel.GetDataListener<AVUser> listener){

        model.gerUserData(username,listener);
    }

    @Override
    public void getCmds() {
        model.getRollPics(new GetDataModel.GetDataListener<AVObject>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(AVException e) {

            }

            @Override
            public void onFinish(List<AVObject> list) {
                fragment.setCmd(list);
            }
        });
    }
}
