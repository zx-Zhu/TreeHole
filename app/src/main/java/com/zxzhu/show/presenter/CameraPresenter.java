package com.zxzhu.show.presenter;

import android.graphics.Bitmap;

import com.zxzhu.show.model.IUpLoadModel;
import com.zxzhu.show.model.UpLoadModel;
import com.zxzhu.show.view.fragments.inference.ICameraFragment;

/**
 * Created by zxzhu on 2017/8/19.
 */

public class CameraPresenter implements ICameraPresenter {
    private IUpLoadModel model;
    private ICameraFragment fragment;
    public CameraPresenter(ICameraFragment fragment){
        this.fragment = fragment;
        model = new UpLoadModel();
    }
    @Override
    public String saveBitmap(Bitmap bitmap, String name) {
        return model.saveBitmap(bitmap,name);
    }
}
