package com.zxzhu.show.view.Inference;

import com.avos.avoscloud.im.v2.AVIMMessage;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/21.
 */

public interface IChatActivity {
    void setBar();
    void sendMessage();
    void sendPicMessage();
    void sendAudioMessage(String time);
    void setAudio();
    void getMessage();
    void setList(List<AVIMMessage> list);
    void toast(String str);
    void refreshMessage();
}
