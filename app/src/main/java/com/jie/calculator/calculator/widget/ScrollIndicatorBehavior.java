package com.jie.calculator.calculator.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ScrollView;

import com.jie.calculator.calculator.util.SystemUtil;

/**
 * Created on 2019/2/12.
 *
 * @author Jie.Wu
 */
public class ScrollIndicatorBehavior extends CoordinatorLayout.Behavior<View> {

    private int limitSize = 0;
    private static final int SHOW_HIDE_ANIM_DURATION = 200;
    private static final Interpolator FAST_OUT_LINEAR_IN_INTERPOLATOR = new FastOutLinearInInterpolator();
    private static final Interpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();
    private boolean isAnimated;


    public ScrollIndicatorBehavior() {
    }

    public ScrollIndicatorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        if (isOverLimit(dy, target)) {
            show(child);
        } else {
            hide(child);
        }
    }

    private boolean isOverLimit(int dy, View target) {
//        int offset = rv.computeVerticalScrollOffset();
        if (limitSize == 0) {
            limitSize = SystemUtil.getScreenHeight(target.getContext());
        }
        int offset = 0;
        if (target instanceof ScrollingView) {
            ScrollingView scrollingView = (ScrollingView) target;
            offset = scrollingView.computeVerticalScrollOffset();
        } else if (target instanceof ScrollView) {
            offset = target.getScrollY();
        }

        return offset >= limitSize;
    }

    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }

    private void show(View view) {
        if (view.getVisibility() == View.VISIBLE || isAnimated) {
            return;
        }
        if (view.getVisibility() != View.VISIBLE) {
            // If the view isn't visible currently, we'll animate it from a single pixel
            view.setAlpha(0f);
            view.setScaleY(0f);
            view.setScaleX(0f);
        }

        view.animate().cancel();
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(SHOW_HIDE_ANIM_DURATION)
                .setInterpolator(LINEAR_OUT_SLOW_IN_INTERPOLATOR)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                        isAnimated = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimated = false;
                    }
                });
    }

    public void hide(View view) {
        if (view.getVisibility() != View.VISIBLE || isAnimated) {
            return;
        }
        view.animate().cancel();
        view.animate()
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(SHOW_HIDE_ANIM_DURATION)
                .setInterpolator(FAST_OUT_LINEAR_IN_INTERPOLATOR)
                .setListener(new AnimatorListenerAdapter() {
                    private boolean mCancelled;

                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                        mCancelled = false;
                        isAnimated = true;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mCancelled = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!mCancelled) {
                            view.setVisibility(View.INVISIBLE);
                        }
                        isAnimated = false;
                    }
                });
    }


}
