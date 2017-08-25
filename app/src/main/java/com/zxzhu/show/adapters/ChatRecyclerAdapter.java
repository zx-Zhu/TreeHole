package com.zxzhu.show.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bumptech.glide.Glide;
import com.zxzhu.show.R;
import com.zxzhu.show.units.AudioTrackManager;
import com.zxzhu.show.units.ScreenUnit;
import com.zxzhu.show.view.PhotoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zxzhu on 2017/8/22.
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.MyViewHolder> {
    private static final int TEXT_LEFT = 1, TEXT_RIGHT = 0, PIC_LEFT = 2, PIC_RIGHT = 3, AUDIO_LEFT = 4, AUDIO_RIGHT = 5;
    private Context context;
    private List list;
    private String targetUser;
    private int picWide;
    private String leftUrl, rightUrl;

    public ChatRecyclerAdapter(Context context, List list, String targetUser, String leftUrl, String rightUrl) {
        this.context = context;
        this.leftUrl = leftUrl;
        this.rightUrl = rightUrl;
        if (!list.toString().equals("[]")) {
            this.list = list;
        } else {
            this.list = new ArrayList();
        }
        this.targetUser = targetUser;
        picWide = ScreenUnit.bulid(context).getPxWide() / 4;
    }


    @Override
    public int getItemViewType(int position) {
        AVIMMessage message = (AVIMMessage) this.list.get(position);
        JSONObject object = null;
        String type = "";
        try {
            object = new JSONObject(message.getContent());
            Log.d("111222", "getItemViewType: " + object.toString());
            type = object.getString("_lctype").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (type.equals("-1")) {
            if (message.getFrom().equals(targetUser)) {
                return TEXT_LEFT;
            } else return TEXT_RIGHT;
        } else if (type.equals("-2")) {
            if (message.getFrom().equals(targetUser)) {
                return PIC_LEFT;
            } else return PIC_RIGHT;
        } else if (type.equals("-3")) {
            if (message.getFrom().equals(targetUser)) {
                return AUDIO_LEFT;
            } else return AUDIO_RIGHT;
        }
        return 0;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TEXT_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_left_chat, parent, false);
            return new MyViewHolder(view, TEXT_LEFT);
        } else if (viewType == TEXT_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_right_chat, parent, false);
            return new MyViewHolder(view, TEXT_RIGHT);
        } else if (viewType == PIC_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.iten_pic_left, parent, false);
            return new MyViewHolder(view, PIC_LEFT);
        } else if (viewType == PIC_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_pic_right, parent, false);
            return new MyViewHolder(view, PIC_RIGHT);
        } else if (viewType == AUDIO_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_audio_left, parent, false);
            return new MyViewHolder(view, AUDIO_LEFT);
        } else if (viewType == AUDIO_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_audio_right, parent, false);
            return new MyViewHolder(view, AUDIO_RIGHT);
        } else return null;
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        switch (holder.viewType) {
            case PIC_LEFT:
                holder.pic_left.setImageResource(R.drawable.defult);
                holder.pic_icon_left.setImageResource(R.drawable.defult_head);
                break;
            case PIC_RIGHT:
                holder.pic_right.setImageResource(R.drawable.defult);
                holder.pic_icon_right.setImageResource(R.drawable.defult_head);
                break;
        }
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        switch (holder.viewType) {
            case TEXT_LEFT:
                AVIMTextMessage message = (AVIMTextMessage) list.get(position);
                holder.text_left.setText(message.getText());
                Glide.with(context).load(leftUrl).into(holder.img_left);

                break;
            case TEXT_RIGHT:
                AVIMTextMessage message_right = (AVIMTextMessage) list.get(position);
                holder.text_right.setText(message_right.getText());
                Glide.with(context).load(rightUrl).into(holder.img_right);
                break;
            case PIC_LEFT:
                final AVIMImageMessage pic_message_left = (AVIMImageMessage) list.get(position);
                Glide.with(context).load(pic_message_left.getFileUrl())
                        .override(picWide, picWide)
                        .crossFade()
                        .into(holder.pic_left);
                holder.pic_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PhotoActivity.class);
                        intent.putExtra("picUrl", pic_message_left.getFileUrl());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, holder.pic_left, "share").toBundle());
                        } else {
                            context.startActivity(intent);
                        }
                    }
                });
                Glide.with(context).load(leftUrl).crossFade().into(holder.pic_icon_left);
                break;
            case PIC_RIGHT:
                final AVIMImageMessage pic_message_right = (AVIMImageMessage) list.get(position);
                Log.d("1112222", "onBindViewHolder: " + pic_message_right.getFileUrl());
                String url = pic_message_right.getFileUrl();

                if (url == null) {
                    url = pic_message_right.getLocalFilePath();
                }
                Glide.with(context).load(url)
                        .override(picWide, picWide)
                        .crossFade()
                        .into(holder.pic_right);
                holder.pic_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PhotoActivity.class);
                        intent.putExtra("picUrl", pic_message_right.getFileUrl());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, holder.pic_right, "share").toBundle());
                        } else {
                            context.startActivity(intent);
                        }

                    }
                });
                Glide.with(context).load(rightUrl)
                        .crossFade()
                        .into(holder.pic_icon_right);
                break;
            case AUDIO_LEFT:
                final AVIMAudioMessage audio_left = (AVIMAudioMessage) list.get(position);
                String audio_url = audio_left.getAVFile().getUrl();
                String time = audio_left.getText();
                Glide.with(context).load(leftUrl).crossFade().into(holder.icon_audio_left);
                holder.time_left.setText(time);
                try {
                    AudioTrackManager.getInstance().loadAudio(audio_url,
                            "audio_message_" + System.currentTimeMillis(), new AudioTrackManager.LoadAudioListener() {
                                @Override
                                public void finish(final String path) {
                                    holder.layout_left.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (!AudioTrackManager.getInstance().isPlaying()) {
                                                AudioTrackManager.getInstance().startPlay(path);
                                            } else {
                                                AudioTrackManager.getInstance().stopPlay();
                                            }
                                        }
                                    });
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case AUDIO_RIGHT:
                final AVIMAudioMessage audio_right = (AVIMAudioMessage) list.get(position);
                Map<String, Object> metaData = audio_right.getFileMetaData();
                final String[] audio_url_right = {audio_right.getFileUrl()};
                String time_right = audio_right.getText();
                Glide.with(context).load(rightUrl).crossFade().into(holder.icon_audio_right);
                holder.time_right.setText(time_right);
                if (audio_url_right[0] == null) {
                    audio_url_right[0] = audio_right.getLocalFilePath();
                    holder.layout_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!AudioTrackManager.getInstance().isPlaying()) {
                                Log.d("zhuzhan", "onBindViewHolder: "+audio_url_right[0]);
                                AudioTrackManager.getInstance().startPlay(audio_url_right[0]);
                            } else {
                                AudioTrackManager.getInstance().stopPlay();
                            }

                        }
                    });
                    return;
                }
                Log.d("zhuzhan", "onBindViewHolder: "+audio_url_right);

                try {
                    AudioTrackManager.getInstance().loadAudio(audio_url_right[0],
                            "audio_message_" + System.currentTimeMillis(), new AudioTrackManager.LoadAudioListener() {
                                @Override
                                public void finish(final String path) {
                                    holder.layout_right.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.d("zhuzhan", "onClick: ");
                                            if (!AudioTrackManager.getInstance().isPlaying()) {
                                                AudioTrackManager.getInstance().startPlay(path);
                                            } else {
                                                AudioTrackManager.getInstance().stopPlay();
                                            }
                                        }
                                    });
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img_left, img_right, pic_icon_left, pic_icon_right, icon_audio_left, icon_audio_right;
        TextView text_left, text_right, time_left, time_right;
        ImageView pic_left, pic_right, img_audio_left, img_audio_right;
        LinearLayout layout_left, layout_right;
        int viewType;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            switch (viewType) {
                case TEXT_LEFT:
                    img_left = itemView.findViewById(R.id.icon_item_left);
                    text_left = itemView.findViewById(R.id.tx_left);
                    break;
                case TEXT_RIGHT:
                    img_right = itemView.findViewById(R.id.icon_item_right);
                    text_right = itemView.findViewById(R.id.tx_right);
                    break;
                case PIC_LEFT:
                    pic_left = itemView.findViewById(R.id.pic_left);
                    pic_icon_left = itemView.findViewById(R.id.icon_pic_item_left);
                    break;
                case PIC_RIGHT:
                    pic_right = itemView.findViewById(R.id.pic_right);
                    pic_icon_right = itemView.findViewById(R.id.icon_pic_item_right);
                    break;
                case AUDIO_LEFT:
                    img_audio_left = itemView.findViewById(R.id.img_audio_left);
                    img_audio_left.setColorFilter(Color.WHITE);
                    time_left = itemView.findViewById(R.id.time_audio_left);
                    icon_audio_left = itemView.findViewById(R.id.icon_audio_left);
                    layout_left = itemView.findViewById(R.id.audio_layout_left);
                    break;
                case AUDIO_RIGHT:
                    img_audio_right = itemView.findViewById(R.id.img_audio_right);
                    img_audio_right.setColorFilter(Color.WHITE);
                    time_right = itemView.findViewById(R.id.time_audio_right);
                    icon_audio_right = itemView.findViewById(R.id.icon_audio_right);
                    layout_right = itemView.findViewById(R.id.audio_layout_right);
                    break;
            }
        }

    }

    public void addMessage(AVIMMessage message) {
        list.add(message);
        notifyDataSetChanged();
    }

}
