package com.jie.calculator.calculator.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jie.calculator.calculator.R;

/**
 * Created on 2019/2/14.
 *
 * @author Jie.Wu
 */
public class CustomTabView extends FrameLayout {

    private ImageView ivTabTop, ivTabBottom, ivIcon, ivTabBg;
    private TextView tvTitle;
    private View llTab;
    private int minHeight;
    private float bottomOffset = -1, topOffset = 0;
    private boolean isAnimation;

    private boolean isSelected;
    private boolean isTabAnimationShow;


    public CustomTabView(@NonNull Context context) {
        this(context, null);
    }

    public CustomTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MeasureSpec.makeMeasureSpec(minHeight, heightMeasureSpec);
        if (bottomOffset < 0) {
            bottomOffset = getMeasuredHeight();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v.getVisibility() != View.GONE) {
                if (v.getId() == R.id.iv_bottom) {
                    layoutBottom(v, l, t, r, b);
                } else if (v.getId() == R.id.iv_top) {
                    layoutTop(v, l, t, r, b);
                }
            }
        }
    }

    private void layoutBottom(View v, int l, int t, int r, int b) {
        layoutSpecialView(v, l, t, r, b, bottomOffset);
    }

    private void layoutTop(View v, int l, int t, int r, int b) {
        layoutSpecialView(v, l, t, r, b, topOffset);
    }

    private void layoutSpecialView(View v, int l, int t, int r, int b, float offset) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();

        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();
        final LayoutParams lp = (LayoutParams) v.getLayoutParams();
        final int width = v.getMeasuredWidth();
        final int height = v.getMeasuredHeight();

        l = parentLeft + (parentRight - parentLeft - width) / 2 +
                lp.leftMargin - lp.rightMargin;
        t = parentTop + (parentBottom - parentTop - height) / 2 +
                lp.topMargin - lp.bottomMargin;
        t += offset;
        int childWidth = v.getMeasuredWidth();
        int childHeight = v.getMeasuredHeight();

        v.layout(l, t, l + childWidth, t + childHeight);
    }


    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, this, false);
        ivIcon = view.findViewById(R.id.iv_icon);
        tvTitle = view.findViewById(R.id.iv_title);
        llTab = view.findViewById(R.id.ll_tab);
        ivTabTop = view.findViewById(R.id.iv_top);
        ivTabBottom = view.findViewById(R.id.iv_bottom);
        ivTabBg = view.findViewById(R.id.iv_tab_bg);

        addXmlView(llTab);
        addXmlView(ivTabBg);
        addXmlView(ivTabTop);
        addXmlView(ivTabBottom);

        int[] textSizeAttr = new int[]{android.R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        minHeight = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();

    }

    @Override
    public boolean isAttachedToWindow() {
        return super.isAttachedToWindow();
    }

    private void addXmlView(View view) {
        ViewGroup parent;
        if ((parent = (ViewGroup) view.getParent()) != null) {
            parent.removeView(view);
        }
        addView(view);
    }

    public void setTabTextColor(@ColorRes int color) {
        if (tvTitle != null) {
            tvTitle.setTextColor(ContextCompat.getColor(getContext(), color));
        }
    }

    public void setTabText(@StringRes int title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    public void setIcon(@DrawableRes int iconDrawable) {
        if (ivIcon != null) {
            ivIcon.setImageResource(iconDrawable);
        }
    }

    public void setTabDrawable(@DrawableRes int tabTopDrawable, @DrawableRes int tabBottomDrawable) {
        ivTabTop.setImageResource(tabTopDrawable);
        ivTabBottom.setImageResource(tabBottomDrawable);
    }

    public void setTabBg(@DrawableRes int res) {
        ivTabBg.setImageResource(res);
    }

    public void updateTab(boolean isSelected) {
        this.isSelected = isSelected;
        llTab.setVisibility(isSelected ? GONE : VISIBLE);
        ivTabTop.setVisibility(isSelected ? VISIBLE : GONE);
        ivTabBg.setVisibility(isSelected ? VISIBLE : GONE);
        ivTabBottom.setVisibility(isSelected ? VISIBLE : GONE);
        tvTitle.setSelected(isSelected);
        ivIcon.setSelected(isSelected);
    }

    public void animator(boolean show, int duration) {
        if (isAnimation || !isSelected) {
            return;
        }
        this.isTabAnimationShow = show;
        ValueAnimator bottomAnimation = buildBottomAnimation(show, duration);
        ValueAnimator topAnimation = buildTopAnimation(show, duration);
        AnimatorSet set = new AnimatorSet();
        set.play(bottomAnimation);
        set.playTogether(bottomAnimation, topAnimation);
        set.setInterpolator(new FastOutLinearInInterpolator());
        set.start();
    }

    private ValueAnimator buildBottomAnimation(boolean show, int duration) {
        float start = bottomOffset;
        int end = show ? 0 : getMeasuredHeight();
        ValueAnimator animator = ObjectAnimator.ofFloat(start, end);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            bottomOffset = (float) animation.getAnimatedValue();
            requestLayout();
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animator;
    }

    private ValueAnimator buildTopAnimation(boolean show, long duration) {
        float start = topOffset;
        int end = show ? -getMeasuredHeight() : 0;
        ValueAnimator animator = ObjectAnimator.ofFloat(start, end);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            topOffset = (float) animation.getAnimatedValue();
            requestLayout();
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animator;
    }

    public boolean isTabAnimationShow() {
        return isTabAnimationShow;
    }
}
