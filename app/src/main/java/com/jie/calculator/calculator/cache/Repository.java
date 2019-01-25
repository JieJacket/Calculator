package com.jie.calculator.calculator.cache;

import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.reflect.TypeToken;
import com.jal.calculator.store.ds.model.ali.TBKFavoriteItemRequest;
import com.jal.calculator.store.ds.model.ali.TBKFavoriteListRequest;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesItemResp;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesResp;
import com.jal.calculator.store.ds.network.AliServerManager;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.InsuranceBean;
import com.jie.calculator.calculator.model.TaxPoint;
import com.jie.calculator.calculator.model.TaxStandard;
import com.jie.calculator.calculator.network.HttpUtils;
import com.jie.calculator.calculator.util.CommonConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
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

    public Observable<List<TaxStandard>> getStandards(boolean update) {
        return cacheProviders.getStandards(HttpUtils.getInst().getList(CommonConstants.URL4, TaxStandard.class, new TypeToken<List<TaxStandard>>() {
        }.getType()), new DynamicKey("standards"), new EvictDynamicKey(update));
    }

    public Observable<TaxStandard> getStandard(String city, boolean update) {
        return getStandards(update)
                .flatMap(Observable::fromIterable)
                .filter(taxStandard -> TextUtils.equals(taxStandard.getName(), city))
                .takeUntil(taxStandard -> taxStandard != null);
    }

    public Observable<TaxPoint> getTaxPoint(boolean update) {
        return cacheProviders.getTaxPoints(HttpUtils.getInst().get(CommonConstants.URL3, TaxPoint.class),
                new DynamicKey("TaxPoint"), new EvictDynamicKey(update));
    }

    public Observable<Pair<String, List<IModel>>> getTaxPoint(String position, boolean update) {
        return getTaxPoint(update)
                .flatMap(tp -> Observable.fromIterable(tp.getIF()))
                .filter(p -> TextUtils.equals(p.getS(), position))
                .takeUntil(p -> p != null)
                .compose(convert());
    }

    public Observable<List<TBKFavoritesResp>> getTBKFavoritesCategory(boolean update, TBKFavoriteListRequest request) {
        return cacheProviders.getTBKFavoritesCategory(AliServerManager.getInst().getServer()
                        .getTBKFavoritesList(request.signRequest())
                        .flatMap(resp -> {
                            if (resp != null && resp.getResults() != null) {
                                return Observable.just(resp.getResults());
                            }
                            return Observable.empty();
                        }),
                new DynamicKey("TBKFavoritesCategory"), new EvictDynamicKey(update));
    }

    public Observable<List<TBKFavoritesItemResp>> getTBKFavoritesItem(boolean update, long favoritesId, TBKFavoriteItemRequest request) {
        return cacheProviders.getTBKFavoritesItem(AliServerManager.getInst().getServer()
                        .getTBKFavoritesItem(request.signRequest())
                        .flatMap(resp -> {
                            if (resp != null && resp.getResults() != null) {
                                return Observable.just(resp.getResults());
                            }
                            return Observable.empty();
                        }),
                new DynamicKey("TBKFavoritesItem:" + favoritesId), new EvictDynamicKey(update));
    }


    public ObservableTransformer<TaxPoint.IFBean, Pair<String, List<IModel>>> convert() {
        return upstream -> upstream.map(bean -> {
            List<IModel> models = new ArrayList<>();
            String city = bean.getS();
            models.add(InsuranceBean.create(InsuranceBean.AccumulationFund, city, bean.getF()));
            models.add(InsuranceBean.create(InsuranceBean.Medical, city, bean.getM()));
            models.add(InsuranceBean.create(InsuranceBean.RetirementFund, city, bean.getE()));
            models.add(InsuranceBean.create(InsuranceBean.Unemployment, city, bean.getU()));
            models.add(InsuranceBean.create(InsuranceBean.Injury, city, bean.getI()));
            models.add(InsuranceBean.create(InsuranceBean.Birth, city, bean.getB()));
            return Pair.create(bean.getS(), models);
        });
    }

}