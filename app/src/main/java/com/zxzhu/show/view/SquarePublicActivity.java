package com.zxzhu.show.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zxzhu.show.Beans.ImgSenceBean;
import com.zxzhu.show.R;
import com.zxzhu.show.databinding.ActivitySquarePublicBinding;
import com.zxzhu.show.presenter.IPublishPresenter;
import com.zxzhu.show.presenter.PublishPresenter;
import com.zxzhu.show.units.ImageBase64;
import com.zxzhu.show.units.PermissionUnit;
import com.zxzhu.show.units.RecordButton;
import com.zxzhu.show.units.RecordManager;
import com.zxzhu.show.units.SystemUtil;
import com.zxzhu.show.units.base.BaseActivity;
import com.zxzhu.show.view.Inference.ISquarePublicActivity;

import java.io.File;
import java.util.List;

public class SquarePublicActivity extends BaseActivity implements ISquarePublicActivity {
    private ActivitySquarePublicBinding binding;
    private String  picPath, miniPicPath;
    private String time;
    private IPublishPresenter presenter;
    private final int PIC = 0, VOICE = 1, VIDEO = 2;
    private int TYPE = 0;
    private ProgressDialog dialog;
    //语音操作对象
    private MediaRecorder mRecorder = null;
    private String mRecordPath = null;

    @Override
    protected void initData() {
        presenter = new PublishPresenter(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_square_public);
        Intent intent = getIntent();
        picPath = intent.getStringExtra("pic");
        miniPicPath = intent.getStringExtra("picMini");
        Log.d("qqq", "initData: "+picPath);
        Glide.with(this).load(picPath).into(binding.picPublish);
        presenter.getPicInfo(ImageBase64.imgToBase64(this, picPath));
        setVoice();
        setBar();
    }

    private void setBar() {
        TextView title = $(R.id.header_title);
        title.setText("编辑");
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
    protected int getContentViewId() {
        return R.layout.activity_square_public;
    }

    @Override
    public void publish() {
        if (mRecordPath != null) {
            presenter.upLoad(this, picPath, miniPicPath, getDescription(), time, mRecordPath);
        } else {
            presenter.upLoad(this,picPath,miniPicPath,getDescription());
        }
    }


    @Override
    public String getDescription() {
        String description;
        if (binding.editContent.getText()!=null) {
            description = binding.editContent.getText().toString();
        } else description = "ta什么也没说。。";

        return description;
    }

    @Override
    public void setVoice() {
        if (PermissionUnit.hasMicPermission(this)) {
            binding.recordBtn.setRecordButtonListener(new RecordButton.RecordButtonListener() {
                long startTime;
                RecordManager recordManager;

                @Override
                public void onStart() {
                    Log.d("Recorderer", "onStart: ");
                    SystemUtil.vibrator(getActivity(), new long[]{30, 20});//震动提示
                    recordManager = new RecordManager();
                    binding.recordBtn.setColorFilter(Color.parseColor("#1B5E20"));
                    setVoicePath();
                    startTime = System.currentTimeMillis();
                    recordManager.startRecord(mRecordPath);

                }

                @Override
                public void onFinish() {
                    Log.d("Recorderer", "onFinish: " + mRecordPath);
                    long audioTime = System.currentTimeMillis() - startTime;
                    if (audioTime > 500) {
                        recordManager.stopRecord();
                        recordManager = null;
                        binding.recordBtn.setColorFilter(Color.parseColor("#4CAF50"));
                        binding.recordBtn.setRecordTime(secToTime((int) audioTime/1000));
                        time = secToTime((int) audioTime/1000);
//                        AudioTrackManager.getInstance().startPlay(mRecordPath);
                    } else {
                        toast("时间太短，录音无效");
                        File file = new File(mRecordPath);
                        file.delete();
                        mRecordPath = null;
                    }
                }

                @Override
                public void onCancel() {
                    Log.d("Recorderer", "onCancel: ");
                    File file = new File(mRecordPath);
                    file.delete();
                    mRecordPath = null;
                    toast("录音取消");
                }
            });
        } else {
            PermissionUnit.askForMicPermission(this,0);
        }
    }

    @Override
    public void setVoicePath() {
        mRecordPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Show/audio/"+MainActivity.USER+System.currentTimeMillis()+".pcm";
        File file = new File(mRecordPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        mRecordPath = file.getPath();
    }

    public void publishClick(View view) {
        publish();
    }

    @Override
    public void showDialog() {
        if(dialog == null) dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(true);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("正在发布");
        dialog.setMessage("稍等");
        dialog.show();
    }
    @Override
    public void hideDialog() {
        if(dialog == null) return;
        dialog.hide();
    }

    @Override
    public void setImgInfo(ImgSenceBean imgSenceBean) {
        List<ImgSenceBean.ObjectsBean> objs = imgSenceBean.getObjects();
        List<ImgSenceBean.SencesBean> scenes = imgSenceBean.getScenes();
        String tx = null;
        for (ImgSenceBean.ObjectsBean bean : objs) {
            tx += bean.getValue();
        }
        for (ImgSenceBean.SencesBean bean : scenes) {
            tx += bean.getValue();
        }
        binding.tagPic.setText(tx);
    }

    @Override
    public void hideImgInfoLoading() {
        binding.loadingImgInfo.setVisibility(View.GONE);
    }

    @Override
    public void back(){
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    setVoice();
                }else{
                    toast("没有录音权限，请手动开启");
                }
                break;
        }
    }
    /**
     * 将整形转化成时分秒格式字符串
     * @param time
     * @return
     */
    public  String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public  String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
