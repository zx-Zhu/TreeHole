package com.zxzhu.show.presenter;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.model.IGetDataModel;
import com.zxzhu.show.model.MessageModel;
import com.zxzhu.show.view.Inference.IChatActivity;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/21.
 */

public class ChatPresenter implements IChatPresenter {
    IChatActivity activity;
    private IGetDataModel getDataModel;

    public ChatPresenter(IChatActivity activity){
        this.activity = activity;
        getDataModel = new GetDataModel();
    }

    @Override
    public void sendMessage(AVIMConversation conversation, String text) {
        MessageModel.getInstance().sendMessageToUser(conversation,text);
    }

    @Override
    public void getMessage(final String id) {
        MessageModel.getInstance().getMessageById(id,new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (e == null) {
                    activity.setList(list);
                } else {
                    activity.toast(id+"获取数据失败"+e.getMessage());
                }
            }
        });
    }

    @Override
    public void refreshMessage(MessageModel.MessageListener listener) {
        MessageModel.getInstance().setMessageListener(listener);
    }

    @Override
    public AVIMConversation getConversation(String id) {
        return MessageModel.getInstance().getConversationById(id);
    }

    @Override
    public void sendPicMessage(AVIMConversation conversation, String path) {
        MessageModel.getInstance().sendPicMessageByConversation(conversation,path);
    }

    @Override
    public void sendAudioMessage(AVIMConversation conversation, String path, String time) {
        MessageModel.getInstance().sendAudioMessageByConversation(conversation,path, time);
    }

    @Override
    public void getUserData(String username, GetDataModel.GetDataListener<AVUser> listener) {
        getDataModel.gerUserData(username, listener);
    }
}
