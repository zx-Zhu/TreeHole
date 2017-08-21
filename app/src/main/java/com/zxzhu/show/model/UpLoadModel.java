package com.zxzhu.show.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.zxzhu.show.view.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by zxzhu on 2017/8/18.
 */

public class UpLoadModel implements IUpLoadModel {
    private final String TAG = "UpLoadModelTest";
    private static final int PIC = 0, VOICE = 1, VIDEO = 2;
    private final String USERNAME = MainActivity.USER;
    public interface UpLoadListener {
        void onStart();
        void onFinish();
    }


    @Override
    public void upLoadPic(Context context, String picPath, String miniPicPath, String description, final UpLoadListener listener) {
        listener.onStart();
        AVObject avObject = new AVObject("Square");
        AVFile pic = null, picMini = null;
        String namePic = USERNAME + "_pic_" + System.currentTimeMillis();
        String namePicMini = "miniPic_" + namePic;
        try {
            Log.d(TAG, "upLoadPic: "+picPath);
            pic = AVFile.withAbsoluteLocalPath(namePic, picPath);
            picMini = AVFile.withAbsoluteLocalPath(namePicMini,miniPicPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        avObject.put("username",USERNAME);
        avObject.put("content", pic);
        avObject.put("picMini", picMini);
        avObject.put("type", PIC);
        avObject.put("description",description);
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                listener.onFinish();
            }
        });
    }

    @Override
    public String saveBitmap(Bitmap bitmap, String name) {
        Log.d(TAG, "save image path:---------");
        FileOutputStream foutput = null;
        String imagePath = null;
        try {
            File appDir = new File(Environment.getExternalStorageDirectory(), "Show");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File picSquare = new File(appDir, "picSquare");
            if (!picSquare.exists()) {
                picSquare.mkdir();
            }
            File file = new File(picSquare, name + ".jpg");
            foutput = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, foutput);
            imagePath = file.getAbsolutePath();
            Log.d(TAG, "save image path:" + imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    @Override
    public void upLoadVoice(Context context, String picPath, String miniPicPath, String description, String voicePath,String audioTime,final UpLoadListener listener) {
        listener.onStart();
        AVObject avObject = new AVObject("Square");
        AVFile pic = null, picMini = null, voice = null;
        String time = System.currentTimeMillis()+"";
        String namePic = USERNAME + "_pci_" + time;
        String namePicMini = "miniPic_" + namePic;
        String nameVoice = USERNAME + "_voice_" + time;
        try {
            pic = AVFile.withAbsoluteLocalPath(namePic, picPath);
            picMini = AVFile.withAbsoluteLocalPath(namePicMini,miniPicPath);
            voice = AVFile.withAbsoluteLocalPath(nameVoice,voicePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        avObject.put("username",USERNAME);
        avObject.put("content", pic);
        avObject.put("picMini", picMini);
        avObject.put("voice",voice);
        avObject.put("type", VOICE);
        avObject.put("audioTime",audioTime);
        avObject.put("description",description);
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                listener.onFinish();
            }
        });
    }
}
