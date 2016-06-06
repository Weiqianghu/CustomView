package com.lvmama.canvastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by huweiqiang on 2016/6/3.
 */
public class Beizer extends View {

    private int centerX, centerY;
    private Paint mPaint;

    private PointF mStartPoint, mEndPoint, mControlPoint;

    public Beizer(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(60);

        mStartPoint = new PointF(0, 0);
        mEndPoint = new PointF(0, 0);
        mControlPoint = new PointF(0, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;

        mStartPoint.x = centerX - 200;
        mStartPoint.y = centerY;
        mEndPoint.x = centerX + 200;
        mEndPoint.y = centerY;
        mControlPoint.x = centerX;
        mControlPoint.y = centerY - 100;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mControlPoint.x = event.getX();
        mControlPoint.y = event.getY();
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(20);
        canvas.drawPoint(mStartPoint.x, mStartPoint.y, mPaint);
        canvas.drawPoint(mEndPoint.x, mEndPoint.y, mPaint);
        canvas.drawPoint(mControlPoint.x, mControlPoint.y, mPaint);

        mPaint.setStrokeWidth(2);
        canvas.drawLine(mStartPoint.x, mStartPoint.y, mControlPoint.x, mControlPoint.y, mPaint);
        canvas.drawLine(mEndPoint.x, mEndPoint.y, mControlPoint.x, mControlPoint.y, mPaint);

        mPaint.setColor(Color.MAGENTA);
        Path path = new Path();
        path.moveTo(mStartPoint.x, mStartPoint.y);
        path.quadTo(mControlPoint.x, mControlPoint.y, mEndPoint.x, mEndPoint.y);
        canvas.drawPath(path, mPaint);




    }
}
