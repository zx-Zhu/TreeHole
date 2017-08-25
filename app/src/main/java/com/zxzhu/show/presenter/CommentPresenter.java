package com.zxzhu.show.presenter;

import com.zxzhu.show.model.IUpLoadModel;
import com.zxzhu.show.model.UpLoadModel;
import com.zxzhu.show.view.Inference.ICommentActivity;

/**
 * Created by zxzhu on 2017/8/23.
 */

public class CommentPresenter implements ICommentPresenter{
    private IUpLoadModel model;
    private ICommentActivity activity;
    public CommentPresenter(ICommentActivity activity) {
        this.activity = activity;
        model = new UpLoadModel();
    }

    @Override
    public void upLoadComment(String objectId, String text, String audioPath) {
        model.upLoadComment(objectId, text, audioPath, new UpLoadModel.UpLoadListener() {
            @Override
            public void onStart() {
                activity.showDialog();
            }

            @Override
            public void onFinish() {
                activity.hideDialog();
                activity.finish();
            }
        });
    }
}
