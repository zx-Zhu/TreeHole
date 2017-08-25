package com.zxzhu.show.view.Inference;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;

import java.util.List;

/**
 * Created by zxzhu on 2017/8/24.
 */

public interface IUserActivity {
    void setUser(AVUser user);
    void setFollowers(List<AVUser> list);
    void setFollowees(List<AVUser> list);
    void setRecently(List<AVObject> list);
    void setMessage(AVIMConversation conversation, String username, String nickname);
}
