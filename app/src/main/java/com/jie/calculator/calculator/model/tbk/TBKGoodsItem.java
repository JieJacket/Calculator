package com.jie.calculator.calculator.model.tbk;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesItemResp;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.provider.GlideApp;

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

    private static int size = -1;

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

        limitImageSize(ivPict);

        GlideApp.with(ivPict.getContext())
                .load(itemResp.getPict_url())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(ivPict);
    }

    private void limitImageSize(ImageView ivPict) {
        if (size > 0) {
            resize(size, ivPict);
            return;
        }
        ivPict.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ivPict.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                size = ivPict.getWidth();
                resize(size, ivPict);
            }
        });
    }

    private void resize(int size, View ivPict) {
        ViewGroup.LayoutParams params = ivPict.getLayoutParams();
        params.width = size;
        params.height = size;
    }

    @Override
    public int getItemType() {
        return TYPE;
    }

    public TBKFavoritesItemResp getItemResp() {
        return itemResp;
    }
}
