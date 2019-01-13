package com.jie.calculator.calculator.cache;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.jie.calculator.calculator.model.TaxPoint;
import com.jie.calculator.calculator.model.TaxStandard;
import com.jie.calculator.calculator.network.HttpUtils;
import com.jie.calculator.calculator.util.CommonConstants;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

public class Repository {
    public static final String USERS_PER_PAGE = "standards";
    private final CacheProviders cacheProviders;

    public static Repository init(File cacheDir) {
        return new Repository(cacheDir);
    }

    public Repository(File cacheDir) {
        cacheProviders = new RxCache.Builder()
                .persistence(cacheDir, new GsonSpeaker())
                .using(CacheProviders.class);
    }

    public Observable<List<TaxStandard>> getStandard(boolean update) {
        return cacheProviders.getStandards(HttpUtils.getInst().getList(CommonConstants.URL4, TaxStandard.class, new TypeToken<List<TaxStandard>>() {
        }.getType()), new DynamicKey("standards"), new EvictDynamicKey(update));
    }

    public Observable<TaxPoint> getTaxPoint(boolean update) {
        return cacheProviders.getTaxPoints(HttpUtils.getInst().get(CommonConstants.URL3, TaxPoint.class),
                new DynamicKey("TaxPoint"), new EvictDynamicKey(update));
    }

    public Observable<TaxPoint.IFBean> getTaxPoint(String position, boolean update) {
        return getTaxPoint(update)
                .flatMap(tp -> Observable.fromIterable(tp.getIF()))
                .filter(p -> TextUtils.equals(p.getS(), position))
                .takeUntil(p -> p != null);
    }
}