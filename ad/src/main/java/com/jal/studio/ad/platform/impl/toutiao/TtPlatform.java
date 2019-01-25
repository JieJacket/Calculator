package com.jal.studio.ad.platform.impl.toutiao;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdManagerFactory;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.TTSplashAd;
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


public class TtPlatform implements Platform {

    private final String id;
    private String TAG = "PlatformAds.TtPlatform";

    public TtPlatform(String id) {
        this.id = id;
    }

    private TTAdManager ttAdManager;

    private TTAdManager getTtAdManager(Context context) {
        if (ttAdManager == null) {
            ttAdManager = TTAdManagerFactory.getInstance(context);
            ttAdManager.setAppId(id);
            String name;
            try {
                PackageManager pm = context.getPackageManager();
                name = pm.getApplicationLabel(pm.getApplicationInfo(context.getPackageName(), 0)).toString();
            } catch (Exception e) {
                name = "";
            }
            TTAdSdk.init(context, buildConfig(context, name));
            ttAdManager = TTAdSdk.getAdManager();

        }

        return ttAdManager;
    }

    private TTAdConfig buildConfig(Context context, String name) {
        return new TTAdConfig.Builder()
                .appId(id)
                .useTextureView(false) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .appName(name)
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .globalDownloadListener(new AppDownloadStatusListener(context)) //下载任务的全局监听
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G) //允许直接下载的网络状态集合
                .supportMultiProcess(false) //是否支持多进程，true支持
                .build();
    }

    @Override
    public Object getSplashAd(Context context, ViewGroup container, String pid, final ADListener listener) {
        AdSlot slot = new AdSlot.Builder()
                .setCodeId(pid)
                .setImageAcceptedSize(720, 1080)
                .build();

        View skipView = container.findViewWithTag("skip");
        boolean needCustomSkipView = (skipView == null) ? false : true;
        TTAdNative ttAdNative = getTtAdManager(context).createAdNative(context);
        ttAdNative.loadSplashAd(slot, new SplashADListenerWrapper(container, listener, needCustomSkipView));
        return ttAdNative;
    }

    private static class SplashADListenerWrapper implements TTAdNative.SplashAdListener, TTSplashAd.AdInteractionListener {

        private ViewGroup container;
        private ADListener listener;
        private boolean needCustomSkipView;

        SplashADListenerWrapper(ViewGroup container, ADListener listener, boolean needCustomSkipView) {
            this.container = container;
            this.listener = listener;
            this.needCustomSkipView = needCustomSkipView;
        }

        @Override
        public void onSplashAdLoad(TTSplashAd ad) {
            if (ad == null) {
                if (listener != null) {
                    listener.onADError("NO AD");
                }
            } else {
                if (listener != null) {
                    listener.onADReady();
                }
                View view = ad.getSplashView();
                container.addView(view);
                ad.setSplashInteractionListener(this);
                if (needCustomSkipView) {
                    ad.setNotAllowSdkCountdown();
                }
            }
        }

        @Override
        public void onError(int code, String message) {
            if (listener != null) {
                listener.onADError(code + ": " + message);
            }
        }

        @Override
        public void onTimeout() {
            if (listener != null) {
                listener.onADError("TIMEOUT");
            }
        }

        @Override
        public void onAdShow(View view, int i) {
            if (listener != null) {
                listener.onADPresent();
            }
        }

        @Override
        public void onAdClicked(View view, int i) {
            if (listener != null) {
                listener.onADClicked();
            }
        }

        @Override
        public void onAdSkip() {
            if (listener != null) {
                listener.onADDismissed();
            }
        }

        @Override
        public void onAdTimeOver() {
            if (listener != null) {
                listener.onADDismissed();
            }
        }
    }

    @Override
    public Observable<AD> getNativeAd(Context context, String pid) {
        return getNativeAd(context, pid, DOWNLOAD_APP_CONFIRM_MOBILE);
    }

    @Override
    public Observable<AD> getNativeAd(final Context context, final String pid, int confirmPolicy) {
        return Observable.create(new ObservableOnSubscribe<TTFeedAd>() {
            @Override
            public void subscribe(final ObservableEmitter<TTFeedAd> subscriber) {
                AdSlot slot = new AdSlot.Builder()
                        .setCodeId(pid)
                        .setImageAcceptedSize(640, 360)
                        .setAdCount(3)
                        .build();
                TTAdNative ttAdNative = getTtAdManager(context).createAdNative(context);
                ttAdNative.loadFeedAd(slot, new TTAdNative.FeedAdListener() {
                    @Override
                    public void onFeedAdLoad(List<TTFeedAd> items) {
                        if (items != null) {
                            for (TTFeedAd item : items) {
                                subscriber.onNext(item);
                            }
                        }
                        subscriber.onComplete();
                    }

                    @Override
                    public void onError(int code, String message) {
                        subscriber.onError(new IOException(code + ": " + message));
                    }
                });
            }

        })
                .observeOn(Schedulers.computation())
                .filter(new Predicate<TTFeedAd>() {
                    @Override
                    public boolean test(TTFeedAd item) {
                        return item != null;
                    }
                })
                .map(new Function<TTFeedAd, AD>() {
                    @Override
                    public AD apply(TTFeedAd item) {
                        return new TtADWrapper(item);
                    }
                });
    }


    public Observable<AD> getRewardAd(final Context context, final String pid, final String mockUserid) {
        return Observable.create(new ObservableOnSubscribe<TTRewardVideoAd>() {
            @Override
            public void subscribe(final ObservableEmitter<TTRewardVideoAd> subscriber) {
                AdSlot adSlot = new AdSlot.Builder()
                        .setCodeId(pid)
                        .setSupportDeepLink(true)
                        .setAdCount(3)
                        .setImageAcceptedSize(1080, 1920)
                        .setRewardName("金币")
                        .setRewardAmount(60)
                        .setUserID(mockUserid)
                        .setMediaExtra("media_extra")
                        .setOrientation(TTAdConstant.VERTICAL)
                        .build();

                TTAdManager ttAdManager = getTtAdManager(context);
                ttAdManager.setTitleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                        .setGlobalAppDownloadListener(new AppDownloadStatusListener(context))
                        .setDirectDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI)
                        .requestPermissionIfNecessary(context);
                TTAdNative ttAdNative = ttAdManager.createAdNative(context);
                ttAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
                    @Override
                    public void onError(int code, String message) {
                        subscriber.onError(new IOException(code + ": " + message));
                    }

                    @Override
                    public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                        if (ttRewardVideoAd != null) {
                            Log.i(TAG, "获取了视频广告");
                            subscriber.onNext(ttRewardVideoAd);
                        }
                        subscriber.onComplete();
                    }

                    @Override
                    public void onRewardVideoCached() {

                    }
                });
            }
        })
                .observeOn(Schedulers.computation())
                .filter(new Predicate<TTRewardVideoAd>() {
                    @Override
                    public boolean test(TTRewardVideoAd item) {
                        return item != null;
                    }
                })
                .map(new Function<TTRewardVideoAd, AD>() {
                    @Override
                    public AD apply(TTRewardVideoAd item) {
                        return new TtRewardADWrapper(item);
                    }
                });
    }


    public Observable<AD> getFullScreenAd(final Context context, final String pid, final int orientation) {
        return Observable.create(new ObservableOnSubscribe<TTFullScreenVideoAd>() {
            @Override
            public void subscribe(final ObservableEmitter<TTFullScreenVideoAd> subscriber) throws Exception {
                AdSlot adSlot = new AdSlot.Builder()
                        .setCodeId(pid)
                        .setSupportDeepLink(true)
                        .setImageAcceptedSize(1080, 1920)
                        .setOrientation(orientation)
                        .build();

                TTAdManager ttAdManager = getTtAdManager(context);
                ttAdManager.setTitleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                        .setGlobalAppDownloadListener(new AppDownloadStatusListener(context))
                        .setDirectDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI)
                        .requestPermissionIfNecessary(context);
                TTAdNative ttAdNative = ttAdManager.createAdNative(context);
                ttAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
                    @Override
                    public void onError(int code, String message) {
                        Log.e("chao", "error");
                        subscriber.onError(new IOException(code + ": " + message));
                    }

                    @Override
                    public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
                        if (ttFullScreenVideoAd != null) {
                            Log.i("chao", "获取了视频广告");
                            subscriber.onNext(ttFullScreenVideoAd);
                        }
                        subscriber.onComplete();
                    }

                    @Override
                    public void onFullScreenVideoCached() {
                        Log.e("chao", "video cached");
                    }
                });
            }
        })
                .observeOn(Schedulers.computation())
                .filter(new Predicate<TTFullScreenVideoAd>() {
                    @Override
                    public boolean test(TTFullScreenVideoAd item) {
                        return item != null;
                    }
                })
                .map(new Function<TTFullScreenVideoAd, AD>() {
                    @Override
                    public AD apply(TTFullScreenVideoAd item) {
                        return new TtFullScreenAdWrapper(item);
                    }
                });
    }

    public Observable<AD> getDrawAd(final Context context, final String pid) {
        return Observable.create(new ObservableOnSubscribe<TTDrawFeedAd>() {
            @Override
            public void subscribe(final ObservableEmitter<TTDrawFeedAd> subscriber) {
                AdSlot slot = new AdSlot.Builder()
                        .setCodeId(pid)
                        .setSupportDeepLink(true)
                        .setImageAcceptedSize(1080, 1920)
                        .setAdCount(3)
                        .build();
                TTAdNative ttAdNative = getTtAdManager(context).createAdNative(context);
                ttAdNative.loadDrawFeedAd(slot, new TTAdNative.DrawFeedAdListener() {
                    @Override
                    public void onDrawFeedAdLoad(List<TTDrawFeedAd> items) {
                        if (items != null) {
                            for (TTDrawFeedAd item : items) {
                                subscriber.onNext(item);
                            }
                        }
                        subscriber.onComplete();
                    }

                    @Override
                    public void onError(int code, String message) {
                        subscriber.onError(new IOException(code + ": " + message));
                    }
                });
            }
        })
                .observeOn(Schedulers.computation())
                .filter(new Predicate<TTDrawFeedAd>() {
                    @Override
                    public boolean test(TTDrawFeedAd item) {
                        return item != null;
                    }
                })
                .map(new Function<TTDrawFeedAd, AD>() {
                    @Override
                    public AD apply(TTDrawFeedAd item) {
                        return new TtDrawAdWrapper(item);
                    }
                });
    }

    @Override
    public HybridManager getHybridAd(WebView view) {
        return null;
    }
}
