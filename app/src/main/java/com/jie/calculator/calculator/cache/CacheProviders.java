package com.jie.calculator.calculator.cache;

import com.jie.calculator.calculator.model.TaxPoint;
import com.jie.calculator.calculator.model.TaxStandard;

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

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<List<TaxStandard>> getStandards(Observable<List<TaxStandard>> standards, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<TaxPoint> getTaxPoints(Observable<TaxPoint> standards, DynamicKey userName, EvictDynamicKey evictDynamicKey);
}
