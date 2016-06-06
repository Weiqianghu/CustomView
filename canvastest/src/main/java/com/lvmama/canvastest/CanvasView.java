package com.lvmama.canvastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huweiqiang on 2016/6/3.
 */
public class CanvasView extends View {
    private Paint mPaint;


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(100, 100, 100, mPaint);

        RectF rectF = new RectF(100, 100, 1000, 800);

        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectF, mPaint);

        mPaint.setColor(Color.YELLOW);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(rectF, 90, 180, true, mPaint);

        mPaint.setColor(Color.MAGENTA);
        canvas.translate(200, 1000);
        canvas.drawCircle(0, 0, 100, mPaint);

        mPaint.setColor(Color.BLACK);
        canvas.translate(0, 400);
        canvas.drawCircle(0, 0, 100, mPaint);

        canvas.translate(400, 0);
        rectF = new RectF(-300, -300, 300, 300);
        mPaint.setColor(Color.CYAN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        for (int i = 0; i < 10; i++) {
            canvas.rotate(30,150,150);
            canvas.scale(0.9f, 0.9f);
            canvas.drawRect(rectF, mPaint);
        }

    }
}
