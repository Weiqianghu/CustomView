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
public class SpiderView extends View {

    private int count = 6;
    private float angle = (float) (Math.PI * 2 / count);
    private float radius = 400;
    private int centerX;
    private int centerY;
    private String[] titles = {"a", "b", "c", "d", "e", "f"};
    private double[] data = {100,60,60,60,100,50,10,20};
    private double maxValue = 100;

    private Paint mainPaint;
    private Paint valuePaint;
    private Paint textPaint;

    public SpiderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();

        init();
    }

    private void init() {
        mainPaint = new Paint();
        mainPaint.setColor(Color.GRAY);
        mainPaint.setAntiAlias(true);
        mainPaint.setStyle(Paint.Style.STROKE);
        mainPaint.setStrokeWidth(2);

        textPaint = new Paint();
        textPaint.setColor(Color.BLUE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(24);

        valuePaint = new Paint();
        valuePaint.setColor(Color.GREEN);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        valuePaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPolygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawRegion(canvas);
    }

    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float r = radius / (count - 1);

        for (int i = 1; i < count; i++) {
            float curR = r * i;
            path.reset();
            for (int j = 0; j < count; j++) {
                if (j == 0) {
                    path.moveTo(centerX + curR, centerY);
                } else {
                    float x = (float) (centerX + curR * Math.cos(angle * j));
                    float y = (float) (centerY + curR * Math.sin(angle * j));
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, mainPaint);
        }
    }

    private void drawLines(Canvas canvas) {
        Path path = new Path();

        for (int i = 0; i < count; i++) {
            path.reset();
            path.moveTo(centerX, centerY);
            float x = (float) (centerX + radius * Math.cos(angle * i));
            float y = (float) (centerY + radius * Math.sin(angle * i));
            path.lineTo(x, y);
            canvas.drawPath(path, mainPaint);
        }
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i = 0; i < count; i++) {
            float x = (float) (centerX + (radius + fontHeight / 2) * Math.cos(angle * i));
            float y = (float) (centerY + (radius + fontHeight / 2) * Math.sin(angle * i));

            if (y >= 0) {
                canvas.drawText(titles[i], x, y, textPaint);
            } else {
                float dis = textPaint.measureText(titles[i]);//文本长度
                canvas.drawText(titles[i], x - dis, y, textPaint);
            }
        }
    }

    private void drawRegion(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            double percent = data[i] / maxValue;
            float x = (float) (centerX + radius * Math.cos(angle * i) * percent);
            float y = (float) (centerY + radius * Math.sin(angle * i) * percent);
            if (i == 0) {
                path.moveTo(x, centerY);
            } else {
                path.lineTo(x, y);
            }
            //绘制小圆点
            canvas.drawCircle(x, y, 10, valuePaint);
        }

        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, valuePaint);

        valuePaint.setAlpha(127);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);
    }
}
