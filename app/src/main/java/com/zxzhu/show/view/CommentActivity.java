package com.zxzhu.show.view;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxzhu.show.R;
import com.zxzhu.show.databinding.ActivityCommentBinding;
import com.zxzhu.show.presenter.CommentPresenter;
import com.zxzhu.show.presenter.ICommentPresenter;
import com.zxzhu.show.units.PermissionUnit;
import com.zxzhu.show.units.RecordButton;
import com.zxzhu.show.units.RecordManager;
import com.zxzhu.show.units.SystemUtil;
import com.zxzhu.show.units.base.BaseActivity;
import com.zxzhu.show.view.Inference.ICommentActivity;

import java.io.File;

import static com.zxzhu.show.units.SystemUtil.secToTime;

public class CommentActivity extends BaseActivity implements ICommentActivity{
    private ActivityCommentBinding binding;
    private ICommentPresenter presenter;
    private String id;
    private String mRecordPath;
    private String time;
    private ProgressDialog dialog;
    @Override
    protected void initData() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_comment);
        presenter = new CommentPresenter(this);
        id = getIntent().getStringExtra("objectId");
        TextView textView = $(R.id.header_title);
        textView.setText("发布评论");
        ImageView back = $(R.id.back_header);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        setPublish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_comment;
    }

    @Override
    public void setPublish() {
        setVoice();
        binding.btnPublishComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment_tx = binding.editComment.getText().toString();
                if (comment_tx.equals("")) {
                    toast("评论不能为空");
                } else {
                    presenter.upLoadComment(id,comment_tx,mRecordPath);
                }
            }
        });
    }

    public void setVoice() {
        if (PermissionUnit.hasMicPermission(this)) {
            binding.recordComment.setRecordButtonListener(new RecordButton.RecordButtonListener() {
                long startTime;
                RecordManager recordManager;

                @Override
                public void onStart() {
                    SystemUtil.vibrator(getActivity(), new long[]{30, 20});//震动提示
                    recordManager = new RecordManager();
                    binding.recordComment.setColorFilter(Color.parseColor("#1B5E20"));
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
                        binding.recordComment.setColorFilter(Color.parseColor("#4CAF50"));
                        binding.recordComment.setRecordTime(secToTime((int) audioTime/1000));
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

    public void setVoicePath() {
        mRecordPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Teller/audio/"+"comment_"+MainActivity.USER+System.currentTimeMillis()+".pcm";
        File file = new File(mRecordPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        mRecordPath = file.getPath();
    }

    @Override
    public void showDialog() {
//        if(dialog == null) dialog = new ProgressDialog(this);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
//        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
//        dialog.setCanceledOnTouchOutside(true);// 设置在点击Dialog外是否取消Dialog进度条
//        dialog.setTitle("正在发表");
//        dialog.setMessage("稍等");
//        dialog.show();
    }

    @Override
    public void hideDialog() {
        if (dialog != null) {
            dialog.hide();
        } else return;
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
}
