package com.zxzhu.show.presenter;

import com.zxzhu.show.model.GetDataModel;

/**
 * Created by zxzhu on 2017/8/24.
 */

public interface IUserActivityPresenter {
    void getUserData(String id, GetDataModel.GetObjectListener listener);
    void getFollowees(final String id);
    void getFollowers(final String id);
    void followById(String id);
    void unFollowById(String id);
    void getRecently(String username);
    void getConversation(String username, String nickname);
}
