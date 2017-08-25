package com.zxzhu.show.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxzhu.show.R;
import com.zxzhu.show.units.UpdateUtil;
import com.zxzhu.show.units.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    protected void initData() {
        TextView title = $(R.id.header_title);
        title.setText("关于");
        ImageView back = $(R.id.back_header);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about;
    }

    public void setUpdate(View view) {
        UpdateUtil.checkUpdate(this,true);
    }
}
