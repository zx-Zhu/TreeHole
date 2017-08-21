package com.zxzhu.show.view;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.avos.avoscloud.AVUser;
import com.zxzhu.show.R;
import com.zxzhu.show.databinding.ActivityMainBinding;
import com.zxzhu.show.units.SystemUtil;
import com.zxzhu.show.adapters.VPAdapter;
import com.zxzhu.show.units.base.BaseActivity;
import com.zxzhu.show.view.Inference.IMainActivity;
import com.zxzhu.show.view.fragments.CameraFragment;
import com.zxzhu.show.view.fragments.MainFragment;

public class MainActivity extends BaseActivity implements IMainActivity {
    private ActivityMainBinding binding;
    private boolean isStop = false;
    public static String USER = null;

    @Override
    protected void initData() {
        USER = AVUser.getCurrentUser().getUsername();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setViewPager();
    }

    @Override
    public void back(){
        onBackPressed();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void setViewPager() {
        final CameraFragment cameraFragment = new CameraFragment();
        MainFragment mainFragment = new MainFragment();
        Fragment[] fragments = new Fragment[]{cameraFragment, mainFragment};
        binding.pagerMain.setAdapter(new VPAdapter(this, getSupportFragmentManager(), fragments));
        binding.pagerMain.setCurrentItem(1);
        binding.pagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                float f = position + positionOffset;
                Log.d("111", "onPageScrolled: " + f);
                if (f == 1f && !isStop) {
                    cameraFragment.onDestroyView();
                    SystemUtil.toggleFullScreen(MainActivity.this, false);
                    isStop = true;
                    Log.d("111", "onPageScrolled: --------------------stop");
                }
                if (f == 0f && isStop) {
                    Log.d("111", "onPageScrolled: --------------------resume");
                    SystemUtil.toggleFullScreen(MainActivity.this, true);
                    cameraFragment.onStart();
                    isStop = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setHeader() {

    }

    public void openCamera() {
        binding.pagerMain.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        if (binding.pagerMain.getCurrentItem() == 0) {
            binding.pagerMain.setCurrentItem(1);
        } else {
            super.onBackPressed();
        }
    }
}
