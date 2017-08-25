package com.zxzhu.show.units;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageButton;

/**
 * Created by zxzhu on 2017/8/19.
 */

public class RecordButton extends ImageButton {
    private RecordButtonListener listener;
    private String time = "";
    public RecordButton(Context context) {
        super(context);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public interface RecordButtonListener {
        void onStart();
        void onFinish();
        void onCancel();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (listener != null) listener.onStart();
                break;
            case MotionEvent.ACTION_UP:
                Log.d("RecorderBT", "dispatchTouchEvent: UP");
                if (listener != null) listener.onFinish();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (listener != null) listener.onCancel();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动
        mPaint.setTextSize(25);
        mPaint.setColor(Color.GRAY);
        canvas.drawText(time,getWidth()/2-mPaint.measureText(time)/2,getHeight()-10,mPaint);
        super.onDraw(canvas);
    }

    public void setRecordButtonListener(RecordButtonListener listener) {
        this.listener = listener;
    }

    public void setRecordTime(String time) {
        this.time = time;
        invalidate();
    }
}
