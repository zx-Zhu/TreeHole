package com.zxzhu.show.presenter;

import com.avos.avoscloud.AVUser;
import com.zxzhu.show.model.IUserModel;
import com.zxzhu.show.model.UserModel;
import com.zxzhu.show.view.Inference.IWelcomeActivity;

/**
 * Created by zxzhu on 2017/8/16.
 */

public class WelcomePresenter implements IwelcomePresenter {
    private static final String TAG = "WelcomePresenter";
    private IWelcomeActivity welcomeActivity;
    private IUserModel userModel = new UserModel();
    public interface AutoLoginListener {
        void onSuccess();
        void onFail();
    }

    public WelcomePresenter(IWelcomeActivity welcomeActivity) {
        this.welcomeActivity = welcomeActivity;
    }
    @Override
    public void autoLogin(final AutoLoginListener listener) {
        if (AVUser.getCurrentUser() != null) {
            listener.onSuccess();
        } else {
            listener.onFail();
        }
    }
}
