package com.jal.studio.ad.platform;

import android.content.Context;
import android.view.ViewGroup;
import android.webkit.WebView;

import io.reactivex.Observable;


/**
 * Created on 19/09/2017.
 *
 * @author leveme
 */

public interface Platform {
    int DOWNLOAD_APP_CONFIRM_NEVER = 0;
    int DOWNLOAD_APP_CONFIRM_MOBILE = 1;
    int DOWNLOAD_APP_CONFIRM_ALWAYS = 2;

    Object getSplashAd(Context context, ViewGroup container, String pid, ADListener listener);

    Observable<AD> getNativeAd(Context context, String placeId);

    Observable<AD> getNativeAd(Context context, String placeId, int confirmPolicy);

    HybridManager getHybridAd(WebView view);
}
