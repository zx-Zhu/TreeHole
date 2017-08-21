package com.zxzhu.show.presenter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.avos.avoscloud.AVException;
import com.zxzhu.show.model.IUserModel;
import com.zxzhu.show.model.UserModel;
import com.zxzhu.show.view.Inference.ISignActivity;

/**
 * Created by zxzhu on 2017/8/16.
 */

public class SignPresenter implements ISignPresenter {
    private IUserModel userModel = new UserModel();
    private ISignActivity signActivity;

    public SignPresenter(ISignActivity signActivity) {
        this.signActivity = signActivity;
    }

    @Override
    public void signup(Context context, final String username, final String password, String phoneNumber, Bitmap icon, String readme) {
        signActivity.showDialog();
        userModel.signUp(context, username, password, phoneNumber, icon, readme, new UserModel.UserListener() {
            @Override
            public void onSuccess() {
                signActivity.toast("注册成功");
                signActivity.jumpToLogin();
                signActivity.hideDialog();
            }

            @Override
            public void onError(AVException e) {
                signActivity.hideDialog();
                if (e.getCode() == 127) {
                    signActivity.toast("请输入正确的手机号");
                } else if (e.getCode() == 214) {
                    signActivity.toast("该手机号已被注册");
                } else {
                    signActivity.toast("注册失败:"+e.toString());
                }
            }
        });
    }

    //请求裁剪照片
    @Override
    public void sendGetFixedIntent(Uri uri, int code) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = signActivity.getActivity().managedQuery(uri, proj, null, null, null);
        //int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        //imagePath = cursor.getString(column_index);
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        signActivity.getActivity().startActivityForResult(intent, code);
    }

}
