package com.zxzhu.show.view.fragments;

import android.content.Intent;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.zxzhu.show.R;
import com.zxzhu.show.databinding.FragmentMineBinding;
import com.zxzhu.show.units.base.BaseFragment;
import com.zxzhu.show.view.AboutActivity;
import com.zxzhu.show.view.ChangeDataActivity;
import com.zxzhu.show.view.ChangePasswordActivity;
import com.zxzhu.show.view.LoginActivity;
import com.zxzhu.show.view.UserActivity;

public class MineFragment extends BaseFragment {
    private FragmentMineBinding b;
    @Override
    protected void initData() {
        b = getDataBinding();
        setContent();
        setChangeData();
        setQuit();
        setChangePassword();
        setShare();
        setOpinion();
        setAbout();
        setAboutMe();
    }

    private void setAboutMe() {
        b.aboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("尚未完善此功能");
            }
        });
    }

    private void setChangeData() {
        b.changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeDataActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setContent() {
        Glide.with(this).load(AVUser.getCurrentUser().getAVFile("head").getUrl())
                .crossFade().into(b.iconMine);
        b.nameMine.setText(AVUser.getCurrentUser().get("nickname").toString());
        if (AVUser.getCurrentUser().get("readme") == null || AVUser.getCurrentUser().get("readme").toString().equals("")) {
            b.readmeMine.setText("你还没有添加简介哦");
        } else {
            b.readmeMine.setText(AVUser.getCurrentUser().get("readme").toString());
        }
        b.mineData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                intent.putExtra("id", AVUser.getCurrentUser().getObjectId());
                startActivity(intent);
            }
        });

    }

    private void setQuit() {
        b.quiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quit();
            }
        });
    }

    public void quit() {
        AVUser.logOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

    private void setChangePassword() {
        b.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setShare(){
        b.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "Come on ！！！下载\"讲述者\"来讲述你的故事"+"http://ac-xKo7tm6a.clouddn.com/d1bff1ddb2b8f4413f39.apk");
                shareIntent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "将应用分享到"));
            }
        });

    }

    private void setOpinion() {
        b.opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] email = {"zxzhu1998@foxmail.com"}; // 需要注意，email必须以数组形式传入
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822"); // 设置邮件格式
                intent.putExtra(Intent.EXTRA_EMAIL, email); // 接收人
                intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                intent.putExtra(Intent.EXTRA_SUBJECT, "Teller意见反馈"); // 主题
                intent.putExtra(Intent.EXTRA_TEXT, ""); // 正文
                startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
            }
        });
    }

    private void setAbout() {
        b.about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        if (AVUser.getCurrentUser() != null) {
            setContent();
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        super.onResume();
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_mine;
    }

}
