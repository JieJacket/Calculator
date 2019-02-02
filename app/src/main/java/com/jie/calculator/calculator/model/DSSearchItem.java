package com.jie.calculator.calculator.model;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.R;

/**
 * Created on 2019/1/31.
 *
 * @author Jie.Wu
 */
public class DSSearchItem implements IModel {

    public static final int TYPE = 0x987;

    private String data;

    public DSSearchItem(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.addOnClickListener(R.id.tv_item);
        holder.addOnLongClickListener(R.id.tv_item);
        holder.setText(R.id.tv_item,data);
    }

    @Override
    public int getItemType() {
        return TYPE;
    }
}
