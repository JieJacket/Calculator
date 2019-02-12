package com.jie.calculator.calculator.model;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.R;

/**
 * Created on 2019/2/11.
 *
 * @author Jie.Wu
 */
public class MineInfoItem implements IModel {

    public static final int TYPE = 10;

    @Override
    public void convert(BaseViewHolder holder) {
        holder.addOnClickListener(R.id.tv_info);
    }

    @Override
    public int getItemType() {
        return TYPE;
    }
}
