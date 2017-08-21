package com.zxzhu.show.presenter;

import com.zxzhu.show.model.MessageModel;

/**
 * Created by zxzhu on 2017/8/21.
 */

public interface IMainPresenter {
    void getMessage(MessageModel.MessageListener listener);
}
