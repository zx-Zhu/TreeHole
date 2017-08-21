package com.zxzhu.show.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * Created by zxzhu on 2017/8/16.
 */

public class UserModel implements IUserModel {

    public interface UserListener {
        void onSuccess();
        void onError(AVException e);
    }
    @Override
    public void login(String username, String password, final UserListener listener) {
            AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        listener.onSuccess();
                    } else {
                        listener.onError(e);
                    }
                }
            });
    }

    @Override
    public void signUp(Context context, final String username, String password, String phoneNumber, Bitmap icon, String readme, final UserModel.UserListener listener) {
        AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setMobilePhoneNumber(phoneNumber);
        user.put("nickname",username);
        if (readme != "")
        user.put("readme",readme);
        String iconPath = saveBitmap(icon,username);
        Log.d("zzzx", "signUp: "+iconPath);
        AVFile avFile = null;
        try {
            avFile = AVFile.withAbsoluteLocalPath(username,iconPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (iconPath != null) user.put("head",avFile);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    listener.onSuccess();
                } else {
                    listener.onError(e);
                }
            }
        });
        user.saveInBackground();
    }

    @Override
    public void qqSign(String id, String nickname, String icon, String readme, final UserListener listener) {
        AVUser user = new AVUser();
        user.setUsername("QQ用户"+id);
        user.setPassword(id);
        user.put("nickname",nickname);
        if (readme != "")
            user.put("readme",readme);
        AVFile avFile = null;
        if (icon != null)
        {
            avFile = new AVFile("QQ用户"+id, icon, new HashMap<String, Object>());
        }
        if(avFile != null) user.put("head",avFile);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    listener.onSuccess();
                } else {
                    listener.onError(e);
                }
            }
        });
        user.saveInBackground();
    }

    @Override
    public void wbSign(String id, String nickname, String icon, String readme, final UserListener listener) {
        AVUser user = new AVUser();
        user.setUsername("微博用户"+id);
        user.setPassword(id);
        user.put("nickname",nickname);
        if (readme != "")
            user.put("readme",readme);
        AVFile avFile = null;
        if (icon != null)
        {
            avFile = new AVFile("微博用户"+id, icon, new HashMap<String, Object>());
        }
        if(avFile != null) user.put("head",avFile);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    listener.onSuccess();
                } else {
                    listener.onError(e);
                }
            }
        });
        user.saveInBackground();
    }

    @Override
    public void setIcon(String icon) {

    }

    @Override
    public void setReadme(String readme) {

    }

    @Override
    public Bitmap getIcon(String username) {
        return null;
    }

    @Override
    public String getPhoneNumber(String username) {
        return null;
    }

    @Override
    public void changePassword(String username, String password_pre, String password_new) {

    }

    @Override
    public String saveBitmap(Bitmap bitmap, String name) {
        FileOutputStream foutput = null;
        String imagePath = null;
        try {
            File appDir = new File(Environment.getExternalStorageDirectory(), "Show");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File headIcons = new File(appDir, "head");
            if (!headIcons.exists()) {
                headIcons.mkdir();
            }
            File file = new File(headIcons, name+".jpg");
            foutput = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, foutput);
            imagePath = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return imagePath;
    }
}
