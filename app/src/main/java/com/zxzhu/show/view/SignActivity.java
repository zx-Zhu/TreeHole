package com.zxzhu.show.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxzhu.show.R;
import com.zxzhu.show.databinding.ActivitySignBinding;
import com.zxzhu.show.presenter.ISignPresenter;
import com.zxzhu.show.presenter.SignPresenter;
import com.zxzhu.show.units.PermissionUnit;
import com.zxzhu.show.units.base.BaseActivity;
import com.zxzhu.show.view.Inference.ISignActivity;

public class SignActivity extends BaseActivity implements ISignActivity {
    private ActivitySignBinding binding;
    private ISignPresenter presenter;
    private static final int GET_IMAGE = 1;
    private static final int GET_FIXED_IMAGE = 2;
    private Bitmap image = null;
    private TextView title;
    private ImageView back;
    private ProgressDialog dialog;

    @Override
    protected void initData() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign);
        presenter = new SignPresenter(this);
        title = $(R.id.header_title);
        title.setText("注册");
        back = $(R.id.back_header);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void signClick(View view) {
        String username = binding.usernameEditSign.getText().toString();
        String password = binding.passwordEditSign.getText().toString();
        String phone = binding.phoneEditSign.getText().toString();
        if (username.equals("")) {
            toast("用户名不能为空");
            return;
        }
        if (password.equals("")) {
            toast("密码不能为空");
            return;
        }
        if (phone.length() != 11) {
            toast("请输入正确的手机号");
            return;
        }
        String readme = binding.readmeEditSign.getText().toString();
        if (readme.equals("")) {
            readme = null;
        }

        sign(username, password, phone, image, readme);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sign;
    }

    @Override
    public void sign(String username, String password, String phoneNumber, Bitmap icon, String readme) {
        presenter.signup(this, username, password, phoneNumber, icon, readme);
    }

    @Override
    public void showDialog() {
        if (dialog == null) dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(true);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("正在注册");
        dialog.setMessage("稍等");
        dialog.show();
    }

    @Override
    public void hideDialog() {
        if (dialog == null) return;
        dialog.hide();
    }

    @Override
    public void jumpToLogin() {
        finish();
    }

    public void setIcon(View view) {
        if (PermissionUnit.hasDiskPermission(this)) {
            Intent intent;
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GET_IMAGE);
        } else {
            PermissionUnit.askForDiskPermission(this, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_IMAGE:
                if (data != null) {
                    Uri uri = data.getData();
                    presenter.sendGetFixedIntent(uri, GET_FIXED_IMAGE);
                }
                break;
            case GET_FIXED_IMAGE:
                if (data != null) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap == null) break;
                    binding.iconSign.setImageBitmap(bitmap);
                    image = bitmap;
                }
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //已获取权限
                    setIcon(binding.iconSign);
                } else {
                    Log.d("1", "onRequestPermissionsResult: 0000000000");
                    toast("没有权限，请手动开启");
                }
                break;

        }
    }
}
