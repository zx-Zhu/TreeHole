package com.zxzhu.show.presenter;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.zxzhu.show.Beans.ImgSenceBean;
import com.zxzhu.show.model.HttpModel;
import com.zxzhu.show.model.IUpLoadModel;
import com.zxzhu.show.model.UpLoadModel;
import com.zxzhu.show.units.TensorFlowUtils.Classifier;
import com.zxzhu.show.units.TensorFlowUtils.TensorFlowImageClassifier;
import com.zxzhu.show.view.Inference.ISquarePublicActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import rx.Subscriber;

/**
 * Created by zxzhu on 2017/8/18.
 */

public class PublishPresenter implements IPublishPresenter {

    private IUpLoadModel upLoadModel = new UpLoadModel();
    private ISquarePublicActivity activity;

    //TF相关
    private static int INPUT_SIZE = 224;
    private static int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";
    private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
    private static final String LABEL_FILE =
            "file:///android_asset/imagenet_comp_graph_label_strings.txt";
    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();

    public PublishPresenter(ISquarePublicActivity activity) {
        this.activity = activity;
    }

    @Override
    public void upLoad(Context context, String pic, String miniPic, String description, Boolean isAnonymity) {
        upLoadModel.upLoadPic(context, pic, miniPic, description, isAnonymity, new UpLoadModel.UpLoadListener() {
            @Override
            public void onStart() {
                activity.showDialog();
            }

            @Override
            public void onFinish() {
                activity.back();
            }
        });
    }

    @Override
    public void upLoad(Context context, String picPath, String miniPicPath, String description, String time, String voicePath, Boolean isAnonymity) {
        upLoadModel.upLoadVoice(context, picPath, miniPicPath, description, voicePath, time, isAnonymity, new UpLoadModel.UpLoadListener() {
            @Override
            public void onStart() {
                activity.showDialog();
            }

            @Override
            public void onFinish() {
                activity.back();
            }
        });
    }

    @Override
    public void getPicInfo(String bitmap64, AssetManager assetManager) {
        HttpModel.bulid().getPhotoInfo(bitmap64, new Subscriber<ImgSenceBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("888", "onError: "+e.getMessage().toString());
            }

            @Override
            public void onNext(ImgSenceBean imgSenceBean) {
                activity.setImgInfo(imgSenceBean);
                Log.d("888", "onNext: "+imgSenceBean.getError_message());
                activity.hideImgInfoLoading();
            }
        });
//        int h = bitmap.getHeight(), w = bitmap.getWidth();
//        initTensorFlow(assetManager);
//        Bitmap mBit1 = Bitmap.createBitmap(bitmap, h / 8, 0, w, w, null, false);
//        Bitmap mBit2 = Bitmap.createScaledBitmap(mBit1, INPUT_SIZE, INPUT_SIZE, false);
//        List<Classifier.Recognition> results = classifier.recognizeImage(mBit2);
//        String recognitionResult = "识别结果：\n";
//        if (results.size() == 0) recognitionResult = "未能识别出物体";
//        else {
//            for (Classifier.Recognition result : results) {
//                recognitionResult += result.toString() + "\n";
//            }
//        }
//        return recognitionResult;
    }

    @Override
    public void initTensorFlow(final AssetManager assetManager) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            assetManager,
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME );
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }


}
