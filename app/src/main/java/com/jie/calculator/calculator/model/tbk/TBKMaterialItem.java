package com.jie.calculator.calculator.model.tbk;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesItemResp;
import com.jal.calculator.store.ds.model.tbk.TBKMaterialsResp;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.provider.GlideApp;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class TBKMaterialItem implements IModel {

    public static final int TYPE = 0x112;

    private TBKMaterialsResp itemResp;

    public TBKMaterialItem(TBKMaterialsResp itemResp) {
        this.itemResp = itemResp;
    }

    private static int width = -1;

    @Override
    public void convert(BaseViewHolder holder) {
        holder.addOnClickListener(R.id.cv_container);
        holder.setText(R.id.tv_title, itemResp.getTitle());
        holder.setText(R.id.tv_volume, String.valueOf(itemResp.getVolume()));
        holder.setText(R.id.tv_coupon, String.valueOf(itemResp.getCoupon_amount()));
        TextView finalPrice = holder.getView(R.id.tv_final_price);
        finalPrice.setText(itemResp.getZk_final_price());
        ImageView ivPict = holder.getView(R.id.iv_pict);

        limitImageSize(ivPict);

        GlideApp.with(ivPict.getContext())
                .load(itemResp.getPict_url())
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (width > 0 ){
                            int w = resource.getIntrinsicWidth();
                            int h = resource.getIntrinsicHeight();
                            int height = (int) (h * width * 1.0f / w);
                            resize(width, height, ivPict);
                        }
                        return false;
                    }
                })
                .into(ivPict);
    }

    private void limitImageSize(ImageView ivPict) {
        if (width > 0) {
            resize(width, ivPict);
            return;
        }
        ivPict.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ivPict.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                width = ivPict.getWidth();
                resize(width, ivPict);
            }
        });
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

    public TBKMaterialsResp getItemResp() {
        return itemResp;
    }
}
