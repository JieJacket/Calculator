package com.jie.calculator.calculator.model.tbk;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jal.calculator.store.ds.model.tbk.TBKSearchResp;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.provider.GlideApp;

/**
 * Created on 2019/1/31.
 *
 * @author Jie.Wu
 */
public class TBKSearchItem implements IModel {

    public static final int TYPE = 0x124;

    private TBKSearchResp searchResp;

    public TBKSearchItem(TBKSearchResp searchResp) {
        this.searchResp = searchResp;
    }

    public TBKSearchResp getSearchResp() {
        return searchResp;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        if (searchResp == null) {
            return;
        }
        holder.addOnClickListener(R.id.rl_container);
        Context context = holder.itemView.getContext();
        holder.setText(R.id.tv_title, searchResp.getShort_title());

        String volume = searchResp.getVolume() > 0 ?
                context.getString(R.string.str_monthly_volume, searchResp.getVolume()) : "";
        holder.setText(R.id.tv_volume, volume);

        holder.setText(R.id.tv_shop_location, searchResp.getProvcity());
        holder.setText(R.id.tv_shop_title, searchResp.getShop_title());
        TextView tvReservePrice = holder.getView(R.id.tv_before_price);
        tvReservePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvReservePrice.setText(searchResp.getReserve_price());
        holder.setText(R.id.tv_after_price, searchResp.getZk_final_price());

        GlideApp.with(context)
                .load(searchResp.getPict_url())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into((ImageView) holder.getView(R.id.iv_pict));

    }

    @Override
    public int getItemType() {
        return TYPE;
    }
}
