package com.zxzhu.show.view.fragments;

import android.view.View;

import com.avos.avoscloud.AVUser;
import com.zxzhu.show.R;
import com.zxzhu.show.databinding.FragmentMineBinding;
import com.zxzhu.show.units.base.BaseFragment;

public class MineFragment extends BaseFragment {
    private FragmentMineBinding binding;
    @Override
    protected void initData() {
        binding = getDataBinding();
        binding.quiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVUser.logOut();
            }
        });
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_mine;
    }

}
