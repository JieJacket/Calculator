package com.jie.calculator.calculator.model.tbk;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jal.calculator.store.ds.model.tbk.TBKMaterialsResp;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.provider.GlideApp;
import com.jie.calculator.calculator.util.SystemUtil;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class TBKMaterialLinearItem extends TBKMaterialItem {

    public static final int TYPE = 0x113;

    public TBKMaterialLinearItem(TBKMaterialsResp itemResp) {
        super(itemResp);
    }

    @Override
    public void convert(BaseViewHolder holder) {
        super.convert(holder);
        holder.setText(R.id.tv_coupon_label, R.string.str_coupon_label);
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
                .transforms(new RoundedCorners(SystemUtil.dp2px(ivPict.getContext(), 8)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(ivPict);
    }

    @Override
    public int getItemType() {
        return TYPE;
    }

}
