package com.zxzhu.show.view;

import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.zxzhu.show.R;
import com.zxzhu.show.databinding.ActivityChangePasswordBinding;
import com.zxzhu.show.units.base.BaseActivity;

public class ChangePasswordActivity extends BaseActivity {
    private ActivityChangePasswordBinding b;
    public static final String TAG = "ChangePasswordActivityTest";

    @Override
    protected void initData() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        setCode();
        TextView title = $(R.id.header_title);
        title.setText("修改密码");
        ImageView back = $(R.id.back_header);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setCode() {
        b.btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                if (b.phoneEdit.getText() != null) {
                    String phone = b.phoneEdit.getText().toString();
                    if (phone.length() != 11) {
                        toast("请输入正确的手机号");
                        return;
                    } else {
                        AVUser.requestPasswordResetBySmsCodeInBackground(phone, new RequestMobileCodeCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    toast("发送成功");
                                    changePassword();
                                } else {
                                    Log.d(TAG, "done: " + e.getCode());
                                    if (e.getCode() == 213 || e.getCode() == 215) {
                                        toast("请输入正确的手机号");
                                    }
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void changePassword() {
        b.savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick:2 ");
                String password = null, passwordSecond = null, code = null;
                if (b.passwordChange.getText() != null) {
                    password = b.passwordChange.getText().toString();
                } else {
                    toast("请输入验证码");
                    return;
                }
                if (b.passwordChange.getText() != null) {
                    password = b.passwordChange.getText().toString();
                }
                if (b.passwordChangeSecone.getText() != null) {
                    passwordSecond = b.passwordChangeSecone.getText().toString();
                }
                if (password == null || passwordSecond == null || !password.equals(passwordSecond)) {
                    toast("两次输入不一致，请确定密码");
                    return;
                } else {
                    AVUser.resetPasswordBySmsCodeInBackground(code, password, new UpdatePasswordCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                toast("密码修改成功，请重新登录");
                                AVUser.logOut();
                                finish();
                            } else {
                                Log.d(TAG, "done: "+e.getCode());
                                if (e.getCode() == 127) {
                                    toast("系统错误, 请稍后再试");
                                }
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_change_password;
    }
}
