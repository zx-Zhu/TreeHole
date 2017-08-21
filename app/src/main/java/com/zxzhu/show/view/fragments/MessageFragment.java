package com.zxzhu.show.view.fragments;

import com.zxzhu.show.R;
import com.zxzhu.show.databinding.FragmentMessageBinding;
import com.zxzhu.show.presenter.IMessagePresenter;
import com.zxzhu.show.presenter.MessagePresenter;
import com.zxzhu.show.units.base.BaseFragment;
import com.zxzhu.show.view.fragments.inference.IMessageFragment;

public class MessageFragment extends BaseFragment implements IMessageFragment{
    private FragmentMessageBinding binding;
    private IMessagePresenter presenter;
    @Override
    protected void initData() {
        binding = getDataBinding();
        presenter = new MessagePresenter(this);
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_message;
    }

    @Override
    public void setIM() {

    }

    @Override
    public void setList() {

    }
}
