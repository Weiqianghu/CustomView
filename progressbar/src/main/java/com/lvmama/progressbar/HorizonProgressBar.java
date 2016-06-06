package com.lvmama.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by huweiqiang on 2016/6/6.
 */
public class HorizonProgressBar extends ProgressBar {
    private static final String TAG = "HorizonProgressBar";

    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = Color.GRAY;
    private static final int DEFAULT_REACH_COLOR = Color.GRAY;
    private static final int DEFAULT_REACH_HEIGHT = 10;//dp
    private static final int DEFAULT_UNREACH_COLOR = Color.LTGRAY;
    private static final int DEFAULT_UNREACH_HEIGHT = 10;//dp
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp


    private float mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mReachColor = DEFAULT_REACH_COLOR;
    private float mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    private int mUnReachColor = DEFAULT_UNREACH_COLOR;
    private float mUnReachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);
    private float mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    private int mRealWidth;

    private Paint mPaint;


    public HorizonProgressBar(Context context) {
        this(context, null);
    }

    public HorizonProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizonProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HorizonProgressBar);

        mTextSize = ta.getDimension(R.styleable.HorizonProgressBar_textSize, mTextSize);
        mTextColor = ta.getColor(R.styleable.HorizonProgressBar_textColor, mTextColor);
        mReachColor = ta.getColor(R.styleable.HorizonProgressBar_reachColor, mReachColor);
        mReachHeight = ta.getDimension(R.styleable.HorizonProgressBar_reachHeight, mReachHeight);
        mUnReachColor = ta.getColor(R.styleable.HorizonProgressBar_unReachColor, mUnReachColor);
        mUnReachHeight = ta.getDimension(R.styleable.HorizonProgressBar_unReachHeight, mUnReachHeight);
        mTextOffset = ta.getDimension(R.styleable.HorizonProgressBar_textOffset, mTextOffset);
        ta.recycle();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(width, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        boolean noNeedUnRech = false;

        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * mRealWidth;

        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);

        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnRech = true;
        }
        float endX = progressX - mTextOffset / 2;

        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }

        mPaint.setColor(mTextColor);
        int y = (int) (((mPaint.descent() - mPaint.ascent())) / 2);
        canvas.drawText(text, progressX, y, mPaint);

        if (!noNeedUnRech) {
            float start = progressX + mTextOffset / 2 + textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }

        canvas.restore();
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = (int) (getPaddingTop() + getPaddingBottom() +
                    Math.max(Math.max(mReachHeight, mUnReachHeight), textHeight));

            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }

        return result;
    }

    private float dp2px(int dp) {
        return getResources().getDisplayMetrics().density * dp + 0.5f;
    }

    private float sp2px(int sp) {
        return getResources().getDisplayMetrics().scaledDensity * sp + 0.5f;
    }
}
