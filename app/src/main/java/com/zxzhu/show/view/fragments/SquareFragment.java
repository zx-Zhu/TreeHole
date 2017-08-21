package com.zxzhu.show.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.zxzhu.show.R;
import com.zxzhu.show.adapters.MyRecyclerAdapter;
import com.zxzhu.show.databinding.FragmentSquareBinding;
import com.zxzhu.show.databinding.ItemSquareBinding;
import com.zxzhu.show.model.GetDataModel;
import com.zxzhu.show.presenter.ISquarePresenter;
import com.zxzhu.show.presenter.SquarePresenter;
import com.zxzhu.show.units.AudioTrackManager;
import com.zxzhu.show.units.base.BaseFragment;
import com.zxzhu.show.view.ChatActivity;
import com.zxzhu.show.view.fragments.inference.ISquareFragment;

import java.util.ArrayList;
import java.util.List;

public class SquareFragment extends BaseFragment implements ISquareFragment {
    private FragmentSquareBinding binding;
    private ISquarePresenter presenter;

    @Override
    protected void initData() {
        binding = getDataBinding();
        presenter = new SquarePresenter(this);
        getSquareData();
        setRefresh();
        binding.squareRefresh.setRefreshing(true);

    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_square;
    }

    @Override
    public void setList(final List<AVObject> list) {
        binding.listSquare.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        binding.listSquare.setAdapter(new MyRecyclerAdapter(getActivity(), R.layout.item_square,
                list, new MyRecyclerAdapter.BindView<ItemSquareBinding>() {
            @Override
            public void onBindViewHolder(final ItemSquareBinding b, final int position) {
                final int i = list.size() - position - 1;
                b.itemLayout.setTag(list.get(position).getObjectId());
                AVFile avFile = (AVFile) list.get(i).get("picMini");
                Glide.with(getActivity()).load(avFile.getUrl()).into(b.imgItemSquare);
                b.description.setText(list.get(i).get("description").toString());
                final String username = (String) list.get(i).get("username");
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
                        if (b.itemLayout.getTag().equals(tag)) {
                            b.nicknameItem.setText(users.get(0).get("nickname").toString());
                            Glide.with(getActivity()).load(head.getUrl()).placeholder(R.drawable.defult_head).into(b.imgHead);
                            b.imgHead.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    intent.putExtra("username",username);
                                    intent.putExtra("nickname",users.get(0).get("nickname").toString());
                                    startActivity(intent);
                                }
                            });
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
                b.commentNum.setText(String.valueOf(list.get(position).get("comment")));
                boolean isFavor = false;
                for (String favor : favors) {
                    if (favor.equals(username)) {
                        isFavor = true;
                        break;
                    }
                }
                if (isFavor) b.imgFavor.setColorFilter(Color.RED);
                b.imgFavor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isFavored = false;
                        for (String favor : favors) {
                            if (favor.equals(username)) {
                                isFavored = true;
                                break;
                            }
                        }
                        AVObject obj = AVObject.createWithoutData("Square", list.get(i).getObjectId());
                        if (!isFavored) {
                            String favor = String.valueOf((favors.size() + 1));
                            b.imgFavor.setColorFilter(Color.RED);
                            favors.add(username);
                            obj.put("favor", favors);
                            b.favorNum.setText(favor);
                        } else {
                            String favor = String.valueOf((favors.size() - 1));
                            b.imgFavor.setColorFilter(Color.LTGRAY);
                            b.favorNum.setText(favor);
                            for (int n = 0; n < num_favor; n++) {
                                if (favors.get(n).equals(username)) {
                                    favors.remove(n);
                                    break;
                                }
                            }
                            obj.put("favor", favors);
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
                    b.btnAudio.setRecordTime(time);
                    b.btnAudio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (audio != null) {
                                Log.d("[[[", "onClick: " + audio.getUrl());
                                try {
                                    AudioTrackManager.getInstance().loadAudio(audio.getUrl(), audio.getName().toString(), new AudioTrackManager.LoadAudioListener() {
                                        @Override
                                        public void finish(String path) {
                                            Log.d("555666", "finish: ");
                                            try {
                                                Thread.sleep(500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            AudioTrackManager.getInstance().startPlay(path);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }


            }

            @Override
            public void onRecycled(ItemSquareBinding b) {
                b.imgHead.setImageResource(R.drawable.defult_head);
                b.imgFavor.setColorFilter(Color.LTGRAY);
                b.btnAudio.setColorFilter(Color.DKGRAY);
                b.btnAudio.setRecordTime("");
            }
        }));
    }


    @Override
    public void getSquareData() {
        presenter.getSquareData();
    }

    void setRefresh() {
        binding.squareRefresh.setColorSchemeResources(R.color.md_deep_orange_100, R.color.md_deep_orange_200,
                R.color.md_deep_orange_300, R.color.md_deep_orange_400,
                R.color.md_deep_orange_500, R.color.md_deep_orange_600,
                R.color.md_deep_orange_700, R.color.md_orange_800);
        binding.squareRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("===", "onRefresh: 9999999999");
                getSquareData();
            }
        });
    }

    @Override
    public void hideRefresh() {
        if (binding.squareRefresh.isRefreshing()) {
            binding.squareRefresh.setRefreshing(false);
        }
    }
}
