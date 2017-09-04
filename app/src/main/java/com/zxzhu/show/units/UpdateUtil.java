package com.zxzhu.show.units;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by zxzhu on 2017/8/25.
 */

public class UpdateUtil {
    private static ProgressDialog dialog;
    public static void checkUpdate(final Context context, final boolean showTip) {
        if (showTip) showDialog(context);
        AVQuery<AVObject> query = new AVQuery<>("Version");
        query.getInBackground("599fabd71b69e6006a466ef3", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    String versionName = getPackageInfo(context).versionName;
                    Log.d("@@@", "done: "+versionName);
                    if (versionName.equals(avObject.get("versionNum"))) {
                        hideDialog();
                        if (showTip) Toast.makeText(context, "已经是最新版本", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        hideDialog();
                        showDialog(context, avObject);
                    }
                } else {
                    hideDialog();
                    Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private static void showDialog(final Context context, final AVObject obj) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("版本更新");
        dialog.setMessage("已有新版本，是否更新");

        dialog.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //确定
                download(context, "http://47.94.206.39/teller/teller.apk");
                Toast.makeText(context, "开始下载, 请注意通知栏", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 获取当前版本号
     *
     * @param context
     * @return
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    private static void download(Context context, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        String path = "/Teller/update/";
        if (setFolder(path)) {
            request.setDestinationInExternalPublicDir(path, "Teller"+System.currentTimeMillis()+".apk");
            request.setTitle("Teller");
            request.setDescription("正在下载最新版");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            long downloadId = downloadManager.enqueue(request);
        }
    }

    private static boolean setFolder(String path){
        File folder = new File(Environment.getExternalStorageDirectory()+path);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    public static void showDialog(Context context) {
        if (dialog == null) dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(true);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("正在检查");
        dialog.setMessage("稍等");
        dialog.show();
    }

    public static void hideDialog() {
        if (dialog == null) return;
        dialog.hide();
    }
}
