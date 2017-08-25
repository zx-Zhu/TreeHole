package com.zxzhu.show.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zxzhu.show.R;
import com.zxzhu.show.adapters.ChatRecyclerAdapter;
import com.zxzhu.show.databinding.ActivityChatBinding;
import com.zxzhu.show.model.MessageModel;
import com.zxzhu.show.presenter.ChatPresenter;
import com.zxzhu.show.presenter.IChatPresenter;
import com.zxzhu.show.units.PermissionUnit;
import com.zxzhu.show.units.RecordManager;
import com.zxzhu.show.units.SystemUtil;
import com.zxzhu.show.units.base.BaseActivity;
import com.zxzhu.show.view.Inference.IChatActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.zxzhu.show.units.SystemUtil.secToTime;

public class ChatActivity extends BaseActivity implements IChatActivity, MessageModel.MessageListener {
    private ActivityChatBinding binding;
    private String user, id, leftUrl, rightUrl;
    private IChatPresenter presenter;
    private ChatRecyclerAdapter adapter;
    private AVIMConversation conversation;
    private RecordManager recordManager;
    private String mRecordPath;
    private int size;

    @Override
    protected void initData() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        presenter = new ChatPresenter(this);
        recordManager = new RecordManager();
        setBar();
        getMessage();
        refreshMessage();
        sendPicMessage();
        conversation = presenter.getConversation(id);
        binding.editChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    binding.sendBtnChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendMessage();
                        }
                    });
                    binding.sendBtnChat.setBackgroundResource(R.drawable.button_orange);
                } else {
                    binding.sendBtnChat.setBackgroundResource(R.drawable.btn_unable);
                    binding.sendBtnChat.setOnClickListener(null);
                }
            }
        });
        setAudio();
        binding.imgAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.recordBtn.getVisibility() == View.VISIBLE) {
                    binding.recordBtn.setVisibility(View.GONE);
                    binding.editChat.setVisibility(View.VISIBLE);
                    binding.imgAudio.setImageResource(R.drawable.voice_public);
                } else {
                    binding.recordBtn.setVisibility(View.VISIBLE);
                    binding.editChat.setVisibility(View.GONE);
                    binding.imgAudio.setImageResource(R.drawable.keyboard);
                }
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
        id = intent.getStringExtra("id");
        String nickname = intent.getStringExtra("nickname");
        leftUrl = intent.getStringExtra("leftUrl");
        rightUrl = intent.getStringExtra("rightUrl");
        TextView title = $(R.id.header_title);
        title.setText(nickname);
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
        binding.editChat.setText("");
        AVIMTextMessage txMessage = new AVIMTextMessage();
        txMessage.setText(tx);
        txMessage.setFrom(AVUser.getCurrentUser().getUsername());
        size += 1;
        txMessage.setFrom(AVUser.getCurrentUser().getUsername());
        adapter.addMessage(txMessage);
        binding.listChat.smoothScrollToPosition(size);
        presenter.sendMessage(conversation, tx);
    }

    @Override
    public void sendPicMessage() {
        if (PermissionUnit.hasDiskPermission(this)) {
            binding.imgPicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Matisse.from(ChatActivity.this)
                            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
//                            .capture(true)
//                            .captureStrategy(
//                                    new CaptureStrategy(true, "com.zxzhu.show.fileprovider"))
                            .countable(true)
                            .maxSelectable(9)
                            .gridExpectedSize(400)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .theme(R.style.Matisse_Dracula)
                            .imageEngine(new GlideEngine())
                            .forResult(000);
                }
            });
        } else {
            PermissionUnit.askForDiskPermission(this, 0);
        }
    }

    @Override
    public void sendAudioMessage(String time) {
        if (mRecordPath != null) {
            presenter.sendAudioMessage(conversation, mRecordPath, time);
            AVIMAudioMessage message = null;
            try {
                message = new AVIMAudioMessage(mRecordPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            message.setText(time);
            message.setFrom(AVUser.getCurrentUser().getUsername());
            size += 1;
            binding.listChat.smoothScrollToPosition(size);
            adapter.addMessage(message);
        }
    }

    @Override
    public void setAudio() {
        if (PermissionUnit.hasMicPermission(this)) {
            binding.recordBtn.setOnTouchListener(new View.OnTouchListener() {
                long startTime;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getY() < -80) {
                        binding.recordBtn.setTextColor(Color.RED);
                        binding.recordBtn.setText("取消发送");
                    } else {
                        binding.recordBtn.setTextColor(Color.parseColor("#999999"));
                        binding.recordBtn.setText("松开  发送");
                    }
                    Log.d("ffff", "onTouch: " + motionEvent.getAction() + "  " + motionEvent.getY());
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startTime = 0;
                            binding.recordBtn.setText("松开  发送");
                            SystemUtil.vibrator(ChatActivity.this, new long[]{30, 20});
                            startTime = System.currentTimeMillis();
                            setVoicePath();
                            Log.d("zhuzhan", "onTouch: "+mRecordPath);
                            recordManager.startRecord(mRecordPath);
                            break;
                        case MotionEvent.ACTION_UP:
                            long endTime = System.currentTimeMillis();
                            if (endTime - startTime > 500 && motionEvent.getY() > -70) {
                                recordManager.stopRecord();
                                binding.recordBtn.setText("按住  录音");
                                binding.recordBtn.setTextColor(Color.parseColor("#999999"));
                                String time = secToTime((int) (endTime - startTime) / 1000);
                                sendAudioMessage(time);
                            } else if (endTime - startTime < 500) {
                                toast("时间太短，录音无效");
                                recordManager.stopRecord();
                                File file = new File(mRecordPath);
                                if (file.exists()) {
                                    file.delete();
                                }
                                binding.recordBtn.setText("按住  录音");
                                binding.recordBtn.setTextColor(Color.parseColor("#999999"));
                                mRecordPath = null;
                            } else {
                                recordManager.stopRecord();
                                File file = new File(mRecordPath);
                                if (file.exists()) {
                                    file.delete();
                                }
                                binding.recordBtn.setText("按住  录音");
                                binding.recordBtn.setTextColor(Color.parseColor("#999999"));
                                mRecordPath = null;
                            }
                            break;

                    }
                    return false;
                }
            });
        } else {
            PermissionUnit.askForMicPermission(this, 1);
        }
    }

    public void setVoicePath() {
        mRecordPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Teller/audio/" + MainActivity.USER + System.currentTimeMillis() + ".pcm";
        File file = new File(mRecordPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        mRecordPath = file.getPath();
    }

    @Override
    public void getMessage() {
        presenter.getMessage(id);
    }

    @Override
    public void setList(final List<AVIMMessage> list) {
        adapter = new ChatRecyclerAdapter(ChatActivity.this, list, user, leftUrl, rightUrl);
        binding.listChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        binding.listChat.setAdapter(adapter);
        size = list.size() - 1;
        binding.listChat.scrollToPosition(size);
    }

    @Override
    public void refreshMessage() {
        presenter.refreshMessage(this);
    }

    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        size += 1;
        adapter.addMessage(message);
        binding.listChat.smoothScrollToPosition(size);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 000:
                if (data == null) break;
                List<Uri> uris = Matisse.obtainResult(data);
                for (Uri uri : uris) {
                    String result;
                    Cursor cursor = this.getContentResolver().query(uri,
                            new String[]{MediaStore.Images.ImageColumns.DATA},
                            null, null, null);
                    if (cursor == null) result = uri.getPath();
                    else {
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        result = cursor.getString(index);
                        cursor.close();
                    }
                    Log.d("!!!", "onActivityResult: " + result);
                    AVIMImageMessage message = null;
                    try {
                        message = new AVIMImageMessage(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (message == null) toast("该图片不存在");
                    else {
                        size += 1;
                        message.setFrom(AVUser.getCurrentUser().getUsername());
                        adapter.addMessage(message);
                        binding.listChat.smoothScrollToPosition(size);
                        presenter.sendPicMessage(conversation, result);
                    }
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendPicMessage();
                } else {
                    toast("没有SD卡权限，请手动开启");
                }
                break;
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setAudio();
                } else {
                    toast("没有麦克风权限，请手动开启");
                }
        }
    }
}
