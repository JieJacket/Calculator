package com.jal.studio.ad.platform.impl.gdt;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.jal.studio.ad.platform.AD;
import com.jal.studio.ad.platform.ADListener;
import com.jal.studio.ad.platform.HybridManager;
import com.jal.studio.ad.platform.Platform;
import com.qq.e.ads.cfg.BrowserType;
import com.qq.e.ads.cfg.DownAPPConfirmPolicy;
import com.qq.e.ads.nativ.NativeMediaAD;
import com.qq.e.ads.nativ.NativeMediaADData;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

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

public class GdtPlatform implements Platform {

    private final String id;

    public GdtPlatform(String id) {
        this.id = id;
    }

    @Override
    public Object getSplashAd(Context context, ViewGroup container, String pid, ADListener listener) {
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        View skipView = container.findViewWithTag("skip");
        return new SplashAD(activity, container, skipView, id, pid, new SplashADListenerWrapper(listener), 0);
    }

    private static class SplashADListenerWrapper implements SplashADListener {

        private ADListener listener;

        public SplashADListenerWrapper(ADListener listener) {
            this.listener = listener;
        }

        @Override
        public void onADDismissed() {
            if (listener != null) {
                listener.onADDismissed();
            }
        }

        @Override
        public void onNoAD(AdError error) {
            if (listener != null) {
                listener.onADError(error.getErrorCode() + ": " + error.getErrorMsg());
            }
        }

        @Override
        public void onADPresent() {
            if (listener != null) {
                listener.onADReady();
                listener.onADPresent();
            }
        }

        @Override
        public void onADClicked() {
            if (listener != null) {
                listener.onADClicked();
            }
        }

        @Override
        public void onADTick(long millisUntilFinished) {

        }

        @Override
        public void onADExposure() {

        }
    }

    @Override
    public Observable<AD> getNativeAd(Context context, String pid) {
        return getNativeAd(context, pid, DOWNLOAD_APP_CONFIRM_MOBILE);
    }

    @Override
    public Observable<AD> getNativeAd(final Context context, final String pid, final int confirmPolicy) {
        return Observable.create(new ObservableOnSubscribe<NativeMediaADData>() {
            @Override
            public void subscribe(final ObservableEmitter<NativeMediaADData> subscriber) throws Exception {
                NativeMediaAD nativeAD = new NativeMediaAD(context, id, pid, new NativeMediaAD.NativeMediaADListener() {
                    @Override
                    public void onADLoaded(List<NativeMediaADData> items) {
                        if (items != null) {
                            for (NativeMediaADData item : items) {
                                subscriber.onNext(item);
                            }
                        }
                        subscriber.onComplete();
                    }

                    @Override
                    public void onNoAD(AdError error) {
                        String msg = error == null
                                ? "UNKNOWN"
                                : (error.getErrorCode() + " " + error.getErrorMsg());
                        subscriber.onError(new IOException("gdt: " + msg));
                    }

                    @Override
                    public void onADStatusChanged(NativeMediaADData nativeMediaADData) {

                    }

                    @Override
                    public void onADError(NativeMediaADData nativeMediaADData, AdError adError) {

                    }

                    @Override
                    public void onADVideoLoaded(NativeMediaADData nativeMediaADData) {

                    }

                    @Override
                    public void onADExposure(NativeMediaADData nativeMediaADData) {

                    }

                    @Override
                    public void onADClicked(NativeMediaADData nativeMediaADData) {

                    }
                });
                nativeAD.setBrowserType(BrowserType.Default);
                nativeAD.setDownAPPConfirmPolicy(mapConfirmPolicy(confirmPolicy));
                nativeAD.loadAD(10);
            }

        })
                .observeOn(Schedulers.computation())
                .filter(new Predicate<NativeMediaADData>() {
                    @Override
                    public boolean test(NativeMediaADData item) {
                        return item != null;
                    }
                })
                .map(new Function<NativeMediaADData, AD>() {
                    @Override
                    public AD apply(NativeMediaADData item) {
                        return new GdtADWrapper(item);
                    }
                });
    }

    private static DownAPPConfirmPolicy mapConfirmPolicy(int policy) {
        switch (policy) {
            case DOWNLOAD_APP_CONFIRM_NEVER:
                return DownAPPConfirmPolicy.NOConfirm;
            default:
                return DownAPPConfirmPolicy.Default;
        }
    }

    @Override
    public HybridManager getHybridAd(WebView view) {
        return null;
    }
}
