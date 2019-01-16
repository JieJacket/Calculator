package com.jie.calculator.calculator.cache;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.InsuranceBean;
import com.jie.calculator.calculator.model.LocationModelv2;
import com.jie.calculator.calculator.model.TaxPoint;
import com.jie.calculator.calculator.model.TaxStandard;
import com.jie.calculator.calculator.network.HttpUtils;
import com.jie.calculator.calculator.util.CommonConstants;
import com.jie.calculator.calculator.util.EmptyWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
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

    public Observable<List<LocationModelv2>> getCityList(Context context) {
        return cacheProviders.getCityList(getCities(context), new DynamicKey("CityList"), new EvictDynamicKey(false));
    }

    public Observable<List<LocationModelv2>> getCities(Context context) {
        return Observable.just(new EmptyWrapper<>(context))
                .filter(EmptyWrapper::isNonNull)
                .map(EmptyWrapper::getValue)
                .map(ctx -> {
                    InputStream is = ctx.getAssets().open("cities.json");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuffer sb = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                })
                .filter(s -> !TextUtils.isEmpty(s))
                .subscribeOn(Schedulers.io())
                .flatMap(json -> Observable.create((ObservableOnSubscribe<List<LocationModelv2>>) emitter -> {
                    if (!emitter.isDisposed()){
                        Type type = new TypeToken<List<LocationModelv2>>() {
                        }.getType();
                        Gson gson = new GsonBuilder()
                                .setLenient()
                                .create();
                        List<LocationModelv2> o = gson.fromJson(json, type);
                        emitter.onNext(o);
                        emitter.onComplete();
                    }else {
                        emitter.onError(new Throwable("Disposed"));
                    }
                }))
                .onErrorReturn(t -> {
                    t.printStackTrace();
                    return new ArrayList<>();
                })
                ;
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