package com.zxzhu.show.view.Inference;

import com.zxzhu.show.Beans.ImgSenceBean;

/**
 * Created by zxzhu on 2017/8/19.
 */

public interface ISquarePublicActivity {
    void publish();
    String getDescription();
    void setVoice();
    void setVoicePath();
    void showDialog();
    void hideDialog();
    void setImgInfo(ImgSenceBean imgSenceBean);
    void hideImgInfoLoading();
    void back();
}
