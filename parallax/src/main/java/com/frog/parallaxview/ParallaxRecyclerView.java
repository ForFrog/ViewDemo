package com.frog.parallaxview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

/**
 * 阻尼效果的控件
 * Created by Frog on 2017/7/10.
 */

public class ParallaxRecyclerView extends RecyclerView {

    private int mTouchSlop;
    private boolean isRestoring;

    private int mActivePointerId;
    private boolean isBeingDragged;
    private float mInitialMotionY;
    private float mDistance;
    private float mScale;

    public ParallaxRecyclerView(Context context) {
        super(context);
    }

    public ParallaxRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //获取触发移动事件的最小距离
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public ParallaxRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (isRestoring && action == MotionEvent.ACTION_DOWN) {
            isRestoring = false;
        }

        if (!isEnabled() || isRestoring || (!isScrollToTop() && !isScrollToBottom())) {
            return super.onInterceptTouchEvent(event);
        }


        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mActivePointerId = event.getPointerId(0);
                isBeingDragged = false;
                float initialMotionY = getMotionEventY(event);
                if (initialMotionY == -1) {
                    return super.onInterceptTouchEvent(event);
                }
                mInitialMotionY = initialMotionY;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mActivePointerId == MotionEvent.INVALID_POINTER_ID) {
                    return super.onInterceptTouchEvent(event);
                }
                final float y = getMotionEventY(event);
                if (y == -1f) {
                    return super.onInterceptTouchEvent(event);
                }

                //滑动到顶部并且继续往下拉,就拦截event
                if (isScrollToTop() && !isScrollToBottom()) {
                    // 移动距离
                    float yDiff = y - mInitialMotionY;

                    if (yDiff > mTouchSlop && !isBeingDragged) {
                        isBeingDragged = true;
                    }

                } else if (!isScrollToTop() && isScrollToBottom()) {
                    // 在底部不在顶部
                    float yDiff = mInitialMotionY - y;
                    if (yDiff > mTouchSlop && !isBeingDragged) {
                        isBeingDragged = true;
                    }
                } else if (isScrollToTop() && isScrollToBottom()) {
                    // 在底部也在顶部
                    float yDiff = y - mInitialMotionY;
                    if (Math.abs(yDiff) > mTouchSlop && !isBeingDragged) {
                        isBeingDragged = true;
                    }
                } else {
                    // 不在底部也不在顶部
                    return super.onInterceptTouchEvent(event);
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                isBeingDragged = false;
                break;
        }

        return isBeingDragged || super.onInterceptTouchEvent(event);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = event.getPointerId(0);
                isBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE: {

                float y = getMotionEventY(event);
                if (isScrollToTop() && !isScrollToBottom()) {
                    // 在顶部不在底部
                    mDistance = y - mInitialMotionY;
                    if (mDistance < 0) {
                        return super.onTouchEvent(event);
                    }
                    mScale = calculateRate(mDistance);
                    pull(mScale);
                    return true;
                } else if (!isScrollToTop() && isScrollToBottom()) {
                    // 在底部不在顶部
                    mDistance = mInitialMotionY - y;
                    if (mDistance < 0) {
                        return super.onTouchEvent(event);
                    }
                    mScale = calculateRate(mDistance);
                    push(mScale);
                    return true;
                } else if (isScrollToTop() && isScrollToBottom()) {
                    // 在底部也在顶部
                    mDistance = y - mInitialMotionY;
                    if (mDistance > 0) {
                        mScale = calculateRate(mDistance);
                        pull(mScale);
                    } else {
                        mScale = calculateRate(-mDistance);
                        push(mScale);
                    }
                    return true;
                } else {
                    // 不在底部也不在顶部
                    return super.onTouchEvent(event);
                }
            }
            case MotionEventCompat.ACTION_POINTER_DOWN:
                mActivePointerId = event.getPointerId(MotionEventCompat.getActionIndex(event));
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (isScrollToTop() && !isScrollToBottom()) {
                    animateRestore(true);
                } else if (!isScrollToTop() && isScrollToBottom()) {
                    animateRestore(false);
                } else if (isScrollToTop() && isScrollToBottom()) {
                    if (mDistance > 0) {
                        animateRestore(true);
                    } else {
                        animateRestore(false);
                    }
                } else {
                    Log.d("ParallaxDragListener", "isTop:" + isScrollToTop() + ", isBottom:" + isScrollToBottom());
                    return super.onTouchEvent(event);
                }
                break;
            }
        }


        return super.onTouchEvent(event);
    }

    private boolean isScrollToTop() {
        return !this.canScrollVertically(-1);
    }

    private boolean isScrollToBottom() {
        return !this.canScrollVertically(1);
    }

    private float getMotionEventY(MotionEvent event) {
        int index = event.findPointerIndex(mActivePointerId);
        return index < 0 ? -1f : event.getY(index);
    }

    private void onSecondaryPointerUp(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = event.getPointerId(newPointerIndex);
        }
    }

    /**
     * 计算缩放比例
     */
    private float calculateRate(float distance) {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float originalDragPercent = distance / screenHeight;
        float dragPercent = Math.min(1f, originalDragPercent);
        float rate = 2f * dragPercent - (float) Math.pow(dragPercent, 2f);
        return 1 + rate / 5f;
    }

    private void pull(float scale) {
        this.setPivotY(0);
        this.setScaleY(scale);
    }

    private void push(float scale) {
        this.setPivotY(this.getHeight());
        this.setScaleY(scale);
    }

    private void animateRestore(final boolean isPullRestore) {
        ValueAnimator animator = ValueAnimator.ofFloat(mScale, 1f);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator(2f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (isPullRestore) {
                    pull(value);
                } else {
                    push(value);
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isRestoring = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRestoring = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

}
