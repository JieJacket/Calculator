package com.jie.calculator.calculator.model;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.R;

import java.text.NumberFormat;

/**
 * Created on 2019/1/14.
 *
 * @author Jie.Wu
 */
public class Insurance extends InsuranceBean {

    private double salary;
    private String max, min;

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setMin(String min) {
        this.min = min;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        TextView tvLabel = holder.getView(R.id.tv_insurance_label);
        TextView tvPercent = holder.getView(R.id.tv_insurance_percent);
        tvLabel.setText(getTitle());
        tvPercent.setText(NumberFormat.getInstance().format(calculate(salary, max, min)));

    }

    @Override
    public int getItemType() {
        return Type.Insurance.value;
    }
}
