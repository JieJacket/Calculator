package com.jie.calculator.calculator.model;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.R;

public class LocationRightLabel implements IModel {

    private String label;
    private int viewHeight;
    private boolean isSelected;
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
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
        tv.setSelected(isSelected);

        holder.addOnClickListener(R.id.tv_label);
    }

    @Override
    public int getItemType() {
        return Type.Location_LABEL.value;
    }
}
