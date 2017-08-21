package com.zxzhu.show.presenter;

import com.zxzhu.show.model.MessageModel;

/**
 * Created by zxzhu on 2017/8/21.
 */

public class MainPresenter implements IMainPresenter {

    @Override
    public void getMessage(MessageModel.MessageListener listener) {
        MessageModel.getInstance().setMessageListener(listener);
    }
}
