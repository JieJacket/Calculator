package com.jie.calculator.calculator.model;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created on 2019/1/4.
 *
 * @author Jie.Wu
 */
public interface IModel extends MultiItemEntity {
    enum Type {
        Normal(0);
        int value;

        Type(int value) {
            this.value = value;
        }

        public int value(){
            return value;
        }
    }

    void convert(BaseViewHolder holder);
}
