package com.zxzhu.show.view.fragments;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bumptech.glide.Glide;
import com.zxzhu.show.R;
import com.zxzhu.show.adapters.MyRecyclerAdapter;
import com.zxzhu.show.databinding.FragmentMessageBinding;
import com.zxzhu.show.databinding.ItemMessageBinding;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.model.MessageModel;
import com.zxzhu.show.presenter.IMessagePresenter;
import com.zxzhu.show.presenter.MessagePresenter;
import com.zxzhu.show.units.base.BaseFragment;
import com.zxzhu.show.view.ChatActivity;
import com.zxzhu.show.view.fragments.inference.IMessageFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageFragment extends BaseFragment implements IMessageFragment {
    private FragmentMessageBinding binding;
    private IMessagePresenter presenter;

    @Override
    public void onResume() {
        setIM();
        binding.refreshMessage.setRefreshing(true);
        presenter.getConversation();
        super.onResume();
    }

    @Override
    protected void initData() {
        binding = getDataBinding();
        presenter = new MessagePresenter(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listMessage.setLayoutManager(manager);
//        presenter.getConversation();
        setRefresh();
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_message;
    }

    @Override
    public void setIM() {
        presenter.getMessage(new MessageModel.MessageListener() {
            @Override
            public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
                presenter.getConversation();
            }
        });
    }

    @Override
    public void setList(final List<AVIMConversation> list) {
        Log.d("zxccvb", "setList: "+list.size());
        for (AVIMConversation con : list) {
            if (con.getLastMessage() == null) {
                Log.d("zxccvb", "setList: "+con);
                list.remove(con);
            }
        }
        if (list.size() == 0) {
            toast("还没有对话信息");
        }
        if (list != null) {
            binding.listMessage.setAdapter(new MyRecyclerAdapter<ItemMessageBinding>(getActivity(), R.layout.item_message
                    , list.size(), new MyRecyclerAdapter.BindView<ItemMessageBinding>() {
                @Override
                public void onBindViewHolder(final ItemMessageBinding b, final int position) {
                    b.nicknameMessageItem.setTag(list.get(position).getConversationId());
                    final int i = list.size() - position - 1;
                    String type = "";
                    JSONObject json = null;
//                    Log.d("zxccvb", list.size()+"onBindViewHolder: " + list.get(i).getLastMessage().getContent());
                    try {
                        if (list.get(i).getLastMessage() != null) {
                            json = new JSONObject(list.get(i).getLastMessage().getContent());
                            Log.d("zxccvb", "onBindViewHolder: "+json);
//                        type = list.get(i).getLastMessage().getContent().substring(list.get(i).getLastMessage().getContent().length()-3);
                        }

                        if (json.getString("_lctype").equals("-2")) {
                            b.contentMessageItem.setText("[图片]");
                        } else if (json.getString("_lctype").equals("-3")) {
                            b.contentMessageItem.setText("[语音消息]");
                        } else {
                            b.contentMessageItem.setText(((AVIMTextMessage) list.get(i).getLastMessage()).getText());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Date updateAt = list.get(i).getUpdatedAt();
                    Date now = new Date(System.currentTimeMillis());
                    DateFormat dayFormat = new SimpleDateFormat("MM月dd日");
                    DateFormat timeFormat = new SimpleDateFormat("今天 HH:mm");
                    String time = null;
                    if (now.getDay() == updateAt.getDay()) {
                        time = timeFormat.format(updateAt);
                    } else {
                        time = dayFormat.format(updateAt);
                    }
                    b.timeMessageItem.setText(time);

                    String username = list.get(i).getMembers().get(0);
                    Log.d("iioo", list.get(i).getMembers().size() + "  " + list.get(i).getMembers().get(0) + "  onBindViewHolder:  " + list.get(i).getCreator());

                    if (username.equals(AVUser.getCurrentUser().getUsername())) {
                        username = list.get(i).getMembers().get(1);
                    }
                    final String finalUsername = username;
                    presenter.getUserData(username, new GetDataModel.GetDataListener<AVUser>() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onError(AVException e) {
                        }

                        @Override
                        public void onFinish(final List<AVUser> users) {
                            final AVFile head = (AVFile) users.get(0).get("head");
                            Log.d("iii", "onFinish: " + head.getUrl());
                            String tag = list.get(position).getConversationId();
                            try {
                                Glide.with(getActivity()).load(head.getUrl()).into(b.imgMessageItem);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (b.nicknameMessageItem.getTag().equals(tag)) {
                                b.nicknameMessageItem.setText(users.get(0).get("nickname").toString());
                            }
                            b.itemLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra("username", finalUsername);
                                    intent.putExtra("id", list.get(i).getConversationId());
                                    intent.putExtra("leftUrl", head.getUrl());
                                    intent.putExtra("rightUrl", AVUser.getCurrentUser().getAVFile("head").getUrl());
                                    intent.putExtra("nickname", users.get(0).get("nickname").toString());
                                    startActivity(intent);
                                }
                            });

                        }
                    });
                }

                @Override
                public void onRecycled(ItemMessageBinding b) {
                    b.imgMessageItem.setImageResource(R.drawable.defult_head);
                }
            }));
        } else toast("还没有对话消息");
    }

    public void setRefresh() {
        binding.refreshMessage.setColorSchemeResources(R.color.md_deep_orange_100, R.color.md_deep_orange_200,
                R.color.md_deep_orange_300, R.color.md_deep_orange_400,
                R.color.md_deep_orange_500, R.color.md_deep_orange_600,
                R.color.md_deep_orange_700, R.color.md_orange_800);
        binding.refreshMessage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("===", "onRefresh: 9999999999");
                presenter.getConversation();
            }
        });
    }

    @Override
    public void hideRefresh() {
        if (binding.refreshMessage.isRefreshing()) {
            binding.refreshMessage.setRefreshing(false);
        }
    }
}
