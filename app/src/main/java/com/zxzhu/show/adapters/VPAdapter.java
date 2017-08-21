package com.zxzhu.show.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

/**
 * Created by zxzhu on 2017/8/17.
 */

public class VPAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;
    private LayoutInflater layoutInflater;
    private Context context;

    public VPAdapter(Context context, FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

}
