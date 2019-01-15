package com.jie.calculator.calculator.model;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.R;

public class LocationModel implements IModel {

    private int type;
    public static final int HEADER = 0x123;
    public static final int LABEL = 0x124;

    private String label;
    private String mark;
    private TaxStandard standard;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TaxStandard getStandard() {
        return standard;
    }

    public void setStandard(TaxStandard standard) {
        this.standard = standard;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.setText(R.id.tv_location, label);
        holder.addOnClickListener(R.id.tv_location);
    }

    @Override
    public int getItemType() {
        return type;
    }
}
