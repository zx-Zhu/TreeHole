package com.zxzhu.show.presenter;

import android.content.Context;
import android.util.Log;

import com.zxzhu.show.Beans.ImgSenceBean;
import com.zxzhu.show.model.HttpModel;
import com.zxzhu.show.model.IUpLoadModel;
import com.zxzhu.show.model.UpLoadModel;
import com.zxzhu.show.view.Inference.ISquarePublicActivity;

import rx.Subscriber;

/**
 * Created by zxzhu on 2017/8/18.
 */

public class PublishPresenter implements IPublishPresenter {
    private IUpLoadModel upLoadModel = new UpLoadModel();
    private ISquarePublicActivity activity;
    public PublishPresenter(ISquarePublicActivity activity) {
        this.activity = activity;
    }

    @Override
    public void upLoad(Context context, String pic, String miniPic, String description) {
        upLoadModel.upLoadPic(context, pic, miniPic, description, new UpLoadModel.UpLoadListener() {
            @Override
            public void onStart() {
                activity.showDialog();
            }

            @Override
            public void onFinish() {
                activity.back();
            }
        });
    }

    @Override
    public void upLoad(Context context, String picPath, String miniPicPath, String description,String time, String voicePath) {
        upLoadModel.upLoadVoice(context, picPath, miniPicPath, description, voicePath,time, new UpLoadModel.UpLoadListener() {
            @Override
            public void onStart() {
                activity.showDialog();
            }

            @Override
            public void onFinish() {
                activity.back();
            }
        });
    }

    @Override
    public void getPicInfo(String imageBase64) {
        HttpModel.bulid().getPhotoInfo(imageBase64, new Subscriber<ImgSenceBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("888", "onError: "+e.getMessage().toString());
            }

            @Override
            public void onNext(ImgSenceBean imgSenceBean) {
                activity.setImgInfo(imgSenceBean);
                Log.d("888", "onNext: "+imgSenceBean.getError_message());
                activity.hideImgInfoLoading();
            }
        });
    }


}
