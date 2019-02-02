package com.jie.calculator.calculator.adapter;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.calculator.calculator.model.IModel;

import java.util.List;

/**
 * Created on 2019/1/4.
 *
 * @author Jie.Wu
 */
public abstract class CommonRecyclerViewAdapter extends BaseMultiItemQuickAdapter<IModel, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CommonRecyclerViewAdapter(List<IModel> data) {
        super(data);
        bindItems();
    }

    private void bindItems() {
        List<Pair<Integer, Integer>> pairs = bindItemTypes();
        for (Pair<Integer, Integer> pair : pairs) {
            addItemType(pair.first, pair.second);
        }
    }

    public void update(List<? extends IModel> data, boolean needClear) {
        if (mData != data) {
            if (needClear) {
                mData.clear();
            }
            mData.addAll(data);
        }

        notifyDataSetChanged();
    }

    public void update(List<? extends IModel> data) {
        this.update(data, true);
    }

    @Override
    protected void convert(BaseViewHolder helper, IModel item) {
        item.convert(helper);
    }

    @NonNull
    protected abstract List<Pair<Integer, Integer>> bindItemTypes();

}
