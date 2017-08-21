package com.zxzhu.show.presenter;

import com.zxzhu.show.model.MessageModel;
import com.zxzhu.show.view.fragments.inference.IMessageFragment;

/**
 * Created by zxzhu on 2017/8/21.
 */

public class MessagePresenter implements IMessagePresenter {
    IMessageFragment fragment;

    public MessagePresenter(IMessageFragment fragment){
        this.fragment = fragment;
    }

    @Override
    public void getMessage(MessageModel.MessageListener listener) {
        MessageModel.getInstance().setMessageListener(listener);
    }

}
