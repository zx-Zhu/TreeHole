package com.zxzhu.show.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zxzhu.show.R;
import com.zxzhu.show.databinding.ActivityChangeDataBinding;
import com.zxzhu.show.presenter.ChangePresenter;
import com.zxzhu.show.presenter.IChangePersenter;
import com.zxzhu.show.units.PermissionUnit;
import com.zxzhu.show.units.base.BaseActivity;

import java.io.FileNotFoundException;
import java.util.List;

public class ChangeDataActivity extends BaseActivity {
    private ActivityChangeDataBinding b;
    private String result;
    private IChangePersenter presenter;
    private ProgressDialog dialog;

    @Override
    protected void initData() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_change_data);
        presenter = new ChangePresenter();
        TextView title = $(R.id.header_title);
        title.setText("修改资料");
        ImageView back = $(R.id.back_header);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (AVUser.getCurrentUser() != null) {
            setData();
        }
        b.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void setData() {
        AVFile head = AVUser.getCurrentUser().getAVFile("head");
        Glide.with(this).load(head.getUrl()).crossFade().into(b.iconChange);
        if (AVUser.getCurrentUser().get("readme") != null) {
            b.readmeEditChange.setText(AVUser.getCurrentUser().get("readme").toString());
        }
        b.usernameEditChange.setText(AVUser.getCurrentUser().get("nickname").toString());
    }

    private void save() {
        AVFile head = null;
        try {
            head = AVFile.withAbsoluteLocalPath(AVUser.getCurrentUser().getUsername(), result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (head != null) {
            AVUser.getCurrentUser().put("head", head);
        }
        if (b.usernameEditChange.getText() != null) {
            AVUser.getCurrentUser().put("nickname", b.usernameEditChange.getText().toString());
        } else toast("昵称不能为空");
        if (b.readmeEditChange.getText() != null) {
            AVUser.getCurrentUser().put("readme", b.readmeEditChange.getText().toString());
        }
        showDialog();
        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    hideDialog();
                    toast("保存成功");
                }
            }
        });
    }

    public void showDialog() {
        if (dialog == null) dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(true);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("正在修改");
        dialog.setMessage("稍等");
        dialog.show();
    }

    public void hideDialog() {
        if (dialog == null) return;
        dialog.hide();
    }

    public void sendGetFixedIntent(Uri uri, int code) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.managedQuery(uri, proj, null, null, null);
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
        this.startActivityForResult(intent, code);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_change_data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (data != null) {
                    List<Uri> uris = Matisse.obtainResult(data);
                    Uri uri = uris.get(0);
                    sendGetFixedIntent(uri, 1);
                }
                break;
            case 1:
                if (data != null) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap == null) break;
                    b.iconChange.setImageBitmap(bitmap);
                    result = presenter.saveBitmap(bitmap);
                }
                break;
        }
    }

    public void setIcon(View view) {
        if (PermissionUnit.hasDiskPermission(this)) {
            Matisse.from(this)
                    .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                    .countable(true)
                    .maxSelectable(1)
                    .gridExpectedSize(400)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .theme(R.style.Matisse_Dracula)
                    .imageEngine(new GlideEngine())
                    .forResult(0);
        } else {
            PermissionUnit.askForDiskPermission(this, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //已获取权限
                    setIcon(b.iconChange);
                } else {
                    Log.d("1", "onRequestPermissionsResult: 0000000000");
                    toast("没有权限，请手动开启");
                }
                break;

        }
    }
}
