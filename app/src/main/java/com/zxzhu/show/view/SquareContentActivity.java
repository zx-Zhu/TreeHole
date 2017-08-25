package com.zxzhu.show.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetFileCallback;
import com.bumptech.glide.Glide;
import com.zxzhu.show.R;
import com.zxzhu.show.adapters.MyRecyclerAdapter;
import com.zxzhu.show.databinding.ActivitySquareContentBinding;
import com.zxzhu.show.databinding.ItemCommentBinding;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.presenter.ISquareContentPresenter;
import com.zxzhu.show.presenter.SquareContentPresenter;
import com.zxzhu.show.units.AudioTrackManager;
import com.zxzhu.show.units.MyLayoutManager;
import com.zxzhu.show.units.base.BaseActivity;
import com.zxzhu.show.view.Inference.ISquareContentActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SquareContentActivity extends BaseActivity implements ISquareContentActivity, SwipeRefreshLayout.OnRefreshListener {
    private ActivitySquareContentBinding binding;
    private String id, iconUrl, nickname, myName, description, username, userId;
    private AVObject object;
    private ISquareContentPresenter presenter;
    public static final String TAG = "SquareContentActivityTest";

    @Override
    protected void initData() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_square_content);
        id = getIntent().getStringExtra("id");
        iconUrl = getIntent().getStringExtra("iconUrl");
        nickname = getIntent().getStringExtra("nickname");
        username = getIntent().getStringExtra("userId");
        description = getIntent().getStringExtra("description");
        myName = AVUser.getCurrentUser().getUsername();
        presenter = new SquareContentPresenter(this);
        setRefresh();
        presenter.getObjectById(id, new GetDataModel.GetObjectListener() {
            @Override
            public void done(AVObject obj) {
                object = obj;
                setBar();
                setContent();
                presenter.getComments(object);
            }
        });

    }

    @Override
    protected void onResume() {
        presenter.getObjectById(id, new GetDataModel.GetObjectListener() {
            @Override
            public void done(AVObject obj) {
                object = obj;
                setBar();
                setContent();
                presenter.getComments(object);
            }
        });
//        presenter.getComments(object);
        binding.refreshComment.setRefreshing(true);
        super.onResume();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_square_content;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void setBar() {
        ImageView back = (ImageView) findViewById(R.id.back_header);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView title = (TextView) findViewById(R.id.header_title);
        Date createdAt = object.getCreatedAt();
        DateFormat dayFormat = new SimpleDateFormat("MM月dd日");
        DateFormat timeFormat = new SimpleDateFormat("今天 HH:mm");
        Log.d(TAG, dayFormat.format(createdAt));
        Date now = new Date(System.currentTimeMillis());
        String time = null;
        if (now.getDay() == createdAt.getDay()) {
            time = timeFormat.format(createdAt);
        } else {
            time = dayFormat.format(createdAt);
        }
        title.setText(time);
    }

    @Override
    public void setList(List<String> list, final HashMap<String, Object> audios) {
        if (list == null) {
            list = new ArrayList<>();
        }
        final List<String> commentList = list;
        Log.d(TAG, "setList: " + list.size());
        binding.commentNumContent.setText(String.valueOf(list.size()));
        MyLayoutManager manager = new MyLayoutManager(this);
        manager.setScrollEnabled(false);
        binding.listComment.setLayoutManager(manager);
        binding.listComment.setAdapter(new MyRecyclerAdapter<ItemCommentBinding>(this,
                R.layout.item_comment, commentList.size(), new MyRecyclerAdapter.BindView<ItemCommentBinding>() {
            @Override
            public void onBindViewHolder(final ItemCommentBinding b, int position) {
                position = commentList.size() - position - 1;
                String str = commentList.get(position);
                String user = str.substring(str.indexOf("[") + 1, str.lastIndexOf("]"));
                String time = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")"));
                final String tx = str.substring(str.indexOf("{") + 1, str.lastIndexOf("}"));
                String audioId = "";
                final AVFile[] audio = {null};
                if (audios != null && audios.get("comment_" + user + time) != null) {
//                        JSONObject json = new JSONObject(audios.get("comment_" + user + time).toString());
                    audioId = audios.get("comment_" + user + time).toString();
                    Log.d(TAG, "onBindViewHolder: " + audioId);
                    AVFile.withObjectIdInBackground(audioId, new GetFileCallback<AVFile>() {
                        @Override
                        public void done(AVFile avFile, AVException e) {
                            if (e == null) {
                                audio[0] = avFile;
                                b.commentAudio.setColorFilter(Color.parseColor("#4CAF50"));
                                final String[] audioPath = {""};
                                try {
                                    AudioTrackManager.getInstance().loadAudio(audio[0].getUrl(), audio[0].getName().toString(), new AudioTrackManager.LoadAudioListener() {
                                        @Override
                                        public void finish(String path) {
                                            audioPath[0] = path;
                                        }
                                    });
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                                b.commentAudio.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (audio[0] != null && !AudioTrackManager.getInstance().isPlaying()) {
                                            Log.d("[[[", "onClick: " + audio[0].getUrl());
                                            try {
                                                Thread.sleep(500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            AudioTrackManager.getInstance().startPlay(audioPath[0]);
                                        } else if (AudioTrackManager.getInstance().isPlaying()) {
                                            toast("停止播放");
                                            AudioTrackManager.getInstance().stopPlay();
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "done: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    b.commentAudio.setVisibility(View.GONE);
                    b.commentTime.setText("No Audio");
                }

                b.commentTx.setText(tx);
                presenter.getUserData(user, new GetDataModel.GetDataListener<AVUser>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(AVException e) {

                    }

                    @Override
                    public void onFinish(List<AVUser> list) {
                        AVFile head = (AVFile) list.get(0).get("head");
                        try {
                            Glide.with(SquareContentActivity.this).load(head.getUrl().toString()).crossFade().into(b.iconCommentItem);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onRecycled(ItemCommentBinding b) {
                b.iconCommentItem.setImageResource(R.drawable.defult_head);
            }
        }));
    }

    @Override
    public void setContent() {
        Glide.with(this).load(iconUrl).crossFade().into(binding.headIconContent);
        binding.nicknameContent.setText(nickname);
        AVFile picMini = object.getAVFile("picMini");
        final AVFile pic = object.getAVFile("content");
        Glide.with(this).load(picMini.getUrl()).crossFade().into(binding.imgContent);
        binding.imgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SquareContentActivity.this, PhotoActivity.class);
                intent.putExtra("picUrl", pic.getUrl());
                startActivity(intent);
            }
        });
        binding.descriptionContent.setText(description);
        binding.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SquareContentActivity.this, CommentActivity.class);
                intent.putExtra("objectId", id);
                startActivity(intent);
            }
        });

        setFavor();
        setAudio();
        setFollow();
    }

    private void setFollow() {
        if (username.equals(AVUser.getCurrentUser().getUsername())) {
            binding.focusImgContent.setVisibility(View.GONE);
            binding.focusTxContent.setText("自己");
            userId = AVUser.getCurrentUser().getObjectId();
            binding.headIconContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SquareContentActivity.this, UserActivity.class);
                    intent.putExtra("id", userId);
                    startActivity(intent);
                }
            });
            return;
        }
        presenter.getFollowees(myName, new GetDataModel.GetDataListener<AVUser>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(AVException e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onFinish(final List<AVUser> list) {
                presenter.getUser(username, new GetDataModel.GetDataListener<AVUser>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(AVException e) {

                    }

                    @Override
                    public void onFinish(List<AVUser> targetUser) {
                        userId = targetUser.get(0).getObjectId();
                        binding.headIconContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SquareContentActivity.this, UserActivity.class);
                                intent.putExtra("id", userId);
                                startActivity(intent);
                            }
                        });
                        final boolean[] isMyFollowee = {false};
                        for (AVUser user : list) {
                            if (user.getObjectId() != null && user.getObjectId().equals(userId)) {
                                binding.focusImgContent.setImageResource(R.drawable.followed);
                                binding.focusImgContent.setColorFilter(Color.RED);
                                binding.focusTxContent.setText("取消关注");
                                isMyFollowee[0] = true;
                                break;
                            }
                        }
                        if (!isMyFollowee[0]) {
                            binding.focusImgContent.setImageResource(R.drawable.follow);
                            binding.focusImgContent.setColorFilter(Color.GRAY);
                            binding.focusTxContent.setText("关注");
                        }
                        binding.focusImgContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isMyFollowee[0]) {
                                    binding.focusImgContent.setImageResource(R.drawable.follow);
                                    binding.focusImgContent.setColorFilter(Color.GRAY);
                                    binding.focusTxContent.setText("关注");
                                    presenter.unFollowById(userId);
                                    isMyFollowee[0] = false;
                                } else {
                                    binding.focusImgContent.setImageResource(R.drawable.followed);
                                    binding.focusImgContent.setColorFilter(Color.RED);
                                    binding.focusTxContent.setText("取消关注");
                                    presenter.followById(userId);
                                    isMyFollowee[0] = true;
                                }
                            }
                        });
                        binding.focusImgContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isMyFollowee[0]) {
                                    binding.focusImgContent.setImageResource(R.drawable.follow);
                                    binding.focusImgContent.setColorFilter(Color.GRAY);
                                    binding.focusTxContent.setText("关注");
                                    presenter.unFollowById(userId);
                                    isMyFollowee[0] = false;
                                } else {
                                    binding.focusImgContent.setImageResource(R.drawable.followed);
                                    binding.focusImgContent.setColorFilter(Color.RED);
                                    binding.focusTxContent.setText("取消关注");
                                    presenter.followById(userId);
                                    isMyFollowee[0] = true;
                                }
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    public void hideRefresh() {
        if (binding.refreshComment.isRefreshing()) {
            binding.refreshComment.setRefreshing(false);
        }
    }

    private void setAudio() {
        final AVFile audio = object.getAVFile("voice");
        final String[] audioPath = {""};
        if ((int) (object.get("type")) == 1) {
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
            String time = (String) object.get("audioTime");
            binding.audioImgContent.setColorFilter(Color.GREEN);
            binding.audioTimeContent.setText(time);
            binding.audioImgContent.setOnClickListener(new View.OnClickListener() {
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
            binding.audioTimeContent.setText("No Audio");
        }
    }

    private void setFavor() {
        List<String> favors_tem = new ArrayList<>();
        if (object.get("favor") != null) {
            favors_tem = (List<String>) object.get("favor");
        }
        final int num_favor = favors_tem.size();
        final List<String> favors = favors_tem;
        binding.favorNumContent.setText(String.valueOf(num_favor));
        final boolean[] isFavor = {false};
        for (String favor : favors) {
            if (favor.equals(myName)) {
                isFavor[0] = true;
                break;
            }
        }
        if (isFavor[0]) binding.favorImgContent.setColorFilter(Color.RED);
        binding.favorImgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVObject obj = AVObject.createWithoutData("Square", object.getObjectId());
                if (!isFavor[0]) {
                    binding.favorImgContent.setColorFilter(Color.RED);
                    favors.add(myName);
                    obj.put("favor", favors);
                    String favor = String.valueOf((favors.size()));
                    binding.favorNumContent.setText(favor);
                    isFavor[0] = true;
                } else {
                    for (String str : favors) {
                        if (str.equals(myName)) {
                            favors.remove(str);
                            break;
                        }
                    }
                    binding.favorImgContent.setColorFilter(Color.LTGRAY);
                    String favor = String.valueOf((favors.size()));
                    binding.favorNumContent.setText(favor);
                    obj.put("favor", favors);
                    isFavor[0] = false;
                }
                obj.saveInBackground();
            }
        });
    }

    private void setRefresh() {
        binding.refreshComment.setColorSchemeResources(R.color.md_deep_orange_100, R.color.md_deep_orange_200,
                R.color.md_deep_orange_300, R.color.md_deep_orange_400,
                R.color.md_deep_orange_500, R.color.md_deep_orange_600,
                R.color.md_deep_orange_700, R.color.md_orange_800);
        binding.refreshComment.setOnRefreshListener(this);
    }

    @Override
    public void finish() {
        AudioTrackManager.getInstance().stopPlay();
        super.finish();
    }

    @Override
    public void onRefresh() {
        presenter.getComments(object);
    }
}
