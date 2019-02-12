package com.jie.calculator.calculator.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jie.calculator.calculator.R;

/**
 * Created on 2019/2/12.
 *
 * @author Jie.Wu
 */
public class MateialDesignLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.load_more_view;
    }

    @Override
    protected int getLoadingViewId() {
        return 0;
    }

    @Override
    protected int getLoadFailViewId() {
        return 0;
    }

    @Override
    protected int getLoadEndViewId() {
        return 0;
    }
}
