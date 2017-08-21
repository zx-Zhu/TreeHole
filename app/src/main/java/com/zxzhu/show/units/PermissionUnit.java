package com.zxzhu.show.units;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.zxzhu.show.view.fragments.CameraFragment;

/**
 * Created by zxzhu on 2017/8/16.
 */

public class PermissionUnit {


    public static boolean hasDiskPermission(Activity activity){
        return  (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean hasCameraPermission(Activity activity){
        return  (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean hasMicPermission(Activity activity){
        return  (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    public static void askForDiskPermission(Activity activity, int code) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, code);
        }
    }

    public static void askForDiskPermission(Fragment fragment, int code) {
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, code);
        }
    }

    public static void askForCameraPermission(CameraFragment fragment, int code) {
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            fragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, code);
        }
    }

    public static void askForMicPermission(Activity activity, int code) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, code);
        }
    }
}