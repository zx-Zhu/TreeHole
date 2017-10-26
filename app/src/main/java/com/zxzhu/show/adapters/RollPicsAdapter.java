package com.zxzhu.show.adapters;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.zxzhu.show.view.WebActivity;

import java.util.List;

/**
 * Created by zxzhu on 2017/10/25.
 */

public class RollPicsAdapter extends StaticPagerAdapter {
    private List<AVObject> list;

    public RollPicsAdapter(List<AVObject> list) {
        this.list = list;
    }

    @Override
    public View getView(final ViewGroup container, int position) {
        final AVObject obj = list.get(position);
        AVFile pic = (AVFile)obj.get("pic");
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Glide.with(container.getContext()).load(pic.getUrl()).into(view);

        if (obj.get("url") != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(container.getContext(), WebActivity.class);
                    intent.putExtra("title", obj.get("text").toString());
                    intent.putExtra("url", obj.get("url").toString());
                    container.getContext().startActivity(intent);
                }
            });
        }
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
