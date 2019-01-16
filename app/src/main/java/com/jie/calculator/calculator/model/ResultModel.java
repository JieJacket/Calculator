package com.jie.calculator.calculator.model;

import android.support.annotation.StringRes;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.R;

/**
 * Created on 2019/1/16.
 *
 * @author Jie.Wu
 */
public class ResultModel implements IModel {

    private String value;
    @StringRes
    private int resId;

    public static IModel create(@StringRes int key, String value) {
        ResultModel model = new ResultModel();
        model.resId = key;
        model.value = value;
        return model;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.setText(R.id.tv_result_key, resId);
        holder.setText(R.id.tv_result_value, value);
    }

    @Override
    public int getItemType() {
        return Type.Result.value;
    }
}
