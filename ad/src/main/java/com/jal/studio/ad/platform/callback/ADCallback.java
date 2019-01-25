package com.jal.studio.ad.platform.callback;

/**
 * Created on 2019/1/7.
 *
 * @author Jie.Wu
 */
public interface ADCallback {

    void onADReady();

    void onADPresent();

    void onADClicked();

    void onADDismissed();

    void onADError(String msg);

}
