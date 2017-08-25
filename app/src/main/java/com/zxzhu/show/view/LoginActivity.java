package com.zxzhu.show.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.zxzhu.show.R;
import com.zxzhu.show.databinding.ActivityLoginBinding;
import com.zxzhu.show.presenter.ILoginPresenter;
import com.zxzhu.show.presenter.LoginPresenter;
import com.zxzhu.show.units.PermissionUnit;
import com.zxzhu.show.units.base.BaseActivity;
import com.zxzhu.show.view.Inference.ILoginActivity;

public class LoginActivity extends BaseActivity implements ILoginActivity {

    private ActivityLoginBinding binding;
    private ILoginPresenter presenter;
    private TextView title;
    private ProgressDialog dialog;

    @Override
    protected void initData() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        presenter = new LoginPresenter(this);
        title = $(R.id.header_title);
        title.setText("登录");
        PermissionUnit.askForDiskPermission(this,0);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void setLogin(String username, String password) {
        presenter.login(username, password);
    }

    @Override
    public void jumpToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void setSign(View view) {
        Intent intent = new Intent(this, SignActivity.class);
        startActivity(intent);
    }

    public void setLoginClick(View view) {
        String username = binding.loginUsername.getText().toString();
        String password = binding.loginPassword.getText().toString();
        if (!username.equals("") && !password.equals("")) {
            setLogin(username, password);
        } else {
            toast("用户名和密码不能为空");
        }
    }

    public void setQQClick(View view) {
        presenter.setQQLogin();
    }

    public void setWbClick(View view) {
        presenter.setWbLogin();
    }

    @Override
    public void showDialog() {
        if(dialog == null) dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(true);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("正在登录");
        dialog.setMessage("稍等");
        dialog.show();
    }
    @Override
    public void hideDialog() {
        if(dialog == null) return;
        dialog.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    toast("没有SD卡权限，请手动开启");
                }
                break;
        }
    }
}
