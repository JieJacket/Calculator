package com.jie.calculator.calculator.model;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.util.AppController;

/**
 * Created on 2019/2/18.
 *
 * @author Jie.Wu
 */
public class TabMoreItem extends TabItem {
    public static final int TYPE = 0x1235;
    private int listMode;

    public TabMoreItem(int listMode) {
        super(new DSMainTab());
        this.listMode = listMode;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.addOnClickListener(R.id.ll_tab);
        holder.setText(R.id.tv_title, listMode == AppController.MODE_LINEAR ? R.string.str_grid_mode : R.string.str_linear_mode);
    }

    @Override
    public int getItemType() {
        return TYPE;
    }
}
