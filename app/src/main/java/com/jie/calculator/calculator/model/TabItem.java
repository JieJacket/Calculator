package com.jie.calculator.calculator.model;

import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.R;

/**
 * Created on 2019/2/18.
 *
 * @author Jie.Wu
 */
public class TabItem implements IModel {
    public static final int TYPE = 0x1234;

    private DSMainTab tab;

    public TabItem(DSMainTab tab) {
        this.tab = tab;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.addOnClickListener(R.id.ll_tab);
        holder.setText(R.id.tv_title, tab.getTabTitle());
    }

    @Override
    public int getItemType() {
        return TYPE;
    }

    public DSMainTab getTab() {
        return tab;
    }
}
