package com.zxzhu.show.view.fragments.inference;

import com.avos.avoscloud.im.v2.AVIMConversation;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/21.
 */

public interface IMessageFragment {
    void setIM();
    void hideRefresh();
    void setList(List<AVIMConversation> list);
}
