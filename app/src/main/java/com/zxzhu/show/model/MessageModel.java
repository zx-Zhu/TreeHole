package com.zxzhu.show.model;

import android.util.Log;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationsQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zxzhu on 2017/8/21.
 */

public class MessageModel extends AVIMMessageHandler implements IMessageModel {
    public static final String TAG = "IMessage";
    private static AVIMClient self;
    private MessageListener listener;
    private static MessageModel modelInstance;

    @Override
    public void getMessageById(String id, AVIMMessagesQueryCallback callback) {
        AVIMConversation conv = self.getConversation(id);
        conv.queryMessages(40,callback);
    }

    @Override
    public AVIMConversation getConversationById(String id) {
        AVIMConversation conversation = self.getConversation(id);
        return conversation;
    }

    public interface MessageListener {
        void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client);
    }

    public interface QueryCallback {
        void start();

        void error(AVIMException e);

        void finish(List<AVIMConversation> list);
    }

    public MessageModel() {
        openClient();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static MessageModel getInstance() {
        if (modelInstance == null) {
            synchronized (MessageModel.class) {
                if (modelInstance == null) {
                    modelInstance = new MessageModel();
                }
            }
        }
        return modelInstance;
    }

    /**
     * 收到消息处理
     *
     * @param message
     * @param conversation
     * @param client
     */
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        Log.d(TAG, "onMessage: " + message.getContent());
        if (listener != null) {
            listener.onMessage(message, conversation, client);
        }
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }

    @Override
    public void setMessageListener(MessageListener listener) {
        Log.d(TAG, "setMessageListener: changeListener");
        this.listener = listener;
        receiveMessage();
    }

    /**
     * 获取当前用户所有会话信息
     * @param callback
     */
    @Override
    public void getConversation(final QueryCallback callback) {
        callback.start();
        AVIMConversationsQuery avimConversationsQuery = self.getConversationsQuery();
        avimConversationsQuery.whereContains("name", AVUser.getCurrentUser().getUsername().toString());
        avimConversationsQuery.setWithLastMessagesRefreshed(true);
        avimConversationsQuery.orderByAscending("updatedAt");
        avimConversationsQuery.setQueryPolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        avimConversationsQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (e == null) {
                    callback.finish(list);
                } else {
                    callback.error(e);
                }
            }
        });
    }

    @Override
    public void getConversationByTargetName(final String username, final QueryCallback callback) {
        callback.start();
        if (username.equals(AVUser.getCurrentUser().getUsername())) return;
        AVIMConversationsQuery avimConversationsQuery = self.getConversationsQuery();
        avimConversationsQuery.whereEqualTo("name", AVUser.getCurrentUser().getUsername()+"&"+username);
        avimConversationsQuery.setQueryPolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        avimConversationsQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (e == null) {
                    callback.finish(list);
                    return;
                } else {
                    AVIMConversationsQuery avimConversationsQuery = self.getConversationsQuery();
                    avimConversationsQuery.whereEqualTo("name", username+"&"+AVUser.getCurrentUser().getUsername());
                    avimConversationsQuery.setQueryPolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
                    avimConversationsQuery.findInBackground(new AVIMConversationQueryCallback() {
                        @Override
                        public void done(List<AVIMConversation> list, AVIMException e) {
                            if (e == null) {
                                callback.finish(list);
                                return;
                            } else {
                                self.createConversation(Arrays.asList(username),
                                        AVUser.getCurrentUser().getUsername()+"&"+username, null,
                                        new AVIMConversationCreatedCallback() {
                                            @Override
                                            public void done(AVIMConversation avimConversation, AVIMException e) {
                                                List<AVIMConversation> conver = new ArrayList<AVIMConversation>();
                                                conver.add(avimConversation);
                                                callback.finish(conver);
                                                return;
                                            }
                                        });
                            }
                        }
                    });

                }
            }
        });
    }


    /**
     * 向指定对话发送消息
     *
     * @param conversation
     * @param text
     */
    @Override
    public void sendMessageToUser(AVIMConversation conversation, final String text) {
        AVIMTextMessage msg = new AVIMTextMessage();
        msg.setText(text);
        // 发送消息
        conversation.sendMessage(msg, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    Log.d(TAG, "发送成功！");
                } else {
                    Log.d(TAG, "error: " + e.getCode() + e.getMessage().toString());
                }
            }
        });
    }

    /**
     * 发送一张图片
     *
     * @param conversation
     * @param path
     */
    @Override
    public void sendPicMessageByConversation(AVIMConversation conversation, final String path) {

        AVIMImageMessage message = null;
        try {
            message = new AVIMImageMessage(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "sendPicMessageByConversation: " + path);
        conversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) Log.d(TAG, "done: 图片发送成功");
                else Log.d(TAG, "done: " + e.getMessage());
            }
        });
    }

    /**
     * 发送音频消息
     * @param conversation
     * @param path
     * @param time
     */
    @Override
    public void sendAudioMessageByConversation(AVIMConversation conversation, String path, String time) {
        AVFile file = null;
        try {
            file = AVFile.withAbsoluteLocalPath(conversation.getName()+"_Audio_"+System.currentTimeMillis(), path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AVIMAudioMessage m = new AVIMAudioMessage(file);
        m.setText(time);
        // 创建一条音频消息
        conversation.sendMessage(m, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    if (e == null) Log.d(TAG, "done: 音频发送成功");
                    else Log.d(TAG, "done: " + e.getMessage());
                }
            }
        });
    }


    /**
     * 初始化即时通讯功能
     */
    private void openClient() {
        // Tom 用自己的名字作为clientId，获取AVIMClient对象实例
        self = AVIMClient.getInstance(AVUser.getCurrentUser().getUsername());
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

    @Override
    public void quiteIM() {
        self.close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                Log.d("IMessage", "done: 退出成功");
            }
        });
        modelInstance = null;
    }

}
