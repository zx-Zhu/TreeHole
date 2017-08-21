package com.zxzhu.show.presenter;

import com.zxzhu.show.model.MessageModel;
import com.zxzhu.show.view.Inference.IChatActivity;

/**
 * Created by zxzhu on 2017/8/21.
 */

public class ChatPresenter implements IChatPresenter {
    IChatActivity activity;

    public ChatPresenter(IChatActivity activity){
        this.activity = activity;
    }

    @Override
    public void sendMessage(String username, String text) {
        MessageModel.getInstance().sendMessageToUser(username,text);
    }

    @Override
    public void getMessage(MessageModel.MessageListener listener) {
        getMessage(listener);
    }
}
