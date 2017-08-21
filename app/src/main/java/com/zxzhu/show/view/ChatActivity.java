package com.zxzhu.show.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.zxzhu.show.R;
import com.zxzhu.show.databinding.ActivityChatBinding;
import com.zxzhu.show.model.MessageModel;
import com.zxzhu.show.presenter.ChatPresenter;
import com.zxzhu.show.presenter.IChatPresenter;
import com.zxzhu.show.units.base.BaseActivity;
import com.zxzhu.show.view.Inference.IChatActivity;

public class ChatActivity extends BaseActivity implements IChatActivity{
    private ActivityChatBinding binding;
    private String user;
    private IChatPresenter presenter;
    @Override
    protected void initData() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat);
        presenter = new ChatPresenter(this);
        binding.sendBtnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    public void setBar() {
        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        String nicknaem = intent.getStringExtra("nickname");
        TextView title = $(R.id.header_title);
        title.setText(nicknaem);
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
    public void sendMessage() {
        String tx = binding.editChat.getText().toString();
        if (!tx.equals("")) {
            presenter.sendMessage(user,tx);
        } else {
            toast("输入为空");
        }
    }

    @Override
    public void getMessage() {
        presenter.getMessage(new MessageModel.MessageListener() {
            @Override
            public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
                toast(message.getContent());
            }
        });
    }
}
