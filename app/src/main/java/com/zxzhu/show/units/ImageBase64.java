package com.zxzhu.show.units;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by zxzhu on 2017/8/20.
 */

public class ImageBase64 {
    /**
     * 将图片转成Base64形式，并进行了图片压缩
     */
    public static String imgToBase64(Context context, String imgPath) {
        Log.d("ImageUtils", "upload file path = " + imgPath);
        Log.d("ImageUtils", "upload file size = " + new File(imgPath).length());
        Bitmap bitmap = null;
        if (imgPath.length() > 0) {
            bitmap = decodeThumbBitmapForFile(imgPath, 480, 800);
        }
        if (bitmap == null) {
            //bitmap not found!!
            return "";
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            // 进行了压缩图片
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            Log.d("ImageUtils","imgBytes.length "+imgBytes.length);
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据View(主要是ImageView)的宽和高来获取图片的缩略图
     *
     * @param path
     * @param viewWidth
     * @param viewHeight
     * @return
     */
    private static Bitmap decodeThumbBitmapForFile(String path, float viewWidth, float viewHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置为true,表示解析Bitmap对象，该对象不占内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //设置缩放比例
        options.inSampleSize = computeScale(options, viewWidth, viewHeight);

        //设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
     *
     * @param options
     * @param viewWidth
     * @param viewHeight
     */
    private static int computeScale(BitmapFactory.Options options, float viewWidth, float viewHeight) {
        int inSampleSize = 1;
        if (viewWidth == 0 || viewHeight == 0) {
            return inSampleSize;
        }
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;

        //假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
        if (bitmapWidth > viewWidth || bitmapHeight > viewHeight) {
            int widthScale = Math.round((float) bitmapWidth / viewWidth);
            int heightScale = Math.round((float) bitmapHeight / viewWidth);

            //为了保证图片不缩放变形，我们取宽高比例最小的那个
            inSampleSize = widthScale < heightScale ? widthScale : heightScale;
        }
        return inSampleSize;
    }
}

