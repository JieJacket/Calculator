package com.jal.studio.ad.platform;

import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 * Created on 22/09/2017.
 *
 * @author leveme
 */

public interface HybridManager {
    void onPageStarted(WebView view, String url, Bitmap favicon);
    void onReceivedError(WebView view, int errorCode, String description, String failingUrl);
    boolean shouldOverrideUrlLoading(WebView view, String url);
}
