package com.jal.studio.ad.platform.impl.baidu;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.baidu.mobad.feeds.BaiduNative;
import com.baidu.mobad.feeds.NativeErrorCode;
import com.baidu.mobad.feeds.NativeResponse;
import com.baidu.mobad.feeds.RequestParameters;
import com.baidu.mobads.BaiduHybridAdManager;
import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.jal.studio.ad.platform.AD;
import com.jal.studio.ad.platform.ADListener;
import com.jal.studio.ad.platform.HybridManager;
import com.jal.studio.ad.platform.Platform;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created on 20/09/2017.
 *
 * @author leveme
 */

public class BaiduPlatform implements Platform {

    private final String id;

    public BaiduPlatform(String id) {
        this.id = id;
    }

    @Override
    public Object getSplashAd(Context context, ViewGroup container, String pid, ADListener listener) {
        if (id != null) {
            BaiduNative.setAppSid(context, id);
        }
        return new SplashAd(context, container, new SplashAdListenerWrapper(listener), pid);
    }

    private static class SplashAdListenerWrapper implements SplashAdListener {

        private ADListener listener;

        public SplashAdListenerWrapper(ADListener listener) {
            this.listener = listener;
        }

        @Override
        public void onAdPresent() {
            if (listener != null) {
                listener.onADReady();
                listener.onADPresent();
            }
        }

        @Override
        public void onAdDismissed() {
            if (listener != null) {
                listener.onADDismissed();
            }
        }

        @Override
        public void onAdFailed(String error) {
            if (listener != null) {
                listener.onADError(error);
            }
        }

        @Override
        public void onAdClick() {
            if (listener != null) {
                listener.onADClicked();
            }
        }
    }

    @Override
    public Observable<AD> getNativeAd(Context context, String pid) {
        return getNativeAd(context, pid, DOWNLOAD_APP_CONFIRM_MOBILE);
    }

    @Override
    public Observable<AD> getNativeAd(final Context context, final String pid, final int confirmPolicy) {
        if (id != null) {
            BaiduNative.setAppSid(context, id);
        }
        return Observable.create(new ObservableOnSubscribe<NativeResponse>() {
            @Override
            public void subscribe(final ObservableEmitter<NativeResponse> subscriber) {
                RequestParameters parameters = new RequestParameters.Builder()
                        .downloadAppConfirmPolicy(mapConfirmPolicy(confirmPolicy))
                        .build();

                new BaiduNative(context, pid, new BaiduNative.BaiduNativeNetworkListener() {
                    @Override
                    public void onNativeLoad(List<NativeResponse> responses) {
                        if (responses != null) {
                            for (NativeResponse response : responses) {
                                subscriber.onNext(response);
                            }
                        }
                        subscriber.onComplete();
                    }

                    @Override
                    public void onNativeFail(NativeErrorCode error) {
                        subscriber.onError(new IOException("baidu: " + error));
                    }
                }).makeRequest(parameters);
            }
        })
                .observeOn(Schedulers.computation())
                .filter(new Predicate<NativeResponse>() {
                    @Override
                    public boolean test(NativeResponse response) {
                        return response != null;
                    }
                })
                .map(new Function<NativeResponse, AD>() {
                    @Override
                    public AD apply(NativeResponse response) throws Exception {
                        return new BaiduADWrapper(response);
                    }
                });
    }

    private static int mapConfirmPolicy(int policy) {
        switch (policy) {
            case DOWNLOAD_APP_CONFIRM_NEVER:
                return RequestParameters.DOWNLOAD_APP_CONFIRM_NEVER;
            case DOWNLOAD_APP_CONFIRM_ALWAYS:
                return RequestParameters.DOWNLOAD_APP_CONFIRM_ALWAYS;
            default:
                return RequestParameters.DOWNLOAD_APP_CONFIRM_ONLY_MOBILE;
        }
    }

    @Override
    public HybridManager getHybridAd(WebView view) {
        BaiduHybridAdManager manager = new BaiduHybridAdManager();
        manager.injectJavaScriptBridge(view);
        return new BaiduWebViewClient(manager);
    }

    private static class BaiduWebViewClient implements HybridManager {

        private BaiduHybridAdManager manager;

        public BaiduWebViewClient(BaiduHybridAdManager manager) {
            this.manager = manager;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (manager != null) {
                manager.onPageStarted(view, url, favicon);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (manager != null) {
                manager.onReceivedError(view, errorCode, description, failingUrl);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (manager != null) {
                return manager.shouldOverrideUrlLoading(view, url);
            }
            return false;
        }
    }
}
