package com.zxzhu.show.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.zxzhu.show.R;
import com.zxzhu.show.databinding.FragmentCameraBinding;
import com.zxzhu.show.presenter.CameraPresenter;
import com.zxzhu.show.presenter.ICameraPresenter;
import com.zxzhu.show.units.BitmapUtils;
import com.zxzhu.show.units.CameraUtil;
import com.zxzhu.show.units.PermissionUnit;
import com.zxzhu.show.units.SystemUtil;
import com.zxzhu.show.units.base.BaseFragment;
import com.zxzhu.show.view.MainActivity;
import com.zxzhu.show.view.SquarePublicActivity;
import com.zxzhu.show.view.fragments.inference.ICameraFragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zxzhu on 2017/8/17.
 */

public class CameraFragment extends BaseFragment implements SurfaceHolder.Callback, View.OnClickListener, ICameraFragment {
    private FragmentCameraBinding binding;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private int mCameraId = 0;
    private ICameraPresenter presenter;
    private Context context;

    //屏幕宽高
    private int screenWidth;
    private int screenHeight;
    private int index;
    //底部高度 主要是计算切换正方形时的动画高度
    private int menuPopviewHeight;
    //动画高度
    private int animHeight;
    //闪光灯模式 0:关闭 1: 开启 2: 自动
    private int light_num = 0;
    //延迟时间
    private int delay_time;
    private int delay_time_temp;
    private boolean isview = false;
    private boolean is_camera_delay;
    private int picHeight;
    private boolean isFirst = true;

    @Override
    protected void initData() {
        presenter = new CameraPresenter(this);
        context = getActivity();
        binding = getDataBinding();
        initView();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        FrameLayout.LayoutParams params =  (FrameLayout.LayoutParams)binding.coverCamera.getLayoutParams();
        params.height = screenHeight/4;
        binding.coverCamera.setLayoutParams(params);
        menuPopviewHeight = screenHeight - screenWidth * 4 / 3;
        animHeight = (screenHeight - screenWidth - menuPopviewHeight - SystemUtil.dp2px(context, 44)) / 2;

        //这里相机取景框我这是为宽高比3:4 所以限制底部控件的高度是剩余部分
//        RelativeLayout.LayoutParams bottomParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, menuPopviewHeight);
//        bottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        homecamera_bottom_relative.setLayoutParams(bottomParam);
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_camera;
    }


    private void initView() {
        binding.surfaceCamera.getHolder().addCallback(this);
//            img_camera = (ImageView) findViewById(R.id.img_camera);
//            img_camera.setOnClickListener(this);
        mCamera = getCamera(mCameraId);
        //关闭相机界面按钮
        binding.closeCamera.setOnClickListener(this);
        //闪光灯
        binding.lightCamera.setOnClickListener(this);
        //前后摄像头切换
        binding.turnCamera.setOnClickListener(this);
        //拍照
        binding.btnCamera.setOnClickListener(this);
        //打开相册
        binding.albumCamera.setOnClickListener(this);


    }


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {

            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //切换摄像头
            case R.id.turn_camera:
                Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate);
                binding.turnCamera.startAnimation(animation);
                switchCamera();
                break;

            case R.id.album_camera:
                if (PermissionUnit.hasDiskPermission(getActivity())) {
                    Intent intent;
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                } else {
                    PermissionUnit.askForDiskPermission(this, 1);
                }

            //退出相机界面 释放资源
            case R.id.close_camera:
                getActivity().onBackPressed();
                break;

            //闪光灯
            case R.id.light_camera:
                if (mCameraId == 1) {
                    //前置
                    toast("前置摄像头无法开启闪光灯");
                    return;
                }
                Camera.Parameters parameters = mCamera.getParameters();
                switch (light_num) {
                    case 0:
                        //打开
                        light_num = 1;
                        binding.lightCamera.setImageResource(R.drawable.light_on);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                        mCamera.setParameters(parameters);
                        break;
                    case 1:
                        //关闭
                        light_num = 0;
                        //关闭
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(parameters);
                        binding.lightCamera.setImageResource(R.drawable.light_off);
                        break;
                }

                break;
            case R.id.btn_camera:
                captrue();
                break;

        }
    }

    public void switchCamera() {
        releaseCamera();
        mCameraId = (mCameraId + 1) % mCamera.getNumberOfCameras();
        mCamera = getCamera(mCameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }
    }


//        @Override
//        protected void onResume() {
//            super.onResume();
//            if (mCamera == null) {
//                mCamera = getCamera(mCameraId);
//                if (mHolder != null) {
//                    startPreview(mCamera, mHolder);
//                }
//            }
//        }
//
//        @Override
//        protected void onPause() {
//            super.onPause();
//            releaseCamera();
//        }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {

        }
        return camera;
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            Log.d(TAG, "startPreview: ");
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(getActivity(), mCameraId, camera);
//            camera.setDisplayOrientation(90);
            camera.startPreview();
            isview = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void captrue() {
        toast("咔嚓");
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                isview = false;
                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap saveBitmap = CameraUtil.getInstance().setTakePicktrueOrientation(mCameraId, bitmap);
                saveBitmap = Bitmap.createScaledBitmap(saveBitmap, screenWidth, picHeight, true);
                Bitmap pic = Bitmap.createBitmap(saveBitmap, 0, 0, screenWidth, screenWidth*4/3);
                Bitmap miniPic = Bitmap.createScaledBitmap(pic,screenWidth/4, screenWidth/3,true);
                String picPath = presenter.saveBitmap(pic,"pic_"+ MainActivity.USER+System.currentTimeMillis());
                String picMiniPath = presenter.saveBitmap(miniPic, "picMini_"+ MainActivity.USER+System.currentTimeMillis());
                Log.d(TAG, "onPictureTaken: "+picPath);
                Intent intent = new Intent(getActivity(), SquarePublicActivity.class);
                intent.putExtra("pic",picPath);
                intent.putExtra("picMini",picMiniPath);
                startActivity(intent);

                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }

                if (!saveBitmap.isRecycled()) {
                    saveBitmap.recycle();
                }

            }
        });
    }

    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        //预览尺寸
        Camera.Size previewSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPreviewSizes(), 800);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        //照片尺寸
        Camera.Size pictrueSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPictureSizes(), 800);
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);

        camera.setParameters(parameters);

        /**
         * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
         * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
         * previewSize.width才是surfaceView的高度
         * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
         *
         */

        picHeight = (screenWidth * pictrueSize.width) / pictrueSize.height;

//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, (screenWidth * pictrueSize.width) / pictrueSize.height);
//        //这里当然可以设置拍照位置 比如居中 我这里就置顶了
////        params.gravity = Gravity.CENTER;
//        binding.surfaceCamera.setLayoutParams(params);
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        light_num = 0;
        binding.lightCamera.setImageResource(R.drawable.light_off);
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated: ");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged: ");
        mHolder = holder;
        if (mCamera != null) {
            mCamera.stopPreview();
            startPreview(mCamera, holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed: ");
        releaseCamera();
    }

    private static final String TAG = "CameraTest";
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && isFirst) {
//            Log.i("TestData", "FoundFragment 加载相机");
//            startPreview(mCamera, mHolder);
//            isFirst = false;
//        }
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        Log.d(TAG, "onAttachFragment: ");
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");

        if (PermissionUnit.hasCameraPermission(getActivity())) {
            Log.i("TestData", "FoundFragment 加载相机");
            mCamera = getCamera(mCameraId);
            if (mHolder != null) {
                startPreview(mCamera, mHolder);
            }
        } else {
            PermissionUnit.askForCameraPermission(this,0);
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory: ");
        super.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        releaseCamera();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: ---------------------有权限");
//                    onStart();
                }else{
                    toast("没有相机权限，请手动开启");
                }
                break;
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: ---------------------有权限");
                    Intent intent;
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }else{
                    toast("没有SD卡权限，请手动开启");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri uri = data.getData();
            Bitmap pic , miniPic ;
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            options.inSampleSize = 4;
            InputStream input = null;
            try {
               input = getActivity().getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onActivityResult: "+input.toString());
            pic = BitmapFactory.decodeStream(input);
            pic = Bitmap.createScaledBitmap(pic,pic.getWidth()/4, pic.getHeight()/4,true);
            pic = BitmapUtils.rotaingImageView(BitmapUtils.readPictureDegree(uri.getPath().replace("/raw/","")),pic);
            Log.d(TAG, "onActivityResult: "+uri.getPath().replace("/raw/",""));
            miniPic = Bitmap.createScaledBitmap(pic,pic.getWidth()/2, pic.getHeight()/2,true);
            String picPath = presenter.saveBitmap(pic,"pic_"+ MainActivity.USER+System.currentTimeMillis());
            String picMiniPath = presenter.saveBitmap(miniPic, "picMini_"+ MainActivity.USER+System.currentTimeMillis());
            Log.d(TAG, "onPictureTaken: "+picPath);
            Intent intent = new Intent(getActivity(), SquarePublicActivity.class);
            intent.putExtra("pic",picPath);
            intent.putExtra("picMini",picMiniPath);
            startActivity(intent);
        }
    }

}
