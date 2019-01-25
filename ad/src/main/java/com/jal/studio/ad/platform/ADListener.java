package com.jal.studio.ad.platform;

/**
 * Created on 18/12/2017.
 *
 * @author han.xiang
 */

public interface ADListener {
    void onADReady();

    void onADPresent();

    void onADClicked();

    void onADDismissed();

    void onADError(String error);
}
