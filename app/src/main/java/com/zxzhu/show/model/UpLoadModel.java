package com.zxzhu.show.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.zxzhu.show.view.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public void upLoadPic(Context context, String picPath, String miniPicPath, String description, Boolean isAnonymity, final UpLoadListener listener) {
        listener.onStart();
        AVObject avObject = new AVObject("Square");
        AVFile pic = null, picMini = null;
        String namePic = USERNAME + "_pic_" + System.currentTimeMillis();
        String namePicMini = "miniPic_" + namePic;
        try {
            Log.d(TAG, "upLoadPic: " + picPath);
            pic = AVFile.withAbsoluteLocalPath(namePic, picPath);
            picMini = AVFile.withAbsoluteLocalPath(namePicMini, miniPicPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (isAnonymity) avObject.put("username", "匿名用户");
        else avObject.put("username", USERNAME);
        avObject.put("content", pic);
        avObject.put("picMini", picMini);
        avObject.put("type", PIC);
        avObject.put("description", description);
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
            File appDir = new File(Environment.getExternalStorageDirectory(), "Teller");
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
    public void upLoadVoice(Context context, String picPath, String miniPicPath, String description, String voicePath, String audioTime, Boolean isAnonymity, final UpLoadListener listener) {
        listener.onStart();
        AVObject avObject = new AVObject("Square");
        AVFile pic = null, picMini = null, voice = null;
        String time = System.currentTimeMillis() + "";
        String namePic = USERNAME + "_pci_" + time;
        String namePicMini = "miniPic_" + namePic;
        String nameVoice = USERNAME + "_voice_" + time;
        try {
            pic = AVFile.withAbsoluteLocalPath(namePic, picPath);
            picMini = AVFile.withAbsoluteLocalPath(namePicMini, miniPicPath);
            voice = AVFile.withAbsoluteLocalPath(nameVoice, voicePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (isAnonymity) avObject.put("username", "匿名用户");
        else avObject.put("username", USERNAME);
        avObject.put("content", pic);
        avObject.put("picMini", picMini);
        avObject.put("voice", voice);
        avObject.put("type", VOICE);
        avObject.put("audioTime", audioTime);
        avObject.put("description", description);
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                listener.onFinish();
            }
        });
    }

    @Override
    public void upLoadComment(String objectId, final String text, final String audioPath, final UpLoadListener loadListener) {
        loadListener.onStart();
        getObjectById(objectId, new GetDataModel.GetObjectListener() {
            @Override
            public void done(final AVObject obj) {
                if (obj == null) return;
                List<String> list_tx = new ArrayList<>();
                HashMap<String, Object> map_audio = new HashMap<>();
                String time = String.valueOf(System.currentTimeMillis());
                if (obj.get("comment_tx") != null) {
                    list_tx = (List<String>) obj.get("comment_tx");
                }
                if (obj.get("comment_audio") != null) {
                    map_audio = (HashMap<String, Object>) obj.get("comment_audio");
                }
                list_tx.add("[" + USERNAME + "]" + "(" + time + ")" + "{" + text + "}");
                final String fileName = "comment_" + USERNAME + time;
                AVFile avFile = null;
                if (audioPath != null) {
                    try {
                        avFile = AVFile.withAbsoluteLocalPath(fileName, audioPath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final HashMap<String, Object> finalMap_audio = map_audio;
                    final AVFile finalAvFile = avFile;
                    avFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            finalMap_audio.put(fileName, finalAvFile.getObjectId());
                            obj.put("comment_audio", finalMap_audio);
                            obj.saveInBackground();
                        }
                    });

                }
                obj.put("comment_tx", list_tx);
//                obj.put("comment_audio", map_audio);
                obj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            loadListener.onFinish();
                        } else Log.d("sss", "done: " + e.getMessage());
                    }
                });
            }
        });
    }

    public void getObjectById(String id, final GetDataModel.GetObjectListener listener) {
        AVQuery<AVObject> query = new AVQuery<>("Square");
        query.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject object, AVException e) {
                if (e == null) {
                    listener.done(object);
                }
            }
        });
    }

    @Override
    public void gerUserData(String username, final GetDataModel.GetDataListener<AVUser> listener) {
        listener.onStart();
        AVQuery<AVUser> query = new AVQuery<>("_User");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    listener.onFinish(list);
                } else {
                    listener.onError(e);
                }
            }
        });
    }

}
