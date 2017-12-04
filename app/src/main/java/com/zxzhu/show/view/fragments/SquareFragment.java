package com.zxzhu.show.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.yalantis.phoenix.PullToRefreshView;
import com.zxzhu.show.R;
import com.zxzhu.show.adapters.MyRecyclerAdapter;
import com.zxzhu.show.adapters.RollPicsAdapter;
import com.zxzhu.show.databinding.FragmentSquareBinding;
import com.zxzhu.show.databinding.ItemSquareBinding;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.presenter.ISquarePresenter;
import com.zxzhu.show.presenter.SquarePresenter;
import com.zxzhu.show.units.AudioTrackManager;
import com.zxzhu.show.units.MyLayoutManager;
import com.zxzhu.show.units.base.BaseFragment;
import com.zxzhu.show.view.SquareContentActivity;
import com.zxzhu.show.view.fragments.inference.ISquareFragment;

import java.util.ArrayList;
import java.util.List;

public class SquareFragment extends BaseFragment implements ISquareFragment {
    private FragmentSquareBinding binding;
    private ISquarePresenter presenter;
    private boolean isPlaying = false;

    @Override
    protected void initData() {
        binding = getDataBinding();
        presenter = new SquarePresenter(this);
        binding.listSquare.setLayoutManager(new LinearLayoutManager(getActivity()));
        getSquareData();
        setRefresh();
        presenter.getRollPics();
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_square;
    }

    @Override
    public void setList(final List<AVObject> list) {
        MyLayoutManager manager = new MyLayoutManager(getContext());
        manager.setScrollEnabled(false);
        binding.listSquare.setLayoutManager(manager);
        binding.listSquare.setAdapter(new MyRecyclerAdapter<ItemSquareBinding>(getActivity(), R.layout.item_square,
                list.size(), new MyRecyclerAdapter.BindView<ItemSquareBinding>() {
            @Override
            public void onBindViewHolder(final ItemSquareBinding b, final int position) throws Exception {
                final int i = list.size() - position - 1;
                b.itemLayout.setTag(list.get(position).getObjectId());
                final String objectId = list.get(i).getObjectId();
                AVFile avFile = (AVFile) list.get(i).get("picMini");
                Glide.with(getActivity()).load(avFile.getUrl()).into(b.imgItemSquare);
                String str = "还没有评论哦~";
                if (!list.get(i).getJSONArray("comment_tx").isNull(0)) {
                    str = list.get(i).getJSONArray("comment_tx").getString(0);
                    str = str.substring(str.indexOf("{") + 1, str.lastIndexOf("}"));
                }
                b.content.setText(str);
                b.description.setText(list.get(i).get("description").toString());
                final String username = (String) list.get(i).get("username");
                final String[] userData = {"", ""};
                presenter.getUserData(username, new GetDataModel.GetDataListener<AVUser>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onError(AVException e) {
                    }

                    @Override
                    public void onFinish(final List<AVUser> users) {
                        AVFile head = (AVFile) users.get(0).get("head");
                        Log.d("iii", "onFinish: " + head.getUrl());
                        String tag = list.get(position).getObjectId();
                        try {
                            Glide.with(getActivity()).load(head.getUrl()).into(b.imgHead);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (b.itemLayout.getTag().equals(tag)) {
                            b.nicknameItem.setText(users.get(0).get("nickname").toString());
                            userData[0] = users.get(0).get("nickname").toString();
                            userData[1] = head.getUrl();
                        }

                    }
                });
                /**
                 * 点赞
                 */
                List<String> favors_tem = new ArrayList<>();
                if (list.get(i).get("favor") != null) {
                    favors_tem = (List<String>) list.get(i).get("favor");
                }
                final int num_favor = favors_tem.size();
                final List<String> favors = favors_tem;
                b.favorNum.setText(String.valueOf(num_favor));
                List<String> comments = (List<String>) list.get(i).get("comment_tx");
                if (comments != null) {
                    b.commentNum.setText(String.valueOf(comments.size()));
                } else {
                    b.commentNum.setText("0");
                }
                final boolean[] isFavor = {false};
                for (String favor : favors) {
                    if (favor.equals(AVUser.getCurrentUser().getUsername())) {
                        isFavor[0] = true;
                        break;
                    }
                }
                if (isFavor[0]) b.imgFavor.setColorFilter(Color.RED);
                b.imgFavor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AVObject obj = AVObject.createWithoutData("Square", list.get(i).getObjectId());
                        if (!isFavor[0]) {
                            b.imgFavor.setColorFilter(Color.RED);
                            favors.add(AVUser.getCurrentUser().getUsername());
                            obj.put("favor", favors);
                            String favor = String.valueOf(favors.size());
                            b.favorNum.setText(favor);
                            isFavor[0] = true;
                        } else {
                            for (String str : favors) {
                                if (str.equals(AVUser.getCurrentUser().getUsername())) {
                                    favors.remove(str);
                                    break;
                                }
                            }
                            String favor = String.valueOf((favors.size()));
                            b.imgFavor.setColorFilter(Color.LTGRAY);
                            b.favorNum.setText(favor);
                            obj.put("favor", favors);
                            isFavor[0] = false;
                        }
                        obj.saveInBackground();
                    }
                });
                /**
                 * 音频
                 */
                final AVFile audio = list.get(i).getAVFile("voice");
                String time = (String) list.get(i).get("audioTime");

                if ((int) (list.get(i).get("type")) == 1) {
                    b.btnAudio.setColorFilter(Color.parseColor("#287c2e"));
                    final String[] audioPath = {""};
                    AudioTrackManager.getInstance().loadAudio(audio.getUrl(), audio.getName().toString(), new AudioTrackManager.LoadAudioListener() {
                        @Override
                        public void finish(String path) {
                            audioPath[0] = path;
                        }
                    });
                    b.btnAudio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (audio != null && !isPlaying) {
                                Log.d("[[[", "onClick: " + audio.getUrl());
                                try {

                                    AudioTrackManager.getInstance().startPlay(audioPath[0]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                isPlaying = true;
                            } else if (isPlaying) {
                                toast("停止播放");
                                AudioTrackManager.getInstance().stopPlay();
                                isPlaying = false;
                            }
                        }
                    });
                } else {
                    b.btnAudio.setColorFilter(Color.LTGRAY);
                }
                b.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), SquareContentActivity.class);
                        intent.putExtra("nickname", userData[0]);
                        intent.putExtra("iconUrl", userData[1]);
                        intent.putExtra("id", objectId);
                        intent.putExtra("userId", username);
                        intent.putExtra("description", list.get(i).get("description").toString());
                        AudioTrackManager.getInstance().stopPlay();
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onRecycled(ItemSquareBinding b) {
                b.imgHead.setImageResource(R.drawable.defult_head);
                b.imgFavor.setColorFilter(Color.LTGRAY);
                b.btnAudio.setColorFilter(Color.LTGRAY);
            }
        }));
    }


    @Override
    public void getSquareData() {
        presenter.getSquareData();
    }

    private void setRefresh() {
        binding.pullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getRollPics();
                presenter.getSquareData();
            }
        });
    }

    @Override
    public void hideRefresh() {
        binding.pullToRefresh.setRefreshing(false);
    }

    @Override
    public void setRollPics(final List<AVObject> list) {
        binding.rollImages.setAdapter(new RollPicsAdapter(list));
    }
}