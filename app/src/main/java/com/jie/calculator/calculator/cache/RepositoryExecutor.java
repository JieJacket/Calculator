package com.jie.calculator.calculator.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jie.calculator.calculator.util.EmptyObserver;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2019/1/31.
 *
 * @author Jie.Wu
 */
public class RepositoryExecutor<T> {

    private final SharedPreferences preferences;
    private Set<T> data = Collections.synchronizedSet(new LinkedHashSet<>());
    private String key;
    private boolean hasChanged;
    private int maxSize = 20;

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public RepositoryExecutor(@NonNull String key, @NonNull Context context) {
        this.key = key;
        preferences = context.getSharedPreferences("ds-search-history", Context.MODE_PRIVATE);
        getDataAsync().subscribe(new EmptyObserver<>());
    }

    private Observable<Set<T>> getDataAsync() {
        return Observable.just(preferences.getString(key, ""))
                .filter(json -> !TextUtils.isEmpty(json))
                .map(json -> {
                    Type type = new TypeToken<Set<String>>() {
                    }.getType();
                    data = new Gson().fromJson(json, type);
                    if (data == null) {
                        data = new LinkedHashSet<>();
                    }
                    return data;
                })
                .subscribeOn(Schedulers.io());
    }

    private Observable<Set<T>> getTempData() {
        return Observable.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onNext(new LinkedHashSet<>(data));
                emitter.onComplete();
            }
        });
    }

    public void put(T v) {
        if (v != null) {
            hasChanged = true;
            while (data.size() >= maxSize) {
                ArrayList<T> temp = new ArrayList<>(data);
                temp.remove(0);
                data.clear();
                data.addAll(temp);
            }
            data.add(v);
            autoAsyncSave();
        }
    }

    public void remove(T t) {
        if (t != null && data.contains(t)) {
            hasChanged = true;
            data.remove(t);
            autoAsyncSave();
        }
    }

    public Observable<List<T>> getData() {
        return getTempData()
                .filter(d -> !d.isEmpty())
                .take(1)
                .map(ArrayList::new);
    }

    private void autoAsyncSave() {
        Observable.just(hasChanged)
                .filter(status -> status)
                .map(status -> new LinkedHashSet<>(data))
                .doOnNext(list -> {
                    String json = new Gson().toJson(list);
                    preferences.edit().putString(key, json).apply();
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void update(Collection<T> list) {
        if (list != null && list.isEmpty()) {
            hasChanged = true;
            data.clear();
            data.addAll(list);
            autoAsyncSave();
        }
    }


}
