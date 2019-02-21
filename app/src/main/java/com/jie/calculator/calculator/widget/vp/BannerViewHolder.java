package com.jie.calculator.calculator.widget.vp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.model.BannerItem;
import com.jie.calculator.calculator.provider.GlideApp;
import com.jie.calculator.calculator.util.SystemUtil;
import com.zhouwei.mzbanner.holder.MZViewHolder;

/**
 * Created on 2019/2/21.
 *
 * @author Jie.Wu
 */
public class BannerViewHolder implements MZViewHolder<BannerItem> {

    private ImageView ivBanner;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item_layout, null);
        ivBanner = view.findViewById(R.id.iv_banner);
        return view;
    }

    @Override
    public void onBind(Context context, int position, BannerItem data) {
        GlideApp.with(context)
                .load(data.getUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(new RoundedCorners(SystemUtil.dp2px(context, 10)))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(ivBanner);
    }
}
