package com.zxzhu.show.view;

import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.zxzhu.show.R;
import com.zxzhu.show.units.base.BaseActivity;

/**
 * Created by zxzhu on 2017/8/23.
 */

public class PhotoActivity extends BaseActivity {
    private PhotoView mPhotoView;
    @Override
    protected void initData() {
        mPhotoView = $(R.id.photo);
        setPhoto();
    }

    private void setPhoto() {
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        String url = intent.getStringExtra("picUrl");
        Glide.with(this).load(url)
                .into(mPhotoView);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_photo;
    }
}
