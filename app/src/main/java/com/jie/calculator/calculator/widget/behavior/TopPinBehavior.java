package com.jie.calculator.calculator.widget.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ScrollingView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 2019/2/20.
 *
 * @author Jie.Wu
 */
public class TopPinBehavior extends CoordinatorLayout.Behavior<View> {

    public TopPinBehavior() {
    }

    public TopPinBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof ScrollingView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        int y = dependency.getScrollY();
        ViewGroup.LayoutParams params = child.getLayoutParams();
        return true;
    }


}
