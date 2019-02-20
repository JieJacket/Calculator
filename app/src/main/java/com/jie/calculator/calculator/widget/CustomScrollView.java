package com.jie.calculator.calculator.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created on 2019/2/15.
 *
 * @author Jie.Wu
 */
public class CustomScrollView extends NestedScrollView {

    public interface OnScrollChangerListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    private OnScrollChangerListener onScrollChangerListener;

    public void addOnScrollChangerListener(OnScrollChangerListener onScrollChangerListener) {
        this.onScrollChangerListener = onScrollChangerListener;
    }

    public CustomScrollView(@NonNull Context context) {
        super(context);
    }

    public CustomScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangerListener != null) {
            onScrollChangerListener.onScrollChanged(l, t, oldl, oldt);
        }
    }
}
