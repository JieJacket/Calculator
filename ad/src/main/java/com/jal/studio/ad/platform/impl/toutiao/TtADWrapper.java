package com.jal.studio.ad.platform.impl.toutiao;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.jal.studio.ad.platform.AD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 19/09/2017.
 *
 * @author leveme
 */

class TtADWrapper implements AD {

    private TTFeedAd raw;

    TtADWrapper(TTFeedAd raw) {
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
            raw.registerViewForInteraction(container, clickView, new AdInteractionListenerWrapper(clickView));
        }
    }

    @Override
    public void onClicked(View view) {
    }

    private static class AdInteractionListenerWrapper implements TTFeedAd.AdInteractionListener {

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
}
