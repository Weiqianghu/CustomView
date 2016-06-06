package com.lvmama.canvastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huweiqiang on 2016/6/3.
 */
public class PathView extends View {
    private static final String TAG = "PathView";

    private Paint mPaint;

    private int mWidth;
    private int mHeight;


    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);

        Path path = new Path();

        path.addRect(-200, -200, 200, 200, Path.Direction.CCW);
        path.setLastPoint(-300, 300);

        canvas.drawPath(path, mPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }
}
