package com.zxzhu.show.view;

import android.content.Intent;
import android.util.Log;

import com.zxzhu.show.R;
import com.zxzhu.show.presenter.IwelcomePresenter;
import com.zxzhu.show.presenter.WelcomePresenter;
import com.zxzhu.show.units.base.BaseActivity;
import com.zxzhu.show.view.Inference.IWelcomeActivity;

public class WelcomeActivity extends BaseActivity implements IWelcomeActivity {
    private IwelcomePresenter welcomePresenter;

    @Override
    protected void initData() {

        welcomePresenter = new WelcomePresenter(this);
        autoLogin();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void autoLogin() {
        welcomePresenter.autoLogin(new WelcomePresenter.AutoLoginListener() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail() {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void jumpToLogin() {
        Log.d("ooo", "jumpToLogin: ");

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void jumpToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

