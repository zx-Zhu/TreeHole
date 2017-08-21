package com.zxzhu.show.model;

import android.util.Log;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.Arrays;

/**
 * Created by zxzhu on 2017/8/21.
 */

public class MessageModel extends AVIMMessageHandler implements IMessageModel {
    public static final String TAG = "IMessage";
    private static AVIMClient self;
    private MessageListener listener;
    private static MessageModel modelInstance;
    public interface MessageListener {
        void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client);
    }

    public MessageModel(){
        openClient();
    }

    /**
     * 获取单例
     * @return
     */
    public static MessageModel getInstance(){
        if (modelInstance == null) {
            synchronized (MessageModel.class) {
                if (modelInstance == null) {
                    modelInstance = new MessageModel();
                }
            }
        }return modelInstance;
    }

    /**
     * 收到消息处理
     * @param message
     * @param conversation
     * @param client
     */
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        if (listener!=null) {
            listener.onMessage(message, conversation, client);
        }
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }

    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }


    /**
     * 向指定用户发送消息
     * @param username
     * @param text
     */
    @Override
    public void sendMessageToUser(final String username, final String text) {
        // 创建与Jerry之间的对话
        self.createConversation(Arrays.asList(username), AVUser.getCurrentUser()+"&"+username,
                null, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation conversation, AVIMException e) {
                        if (e == null) {
                            AVIMTextMessage msg = new AVIMTextMessage();
                            msg.setText(text);
                            // 发送消息
                            conversation.sendMessage(msg, new AVIMConversationCallback() {

                                @Override
                                public void done(AVIMException e) {
                                    if (e == null) {
                                        Log.d(TAG, "发送成功！");
                                    }
                                }
                            });
                        }
                    }
                });
    }

    /**
     * 初始化即时通讯功能
     */
    private void openClient(){
        // Tom 用自己的名字作为clientId，获取AVIMClient对象实例
        receiveMessage();
        self = AVIMClient.getInstance(AVUser.getCurrentUser());
        // 与服务器连接
        self.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    Log.d(TAG, "done: 登录成功");
                }
            }
        });
    }

    /**
     * 开启消息接收
     */
    public void receiveMessage() {
        AVIMMessageManager.registerDefaultMessageHandler(this);
    }

}
