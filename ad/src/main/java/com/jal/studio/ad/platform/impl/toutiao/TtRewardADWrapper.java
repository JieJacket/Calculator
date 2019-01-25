package com.jal.studio.ad.platform.impl.toutiao;

import android.content.Context;
import android.view.View;

import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.jal.studio.ad.platform.AD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 19/09/2017.
 *
 * @author leveme
 */

public class TtRewardADWrapper implements AD {

    private TTRewardVideoAd raw;

    TtRewardADWrapper(TTRewardVideoAd raw) {
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
        return raw != null;
    }

    @Override
    public boolean isApp() {
        return false;
    }

    @Override
    public String getBrand() {
        return "";
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public String getDesc() {
        return "";
    }

    @Override
    public String getIconUrl() {
        return "";
    }

    @Override
    public String getImageUrl() {
        return "";
    }

    @Override
    public List<String> getImageUrls() {
        return new ArrayList<String>();
    }

    @Override
    public void onExposed(View view) {
        //实际无需调用
        if (raw != null) {
            raw.setRewardAdInteractionListener(new AdInteractionListenerWrapper());
            raw.setDownloadListener(new  AdDownloadListener());
            raw.setShowDownLoadBar(true);
        }
    }

    @Override
    public void onClicked(View view) {}


    private static class AdInteractionListenerWrapper implements TTRewardVideoAd.RewardAdInteractionListener {

        @Override
        public void onAdShow() {}

        @Override
        public void onAdVideoBarClick() {}

        @Override
        public void onAdClose() {}

        @Override
        public void onVideoComplete() {}

        @Override
        public void onRewardVerify(boolean b, int i, String s) {}

        @Override
        public void onVideoError(){}

    }

    private static boolean mHasShowDownloadActive = false;
    private static class AdDownloadListener implements TTAppDownloadListener {
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
