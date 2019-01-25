package com.jal.studio.ad.platform.impl.toutiao;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.jal.studio.ad.platform.AD;

import java.util.List;

public class TtFullScreenAdWrapper implements AD {

    private TTFullScreenVideoAd raw;

    TtFullScreenAdWrapper(TTFullScreenVideoAd raw) {
        this.raw = raw;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public Object getRaw() {
        return raw;
    }

    @Override
    public boolean isAvailable(Context context) {
        return false;
    }

    @Override
    public boolean isApp() {
        return false;
    }

    @Override
    public String getBrand() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public String getIconUrl() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public List<String> getImageUrls() {
        return null;
    }

    @Override
    public void onExposed(View view) {
        //实际无需调用
        if (raw != null) {
            raw.setFullScreenVideoAdInteractionListener(new TtFullScreenAdWrapper.AdInteractionListenerWrapper());
            raw.setDownloadListener(new TtFullScreenAdWrapper.AdDownloadListener());
            raw.setShowDownLoadBar(true);
        }
    }

    @Override
    public void onClicked(View view) {

    }

    public static class AdInteractionListenerWrapper implements TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {

        @Override
        public void onAdShow() {
            Log.e("chao", "ad show");
        }

        @Override
        public void onAdVideoBarClick() {
            Log.e("chao", "onAdVideoBarClick");

        }

        @Override
        public void onAdClose() {
            Log.e("chao", "onAdClose");

        }

        @Override
        public void onVideoComplete() {
            Log.e("chao", "onVideoComplete");

        }

        @Override
        public void onSkippedVideo() {
            Log.e("chao", "onSkippedVideo");

        }

    }

    private static boolean mHasShowDownloadActive = false;
    public static class AdDownloadListener implements TTAppDownloadListener {
        @Override
        public void onIdle() {
            mHasShowDownloadActive = false;
        }

        @Override
        public void onDownloadActive(long l, long l1, String s, String s1) {
            if (!mHasShowDownloadActive) {
                mHasShowDownloadActive = true;
            }
        }

        @Override
        public void onDownloadPaused(long l, long l1, String s, String s1) {}

        @Override
        public void onDownloadFailed(long l, long l1, String s, String s1) {}

        @Override
        public void onDownloadFinished(long l, String s, String s1) {}

        @Override
        public void onInstalled(String s, String s1) {}
    }
}
