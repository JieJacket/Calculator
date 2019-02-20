package com.jie.calculator.calculator.model.tbk;

import android.view.View;
import android.view.ViewGroup;
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
public class TBKFavoriteLinearItem extends TBKFavoriteItem {

    public static final int TYPE = 0x113;

    public TBKFavoriteLinearItem(TBKFavoritesItemResp itemResp) {
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


    private void resize(int size, View ivPict) {
        ViewGroup.LayoutParams params = ivPict.getLayoutParams();
        params.width = size;
        params.height = size;
    }

    private void resize(int w, int h, View ivPict) {
        ViewGroup.LayoutParams params = ivPict.getLayoutParams();
        params.width = w;
        params.height = h;
    }

    @Override
    public int getItemType() {
        return TYPE;
    }

}
