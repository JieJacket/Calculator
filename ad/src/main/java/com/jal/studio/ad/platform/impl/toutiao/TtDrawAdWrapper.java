package com.jal.studio.ad.platform.impl.toutiao;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.jal.studio.ad.platform.AD;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载Draw竖版视频信息流广告
 */
public class TtDrawAdWrapper implements AD {

    private TTDrawFeedAd raw;

    TtDrawAdWrapper(TTDrawFeedAd raw) {
        this.raw = raw;

    }

    @Override
    public int getType() {
        if (raw != null) {
            int mode = raw.getImageMode();
            if (mode == TTAdConstant.IMAGE_MODE_VIDEO
                    || mode == TTAdConstant.IMAGE_MODE_VIDEO_VERTICAL) {
                return TYPE_VIDEO;
            }
        }

        return TYPE_IMAGE;
    }

    @Override
    public Object getRaw() {
        return raw;
    }

    @Override
    public boolean isAvailable(Context context) {
        return raw != null;
    }

    @Override
    public boolean isApp() {
        return raw != null && raw.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD;
    }

    @Override
    public String getBrand() {
        if (raw != null) {
            return raw.getSource();
        }
        return null;
    }

    @Override
    public String getTitle() {
        if (raw != null) {
            return raw.getTitle();
        }
        return null;
    }

    @Override
    public String getDesc() {
        if (raw != null) {
            return raw.getDescription();
        }
        return null;
    }

    @Override
    public String getIconUrl() {
        if (raw != null) {
            TTImage icon = raw.getIcon();
            if (icon != null) {
                return icon.getImageUrl();
            }
        }
        return null;
    }

    @Override
    public String getImageUrl() {
        if (raw != null) {
            List<TTImage> images = raw.getImageList();
            if (images != null && images.size() > 0) {
                TTImage image = images.get(0);
                if (image != null) {
                    return image.getImageUrl();
                }
            }
        }
        return null;
    }


    @Override
    public List<String> getImageUrls() {
        if (raw != null) {
            List<TTImage> images = raw.getImageList();
            if (images != null) {
                List<String> urls = new ArrayList<>();
                for (TTImage image : images) {
                    if (image != null) {
                        urls.add(image.getImageUrl());
                    }
                }
                return urls;
            }
        }
        return null;
    }

    @Override
    public void onExposed(View view) {
        if (raw != null) {
            ViewGroup container = (ViewGroup) (view instanceof ViewGroup ? view : view.getParent());
            View clickView = (!view.isClickable() && container.isClickable()) ? container : view;
            List<View> clickViews = new ArrayList<>();
            clickViews.add(view);
            //为广告设置activity对象，下载相关行为需要该context对象
            raw.setActivityForDownloadApp(getActivityFromView(view));
            //设置广告视频区域是否响应点击行为，控制视频暂停、继续播放，默认不响应；
            raw.setCanInterruptVideoPlay(true);
            //设置视频暂停的Icon和大小
            //item.setPauseIcon(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.dislike_icon), 60);
            /**
             * 初始化并绑定广告行为
             * 响应点击区域的设置，分为普通的区域clickViews和创意区域creativeViews
             * clickViews中的view被点击会尝试打开广告落地页；creativeViews中的view被点击会根据广告类型
             * 响应对应行为，如下载类广告直接下载，打开落地页类广告直接打开落地页。
             * 注意：ad.getAdView()获取的view请勿放入这两个区域中。
             * ========↑↑↑上面是SDK说明↑↑↑=========↓↓↓下面是实际使用的大概效果↓↓↓============
             * 目前测试如果两个参数都是写同一个view（clickViews，creativeViews）
             * 这两个参数的表现是：
             *  1.（clickViews，null），基本都会额外打开一个落地页
             *  2.（null，creativeViews），根据广告类型一部分点击直接下载，但是有部分可能点击没反应
             *  3.（clickViews，creativeViews），绝大部分情况下直接下载
             *  ps：这里最好使用1和3两种。
             */
            raw.registerViewForInteraction(container, clickViews, null, new TtDrawAdWrapper.AdInteractionListenerWrapper(clickView));
        }
    }

    @Override
    public void onClicked(View view) {

    }

    private static class AdInteractionListenerWrapper implements TTNativeAd.AdInteractionListener {

        private View view;
        private View.OnClickListener listener;

        AdInteractionListenerWrapper(View view) {
            this.view = view;
            this.listener = ViewUtils.getOnClickListener(view);
        }

        @Override
        public void onAdShow(TTNativeAd ad) {

        }

        @Override
        public void onAdClicked(View view, TTNativeAd ad) {
            if (listener != null) {
                listener.onClick(this.view);
            }
        }

        @Override
        public void onAdCreativeClick(View view, TTNativeAd ad) {

        }
    }

    public static Activity getActivityFromView(View view) {
        if (null != view) {
            Context context = view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }
}
