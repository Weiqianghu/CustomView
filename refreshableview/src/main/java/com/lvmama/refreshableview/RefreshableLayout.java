package com.lvmama.refreshableview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by huweiqiang on 2016/5/16.
 */
public class RefreshableLayout extends LinearLayout implements View.OnTouchListener {
    /**
     * 下拉状态
     */
    public static final int STATUS_PULL_TO_REFRESH = 0;

    /**
     * 释放立即刷新状态
     */
    public static final int STATUS_RELEASE_TO_REFRESH = 1;

    /**
     * 正在刷新状态
     */
    public static final int STATUS_REFRESHING = 2;

    /**
     * 刷新完成或未刷新状态
     */
    public static final int STATUS_REFRESH_FINISHED = 3;

    private View mLayout;
    private View mHeadView;
    private View mContentView;
    private TextView mDescription;
    private ProgressBar mProgressBar;
    private ImageView mArrow;

    private int mHeadViewHeight;
    private volatile MarginLayoutParams mHeaderLayoutParams;
    private int touchSlop;

    private int currentStatus = STATUS_REFRESH_FINISHED;

    private boolean firstLoad = true;
    private boolean refreshState;

    private RefreshListener mRefreshListener;

    public RefreshableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initHead(context, attrs);
        initContent();
        touchSlop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();

    }

    private void initContent() {

    }

    private void initHead(Context context, AttributeSet attrs) {
        mLayout = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh, null);

        mHeadView = mLayout.findViewById(R.id.head);

        mProgressBar = (ProgressBar) mLayout.findViewById(R.id.progress_bar);
        mArrow = (ImageView) mLayout.findViewById(R.id.arrow);
        mDescription = (TextView) mLayout.findViewById(R.id.description);
        this.addView(mLayout);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (firstLoad) {
            mHeadViewHeight = mHeadView.getHeight();

            mHeaderLayoutParams = (MarginLayoutParams) mHeadView.getLayoutParams();
            mHeaderLayoutParams.topMargin = -mHeadViewHeight;

            if (getChildCount() != 2) {
                throw new IllegalArgumentException("RefreshableLayout 只能有一个子控件（类似于）ScrollView");
            }

            mContentView = getChildAt(1);
            while (mContentView instanceof ViewGroup && !(mContentView instanceof AbsListView)
                    ) {
                if (((ViewGroup) mContentView).getChildCount() != 1) {
                    throw new IllegalArgumentException("RefreshableLayout 只能有一个子控件（类似于）ScrollView");
                }
                mContentView = ((ViewGroup) mContentView).getChildAt(0);
            }
            mContentView.setOnTouchListener(this);

            firstLoad = false;
        }
    }

    float yDown = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.d("tag", "mHeadView:" + mHeadView.getBottom());
        if (!isAbleToPull()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float yMove = event.getRawY();
                int distance = (int) (yMove - yDown);
                // 如果手指是下滑状态，并且下拉头是完全隐藏的，就屏蔽下拉事件
                if (distance <= 0 && mHeaderLayoutParams.topMargin <= mHeadViewHeight) {
                    return false;
                }
                if (distance < touchSlop) {
                    return false;
                }
                if (currentStatus != STATUS_REFRESHING && mHeaderLayoutParams.topMargin <= 20) {
                    if (mHeaderLayoutParams.topMargin >= 0) {
                        currentStatus = STATUS_RELEASE_TO_REFRESH;
                        mDescription.setText(R.string.release_to_refresh);
                    } else {
                        currentStatus = STATUS_PULL_TO_REFRESH;
                    }
                    // 通过偏移下拉头的topMargin值，来实现下拉效果
                    mHeaderLayoutParams.topMargin = (distance) - mHeadViewHeight;
                    mHeadView.requestLayout();
                }
                break;

            case MotionEvent.ACTION_UP:
                if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                    // 松手时如果是释放立即刷新状态，就去调用正在刷新的任务
                    startRefreshing();

                } else if (currentStatus == STATUS_PULL_TO_REFRESH) {
                    // 松手时如果是下拉状态，就去调用隐藏下拉头的任务
                    stopRefreshing();
                }
                break;
        }
        // 时刻记得更新下拉头中的信息
        if (currentStatus == STATUS_PULL_TO_REFRESH
                || currentStatus == STATUS_RELEASE_TO_REFRESH) {
            //updateHeaderView();
            // 当前正处于下拉或释放状态，要让ListView失去焦点，否则被点击的那一项会一直处于选中状态
           /* listView.setPressed(false);
            listView.setFocusable(false);
            listView.setFocusableInTouchMode(false);
            lastStatus = currentStatus;*/
            // 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
            return true;
        }

        return false;
    }


    public void startRefreshing() {
        mHeaderLayoutParams.topMargin = 0;
        mHeadView.requestLayout();
        mProgressBar.setVisibility(VISIBLE);
        mArrow.setVisibility(GONE);
        mDescription.setText(R.string.refreshing);
        currentStatus = STATUS_REFRESHING;
        if (mRefreshListener != null) {
            mRefreshListener.refresh();
        }
    }

    public void stopRefreshing() {
        mHeaderLayoutParams.topMargin = -mHeadViewHeight;
        mHeadView.requestLayout();
        mProgressBar.setVisibility(GONE);
        mArrow.setVisibility(VISIBLE);
        mDescription.setText(R.string.pull_to_refresh);
        currentStatus = STATUS_REFRESH_FINISHED;
    }

    public boolean isRefreshState() {
        if (currentStatus == STATUS_REFRESHING) {
            return true;
        }
        return refreshState;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    private boolean isAbleToPull() {
        if (!(mContentView instanceof ViewGroup)) {
            return true;
        }
        ViewGroup viewGroup = (ViewGroup) mContentView;

        if (viewGroup.getChildCount() == 0) {
            return true;
        }

        if (viewGroup instanceof AbsListView) {
            AbsListView listView = (AbsListView) viewGroup;
            if (listView.getFirstVisiblePosition() > 0) {
                return false;
            }
        }

        /**
         * 如果 SDK 版本为 14 以上，可以用 canScrollVertically 判断是否能在竖直方向上，向上滑动</br>
         * 不能向上，表示已经滑动到在顶部或者 Content 不能滑动，返回 true，可以下拉</br>
         * 可以向上，返回 false，不能下拉
         */
        if (Build.VERSION.SDK_INT >= 14) {
            return !mContentView.canScrollVertically(-1);
        } else {
            /**
             * SDK 版本小于 14，如果 Content 是 ScrollView 或者 AbsListView,通过 getScrollY 判断滑动位置 </br>
             * 如果位置为 0，表示在最顶部，返回 true，可以下拉
             */
            if (viewGroup instanceof ScrollView || viewGroup instanceof AbsListView) {
                return viewGroup.getScrollY() == 0;
            }
        }

        /**
         * 最终判断，判断第一个子 View 的 top 值</br>
         * 如果第一个子 View 有 margin，则当 top==子 view 的 marginTop+content 的 paddingTop 时，表示在最顶部，返回 true，可以下拉</br>
         * 如果没有 margin，则当 top==content 的 paddinTop 时，表示在最顶部，返回 true，可以下拉
         */
        View child = viewGroup.getChildAt(0);
        ViewGroup.LayoutParams glp = child.getLayoutParams();
        int top = child.getTop();
        if (glp instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) glp;
            return top == mlp.topMargin + viewGroup.getPaddingTop();
        } else {
            return top == viewGroup.getPaddingTop();
        }
    }
}
