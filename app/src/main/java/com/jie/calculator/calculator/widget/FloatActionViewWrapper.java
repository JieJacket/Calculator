package com.jie.calculator.calculator.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Interpolator;

import com.jie.calculator.calculator.util.SystemUtil;

/**
 * Created on 2019/2/12.
 *
 * @author Jie.Wu
 */
public class FloatActionViewWrapper<T extends View> extends RecyclerView.OnScrollListener implements NestedScrollView.OnScrollChangeListener {

    private int limitSize = 0;
    private static final int SHOW_HIDE_ANIM_DURATION = 200;
    private static final Interpolator FAST_OUT_LINEAR_IN_INTERPOLATOR = new FastOutLinearInInterpolator();
    private static final Interpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();
    private boolean isAnimated;

    private T wrapperView;

    private int mOffset;

    public FloatActionViewWrapper(@NonNull View t) {
        setDependency(t);
    }

    private void setDependency(View t) {
        if (t instanceof RecyclerView) {
            ((RecyclerView) t).addOnScrollListener(this);
        } else if (t instanceof NestedScrollView) {
            ((NestedScrollView) t).setOnScrollChangeListener(this);
        }
    }

    public void setActionView(@NonNull T t){
        this.wrapperView = t;
    }

    public void setLimitRange(int limitSize) {
        this.limitSize = limitSize;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (isOverLimit(recyclerView, dy)) {
            show(wrapperView);
        } else {
            hide(wrapperView);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY >= limitSize) {
            show(wrapperView);
        } else {
            hide(wrapperView);
        }
    }

    private boolean isOverLimit(View view, int dy) {
        if (limitSize == 0) {
            limitSize = (int) (SystemUtil.getScreenHeight(wrapperView.getContext()) * 2.0f / 3);
        }
        int offset = 0;
        if (view instanceof RecyclerView) {
            if (canScrollVertically((RecyclerView) view)) {
                mOffset += dy;
                offset = mOffset;
            }
        } else if (view instanceof NestedScrollView) {
            offset = dy;
        }

        return offset > dy;
    }

    private boolean canScrollVertically(RecyclerView view) {
        final int offset = view.computeVerticalScrollOffset();
        final int range = view.computeVerticalScrollRange() - view.computeVerticalScrollExtent();


        return range != 0 && (offset > 0 || offset < range - 1);
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

    private void hide(View view) {
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
