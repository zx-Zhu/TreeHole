package com.zxzhu.show.model;

/**
 * Created by zxzhu on 2017/8/21.
 */

public interface IMessageModel {
    void setMessageListener(MessageModel.MessageListener listener);
    void sendMessageToUser(String username, String text);
}
