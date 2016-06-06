package com.lvmama.piechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by huweiqiang on 2016/5/16.
 */
public class PieChart extends View {

    private int[] mColors = {Color.GREEN, Color.BLACK, Color.GREEN, Color.RED, Color.YELLOW,Color.BLUE,Color.GRAY};
    private float mStartAngle = 0;
    private List<PieData> mPieDatas;
    private int mWidth, mHeight;
    private Paint mPaint;


    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPieDatas == null || mPieDatas.size() <= 0) {
            return;
        }

        float currentStartAngle = mStartAngle;
        canvas.translate(mWidth / 2, mHeight / 2);
        float r = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);
        RectF rectF = new RectF(-r, -r, r, r);

        for (int i = 0, length = mPieDatas.size(); i < length; i++) {
            PieData pieData = mPieDatas.get(i);

            mPaint.setColor(pieData.getColor());
            canvas.drawArc(rectF, currentStartAngle, pieData.getAngle(), true, mPaint);
            currentStartAngle += pieData.getAngle();
        }
    }

    public void setStartAngle(int startAngle) {
        this.mStartAngle = startAngle;
    }

    public void stetData(List<PieData> pieDatas) {
        this.mPieDatas = pieDatas;
        initData(mPieDatas);
        invalidate();
    }

    private void initData(List<PieData> pieDatas) {
        if (pieDatas == null || pieDatas.size() <= 0) {
            return;
        }

        float sum = 0;
        for (int i = 0, length = pieDatas.size(); i < length; i++) {
            PieData pieData = pieDatas.get(i);

            sum += pieData.getValue();

            int j = i % mColors.length;
            pieData.setColor(mColors[j]);
        }

        float sumAngle = 0;
        for (int i = 0, length = pieDatas.size(); i < length; i++) {
            PieData pieData = pieDatas.get(i);
            float percentage = pieData.getValue() / sum;
            float angle = percentage * 360;

            pieData.setPercentage(percentage);
            pieData.setAngle(angle);
            sumAngle += angle;
        }
    }


}
