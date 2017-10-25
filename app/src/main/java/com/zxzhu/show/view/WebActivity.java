package com.zxzhu.show.view;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxzhu.show.R;
import com.zxzhu.show.units.base.BaseActivity;

public class WebActivity extends BaseActivity {
    @Override
    protected void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        WebView webView = $(R.id.webView);
        TextView title_text = $(R.id.header_title);
        ImageView back = $(R.id.back_header);
        title_text.setText(title);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        webView.loadUrl(url);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_web;
    }
}
