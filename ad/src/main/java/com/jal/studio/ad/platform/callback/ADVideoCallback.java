package com.jal.studio.ad.platform.callback;

/**
 * Created on 2019/1/7.
 *
 * @author Jie.Wu
 */
public interface ADVideoCallback {
    void onAdShow();

    void onAdVideoBarClick();

    void onAdClose();

    void onVideoComplete();

    void onVideoError();

    void onVideoSkip();

}
