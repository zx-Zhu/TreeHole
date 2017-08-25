package com.zxzhu.show.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.bumptech.glide.Glide;
import com.zxzhu.show.R;
import com.zxzhu.show.adapters.MyRecyclerAdapter;
import com.zxzhu.show.databinding.ActivityUserBinding;
import com.zxzhu.show.databinding.ItemFollowBinding;
import com.zxzhu.show.databinding.ItemRecentlyBinding;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.presenter.IUserActivityPresenter;
import com.zxzhu.show.presenter.UserPresenter;
import com.zxzhu.show.units.AudioTrackManager;
import com.zxzhu.show.units.MyLayoutManager;
import com.zxzhu.show.units.base.BaseActivity;
import com.zxzhu.show.view.Inference.IUserActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserActivity extends BaseActivity implements IUserActivity {
    private ActivityUserBinding binding;
    private IUserActivityPresenter presenter;
    private String leftUrl, rightUrl;
    private AVUser thisUser;
    private static final String TAG = "UserActivityTest";

    @Override
    protected void initData() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        presenter = new UserPresenter(this);
        String id = getIntent().getStringExtra("id");
        Log.d(TAG, "initData: "+id);
        presenter.getFollowees(id);
        presenter.getFollowers(id);
        if (id.equals(AVUser.getCurrentUser().getObjectId())) {
            thisUser = AVUser.getCurrentUser();
            setUser(AVUser.getCurrentUser());
            binding.sendMessageUser.setVisibility(View.GONE);
            presenter.getRecently(AVUser.getCurrentUser().getUsername());
            presenter.getConversation(AVUser.getCurrentUser().getUsername(), AVUser.getCurrentUser().get("nickname").toString());
        } else {
            presenter.getUserData(id, new GetDataModel.GetObjectListener() {
                @Override
                public void done(AVObject obj) {
                    Log.d(TAG, "done: ");
                    AVUser user = (AVUser) obj;
                    thisUser = user;
                    setUser(user);
                    leftUrl = user.getAVFile("head").getUrl();
                    rightUrl = AVUser.getCurrentUser().getAVFile("head").getUrl();
                    presenter.getRecently(user.getUsername());
                    presenter.getConversation(user.getUsername(), user.get("nickname").toString());
                }
            });
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user;
    }

    @Override
    public void setUser(AVUser user) {
        AVFile head = user.getAVFile("head");
        Glide.with(this).load(head.getUrl()).crossFade().into(binding.headUser);
        binding.nicknameUser.setText(user.get("nickname").toString());
        String readme = String.valueOf(user.get("readme"));
        if (readme != null && !readme.equals("null")) {
            binding.descriptionUser.setText(readme);
        } else {
            binding.descriptionUser.setText("该用户很神秘没有简介");
        }
    }

    @Override
    public void setFollowers(final List<AVUser> list) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.listFollowersUser.setLayoutManager(manager);
        binding.listFollowersUser.setAdapter(new MyRecyclerAdapter<ItemFollowBinding>(this, R.layout.item_follow,
                list.size(), new MyRecyclerAdapter.BindView<ItemFollowBinding>() {
            @Override
            public void onBindViewHolder(final ItemFollowBinding b, final int position) {
                final String id = list.get(position).getObjectId();
                presenter.getUserData(id, new GetDataModel.GetObjectListener() {
                    @Override
                    public void done(AVObject obj) {
                        AVFile head = obj.getAVFile("head");
                        Log.d(TAG, "onBindViewHolder: " + head + "    " + obj.toString());
                        Glide.with(UserActivity.this).load(head.getUrl()).crossFade().into(b.iconItemFollower);
                        b.nicknameItemFollower.setText(obj.get("nickname").toString());
                        b.iconItemFollower.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(UserActivity.this,UserActivity.class);
                                intent.putExtra("id",id);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }

            @Override
            public void onRecycled(ItemFollowBinding b) {
                b.nicknameItemFollower.setText("");
                b.iconItemFollower.setImageResource(R.drawable.defult_head);
            }
        }));
    }

    @Override
    public void setFollowees(final List<AVUser> list) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.listFolloweesUser.setLayoutManager(manager);
        binding.listFolloweesUser.setAdapter(new MyRecyclerAdapter<ItemFollowBinding>(this, R.layout.item_follow,
                list.size(), new MyRecyclerAdapter.BindView<ItemFollowBinding>() {
            @Override
            public void onBindViewHolder(final ItemFollowBinding b, int position) {
                final String id = list.get(position).getObjectId();
                presenter.getUserData(id, new GetDataModel.GetObjectListener() {
                    @Override
                    public void done(AVObject obj) {
                        AVFile head = obj.getAVFile("head");
                        Log.d(TAG, "onBindViewHolder: " + head + "    " + obj.toString());
                        Glide.with(UserActivity.this).load(head.getUrl()).crossFade().into(b.iconItemFollower);
                        b.nicknameItemFollower.setText(obj.get("nickname").toString());
                        b.iconItemFollower.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(UserActivity.this,UserActivity.class);
                                intent.putExtra("id",id);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }

            @Override
            public void onRecycled(ItemFollowBinding b) {
                b.nicknameItemFollower.setText("");
                b.iconItemFollower.setImageResource(R.drawable.defult_head);
            }
        }));
    }

    @Override
    public void setRecently(final List<AVObject> list) {
        MyLayoutManager manager = new MyLayoutManager(this);
        manager.setScrollEnabled(false);
        binding.listRecently.setLayoutManager(manager);
        binding.listRecently.setAdapter(new MyRecyclerAdapter<ItemRecentlyBinding>(this, R.layout.item_recently,
                list.size(), new MyRecyclerAdapter.BindView<ItemRecentlyBinding>() {
            @Override
            public void onBindViewHolder(final ItemRecentlyBinding b, int position) {
                final int i = list.size() - position - 1;
                Log.d("setRecentlyTest", "onBindViewHolder: " + list.get(i));
                final String user = AVUser.getCurrentUser().getUsername();
                AVFile img = list.get(i).getAVFile("picMini");
                Glide.with(UserActivity.this).load(img.getUrl()).crossFade().into(b.imgRecently);
                b.txRecently.setText(list.get(i).get("description").toString());
                //时间
                Date createdAt = (Date) list.get(i).get("createdAt");
                Date now = new Date(System.currentTimeMillis());
                String time = null;
                if (now.getDay() == createdAt.getDay()) {
                    time = createdAt.getHours() + ":" + createdAt.getMinutes();
                } else {
                    time = createdAt.getMonth() + "月" + createdAt.getDay() + "日 ";
                }
                b.timeRecently.setText(time);
                //点赞
                List<String> favors_tem = new ArrayList<>();
                if (list.get(i).get("favor") != null) {
                    favors_tem = (List<String>) list.get(i).get("favor");
                }
                final int num_favor = favors_tem.size();
                final List<String> favors = favors_tem;
                b.numFavorRecently.setText(String.valueOf(num_favor));
                final boolean[] isFavor = {false};
                for (String favor : favors) {
                    if (favor.equals(user)) {
                        isFavor[0] = true;
                        break;
                    }
                }
                if (isFavor[0]) b.imgFavorRecently.setColorFilter(Color.RED);
                b.imgFavorRecently.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AVObject obj = list.get(i);
                        if (!isFavor[0]) {
                            favors.add(user);
                            String favor = String.valueOf((favors.size()));
                            b.imgFavorRecently.setColorFilter(Color.RED);
                            b.numFavorRecently.setText(favor);
                            obj.put("favor", favors);
                            isFavor[0] = true;
                        } else {
                            for (int n = 0; n < num_favor; n++) {
                                if (favors.get(n).equals(user)) {
                                    favors.remove(n);
                                    break;
                                }
                            }
                            b.imgFavorRecently.setColorFilter(Color.LTGRAY);
                            String favor = String.valueOf((favors.size()));
                            b.numFavorRecently.setText(favor);
                            obj.put("favor", favors);
                            isFavor[0] = false;
                        }
                        obj.saveInBackground();
                    }
                });
                //评论
                List<String> comments = list.get(i).getList("comment_tx");
                if (comments != null) {
                    b.numCommentRecently.setText(String.valueOf(comments.size()));
                } else {
                    b.numCommentRecently.setText("0");
                }
                //音频
                final AVFile audio = list.get(i).getAVFile("voice");
                final String[] audioPath = {""};
                if ((int) (list.get(i).get("type")) == 1) {
                    try {
                        AudioTrackManager.getInstance().loadAudio(audio.getUrl(), audio.getName().toString(), new AudioTrackManager.LoadAudioListener() {
                            @Override
                            public void finish(String path) {
                                audioPath[0] = path;
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String audioTime = (String) list.get(i).get("audioTime");
                    b.audioRecently.setColorFilter(Color.parseColor("#287c2e"));
                    b.audioRecently.setRecordTime(audioTime);
                    b.audioRecently.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (audio != null && !AudioTrackManager.getInstance().isPlaying()) {
                                Log.d("[[[", "onClick: " + audio.getUrl());
                                AudioTrackManager.getInstance().startPlay(audioPath[0]);
                            } else if (AudioTrackManager.getInstance().isPlaying()) {
                                toast("停止播放");
                                AudioTrackManager.getInstance().stopPlay();
                            }
                        }
                    });
                } else {
                    b.audioRecently.setRecordTime("No Audio");
                }
                b.recentlyLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserActivity.this, SquareContentActivity.class);
                        intent.putExtra("nickname", thisUser.get("nickname").toString());
                        intent.putExtra("iconUrl", thisUser.getAVFile("head").getUrl());
                        intent.putExtra("id", list.get(i).getObjectId());
                        intent.putExtra("userId", thisUser.getUsername());
                        intent.putExtra("description", list.get(i).get("description").toString());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onRecycled(ItemRecentlyBinding b) {
                b.audioRecently.setRecordTime("");
                b.audioRecently.setColorFilter(Color.LTGRAY);
                b.imgFavorRecently.setColorFilter(Color.LTGRAY);
            }
        }));
    }

    @Override
    public void setMessage(final AVIMConversation conservation, final String username, final String nickname) {
        binding.sendMessageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("id", conservation.getConversationId());
                intent.putExtra("nickname", nickname);
                intent.putExtra("rightUrl", rightUrl);
                intent.putExtra("leftUrl", leftUrl);

                startActivity(intent);
            }
        });
    }


}
