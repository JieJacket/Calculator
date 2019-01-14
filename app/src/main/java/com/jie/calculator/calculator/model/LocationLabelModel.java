package com.jie.calculator.calculator.model;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.R;

public class LocationLabelModel implements IModel {
    public static final int LABEL = 125;

    private String label;
    private int viewHeight;


    public void setLabel(String label) {
        this.label = label;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        TextView tv = holder.getView(R.id.tv_label);
        ViewGroup.LayoutParams params = tv.getLayoutParams();
        params.height = viewHeight;
        tv.setLayoutParams(params);
        tv.setText(label);
        tv.setGravity(Gravity.CENTER);
    }

    @Override
    public int getItemType() {
        return LABEL;
    }
}
