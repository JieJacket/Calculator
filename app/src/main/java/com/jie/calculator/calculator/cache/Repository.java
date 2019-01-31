package com.jie.calculator.calculator.cache;

import com.jal.calculator.store.ds.model.ali.TBKFavoriteItemRequest;
import com.jal.calculator.store.ds.model.ali.TBKFavoriteListRequest;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesItemResp;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesResp;
import com.jal.calculator.store.ds.network.AliServerManager;

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


}