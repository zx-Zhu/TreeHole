package com.zxzhu.show.presenter;

import android.graphics.Bitmap;

import com.avos.avoscloud.AVUser;
import com.zxzhu.show.model.IUserModel;
import com.zxzhu.show.model.UserModel;

/**
 * Created by zxzhu on 2017/8/24.
 */

public class ChangePresenter implements IChangePersenter {
    private IUserModel model;
    public ChangePresenter (){
        this.model = new UserModel();
    }
    @Override
    public String saveBitmap(Bitmap bitmap) {
        return model.saveBitmap(bitmap, AVUser.getCurrentUser().getUsername());
    }
}
