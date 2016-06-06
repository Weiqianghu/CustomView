package com.lvmama.canvastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huweiqiang on 2016/6/3.
 */
public class CanvasImg extends View {
    private static final String TAG = "CanvasImg";

    private Paint mPaint;
    private Picture mPicture;

    public CanvasImg(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.CYAN);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mPicture = new Picture();
        recording();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPicture(mPicture);
    }

    private void recording() {
        // 开始录制 (接收返回值Canvas)
        Canvas canvas = mPicture.beginRecording(500, 500);
        // 创建一个画笔
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        // 在Canvas中具体操作
        // 位移
        canvas.translate(250, 250);
        // 绘制一个圆
        canvas.drawCircle(0, 0, 100, paint);

        mPicture.endRecording();
    }
}
