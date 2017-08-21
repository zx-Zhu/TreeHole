package com.zxzhu.show.view.fragments;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.zxzhu.show.R;
import com.zxzhu.show.databinding.FragmentMainBinding;
import com.zxzhu.show.model.MessageModel;
import com.zxzhu.show.presenter.IMainPresenter;
import com.zxzhu.show.presenter.MainPresenter;
import com.zxzhu.show.units.base.BaseFragment;
import com.zxzhu.show.view.MainActivity;
import com.zxzhu.show.view.fragments.inference.IMainFragment;

/**
 * Created by zxzhu on 2017/8/17.
 */

public class MainFragment extends BaseFragment implements IMainFragment{
    private FragmentMainBinding binding;
    private Resources resource;
    private ColorStateList orange, textLight;
    private IMainPresenter presenter;
    @Override
    public void setNavigationBar() {
        binding.squareBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ppp", "onClick: SQUARE");
                binding.headerTitle.setText("广场");
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.layout_main,new SquareFragment());
                transaction.commit();
                binding.iconSquare.setImageResource(R.drawable.square_focus);
                binding.iconMessage.setImageResource(R.drawable.message);
                binding.iconMine.setImageResource(R.drawable.mine);
                binding.txSquare.setTextColor(orange);
                binding.txMessage.setTextColor(textLight);
                binding.txMine.setTextColor(textLight);
            }
        });
        binding.messageBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ppp", "onClick: MESSAGE");
                binding.headerTitle.setText("消息");
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.layout_main,new MessageFragment());
                transaction.commit();
                binding.iconSquare.setImageResource(R.drawable.square);
                binding.iconMessage.setImageResource(R.drawable.message_focus);
                binding.iconMine.setImageResource(R.drawable.mine);
                binding.txSquare.setTextColor(textLight);
                binding.txMessage.setTextColor(orange);
                binding.txMine.setTextColor(textLight);
            }
        });
        binding.mineBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ppp", "onClick: MINE");
                binding.headerTitle.setText("个人中心");
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.layout_main,new MineFragment());
                transaction.commit();
                binding.iconSquare.setImageResource(R.drawable.square);
                binding.iconMessage.setImageResource(R.drawable.message);
                binding.iconMine.setImageResource(R.drawable.mine_focus);
                binding.txSquare.setTextColor(textLight);
                binding.txMessage.setTextColor(textLight);
                binding.txMine.setTextColor(orange);
            }
        });
    }

    @Override
    public void setFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.layout_main,new SquareFragment());
        transaction.commit();
    }

    @Override
    public void setHeaderBar() {
        binding.backHeader.setVisibility(View.VISIBLE);
        binding.headerTitle.setText("广场");
        binding.backHeader.setImageResource(R.drawable.camera);
        binding.backHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity)getActivity();
                activity.openCamera();
            }
        });
    }

    @Override
    public void receiveMessage() {
        presenter.getMessage(new MessageModel.MessageListener() {
            @Override
            public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
                toast("收到一条私信");
            }
        });
    }

    @Override
    protected void initData() {
        binding = getDataBinding();
        presenter = new MainPresenter();
        resource = (Resources) getActivity().getBaseContext().getResources();
        orange = (ColorStateList) resource.getColorStateList(R.color.orange);
        textLight = (ColorStateList) resource.getColorStateList(R.color.textLight);
        setNavigationBar();
        setFragments();
        setHeaderBar();
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_main;
    }

}
