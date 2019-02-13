package com.jie.calculator.calculator.cache;

import com.jal.calculator.store.ds.model.tbk.TBKBaseResp;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesItemResp;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesResp;
import com.jal.calculator.store.ds.model.tbk.TBKMaterialsResp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;

/**
 * Created on 2019/1/13.
 *
 * @author Jie.Wu
 */
public interface CacheProviders {

    @LifeCache(duration = 6, timeUnit = TimeUnit.HOURS)
    Observable<List<TBKFavoritesResp>> getTBKFavoritesCategory(Observable<List<TBKFavoritesResp>> standards, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 3, timeUnit = TimeUnit.HOURS)
    Observable<List<TBKFavoritesItemResp>> getTBKFavoritesItem(Observable<List<TBKFavoritesItemResp>> standards, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 1, timeUnit = TimeUnit.HOURS)
    Observable<List<TBKMaterialsResp>> getMaterialInfo(Observable<List<TBKMaterialsResp>> standards, DynamicKey userName, EvictDynamicKey evictDynamicKey);


}
