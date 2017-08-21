package com.zxzhu.show.units.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by zxzhu on 2017/8/2.
 */

public abstract class BaseFragment extends Fragment {

    private View mView;
    private LayoutInflater inflater;
    private ViewGroup container;
    private ViewDataBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getResourceId(),container,false);
        binding = DataBindingUtil.inflate(inflater, getResourceId(), container, false);
        this.inflater = inflater;
        this.container = container;
        initData();
        return binding.getRoot();
    }

    //做数据或其他初始化的方法
    protected abstract void initData();

    //获取当前fragment的view的方法
    protected abstract int getResourceId();

    public void toast(String str) {
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
    }



    public LayoutInflater getInflater(){
        return inflater;
    }

    public ViewGroup getViewGroup(){
        return container;
    }

    //获取对应ViewDataBinding
    public <T extends ViewDataBinding>T getDataBinding() {
        return (T)binding;
    }

    //View快捷绑定id的方法
    public <T extends View> T $(int id) {
        return (T) mView.findViewById(id);
    }
}
