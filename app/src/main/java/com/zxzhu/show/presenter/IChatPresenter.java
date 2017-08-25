package com.zxzhu.show.presenter;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.model.MessageModel;

/**
 * Created by zxzhu on 2017/8/21.
 */

public interface IChatPresenter {
    void sendMessage(AVIMConversation conversation, String text);
    void getMessage(String id);
    void refreshMessage(MessageModel.MessageListener listener);
    AVIMConversation getConversation(String id);
    void sendPicMessage(AVIMConversation conversation, String path);
    void sendAudioMessage(AVIMConversation conversation, String path, String time);
    void getUserData(String username, GetDataModel.GetDataListener<AVUser> listener);
}
