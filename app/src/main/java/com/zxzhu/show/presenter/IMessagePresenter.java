package com.zxzhu.show.presenter;

import com.avos.avoscloud.AVUser;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.model.MessageModel;

/**
 * Created by zxzhu on 2017/8/21.
 */

public interface IMessagePresenter {
    void getMessage(MessageModel.MessageListener listener);
    void getConversation();
    void getUserData(String username, GetDataModel.GetDataListener<AVUser> listener);
    void getCmds();
}
