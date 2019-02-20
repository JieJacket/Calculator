package com.jie.calculator.calculator.model.tbk;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesItemResp;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.provider.GlideApp;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class TBKGoodsLinearItem extends TBKGoodsItem {

    public static final int TYPE = 0x115;

    public TBKGoodsLinearItem(TBKFavoritesItemResp itemResp) {
        super(itemResp);
    }

    @Override
    protected void limitImageSize(ImageView ivPict) {

    }


    @Override
    protected void resizePictSize(ImageView ivPict) {
        GlideApp.with(ivPict.getContext())
                .load(getItemResp().getPict_url())
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(ivPict);
    }

    @Override
    public int getItemType() {
        return TYPE;
    }

}
