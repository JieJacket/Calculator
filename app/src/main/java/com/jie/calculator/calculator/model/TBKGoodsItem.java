package com.jie.calculator.calculator.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesItemResp;
import com.jie.calculator.calculator.R;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class TBKGoodsItem implements IModel {

    public static final int TYPE = 0x112;

    private TBKFavoritesItemResp itemResp;

    public TBKGoodsItem(TBKFavoritesItemResp itemResp) {
        this.itemResp = itemResp;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.addOnClickListener(R.id.cv_container);
        holder.setText(R.id.tv_title, itemResp.getTitle());
        holder.setText(R.id.tv_volume, String.valueOf(itemResp.getVolume()));
        TextView finalPrice = holder.getView(R.id.tv_final_price);
        TextView reservePrice = holder.getView(R.id.tv_reserve_price);
        finalPrice.setText(itemResp.getZk_final_price());
        reservePrice.setText(itemResp.getReserve_price());
        ImageView ivPict = holder.getView(R.id.iv_pict);

        Glide.with(ivPict.getContext())
                .load(itemResp.getPict_url())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ViewGroup.LayoutParams params = ivPict.getLayoutParams();
                        int size = holder.getView(R.id.cv_container).getWidth();
                        params.width = size;
                        params.height = size;
                        ivPict.setLayoutParams(params);
                        return false;
                    }
                })
                .into(ivPict);
    }

    @Override
    public int getItemType() {
        return TYPE;
    }

    public TBKFavoritesItemResp getItemResp() {
        return itemResp;
    }
}
