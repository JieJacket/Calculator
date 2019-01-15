package com.jie.calculator.calculator.model;

import android.support.annotation.StringRes;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.R;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created on 2019/1/4.
 *
 * @author Jie.Wu
 */
public class TaxItem implements IModel, Serializable {
    @StringRes
    private int titleRes;
    private double percent;

    public static TaxItem create(@StringRes int titleRex, double percent) {
        TaxItem taxItem = new TaxItem();
        taxItem.percent = percent;
        taxItem.titleRes = titleRex;
        return taxItem;
    }

    public int getTitle() {
        return titleRes;
    }

    public void setTitle(@StringRes int titleRex) {
        this.titleRes = titleRex;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        TextView tvLabel = holder.getView(R.id.tv_insurance_label);
        TextView tvPercent = holder.getView(R.id.tv_insurance_percent);
        tvLabel.setText(titleRes);
        tvPercent.setText(String.format(Locale.getDefault(), "%.1f%%", percent));

    }

    @Override
    public int getItemType() {
        return Type.Normal.value;
    }


    public double calculate(double salary) {
        return salary * percent;
    }
}
