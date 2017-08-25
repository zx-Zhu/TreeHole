package com.zxzhu.show.model;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;

/**
 * Created by zxzhu on 2017/8/21.
 */

public interface IMessageModel {
    void setMessageListener(MessageModel.MessageListener listener);
    void sendMessageToUser(AVIMConversation conversation, String text);
    void quiteIM();
    void getMessageById(String id, AVIMMessagesQueryCallback callback);
    AVIMConversation getConversationById(String id);
    void getConversation(final MessageModel.QueryCallback callback);
    void getConversationByTargetName(final String username, final MessageModel.QueryCallback callback);
    void sendPicMessageByConversation(AVIMConversation conversation, final String path);
    void sendAudioMessageByConversation(AVIMConversation conversation, String path, String time);
}
