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
public class RepositoryExecutor {

    private final SharedPreferences preferences;
    private Set<String> data = Collections.synchronizedSet(new LinkedHashSet<>());
    private String key;
    private boolean hasChanged;
    private int maxSize = 20;

    public RepositoryExecutor(@NonNull String key, @NonNull Context context) {
        this.key = key;
        preferences = context.getSharedPreferences("ds-search-history", Context.MODE_PRIVATE);
        getDataAsync().subscribe(new EmptyObserver<>());
    }

    private Observable<Set<String>> getDataAsync() {
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

    private Observable<Set<String>> getTempData() {
        return Observable.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onNext(new LinkedHashSet<>(data));
                emitter.onComplete();
            }
        });
    }

    public void put(String v) {
        if (v != null) {
            hasChanged = true;
            while (data.size() >= maxSize) {
                ArrayList<String> temp = new ArrayList<>(data);
                temp.remove(0);
                data.clear();
                data.addAll(temp);
            }
            data.add(v);
        }
    }

    public void remove(String t) {
        if (t != null && data.contains(t)) {
            hasChanged = true;
            data.remove(t);
        }
    }

    public Observable<List<String>> getData() {
        return getTempData()
                .filter(d -> !d.isEmpty())
                .take(1)
                .map(ArrayList::new);
    }

    public void save() {
        Observable.just(hasChanged)
                .filter(status -> status)
                .map(status -> data)
                .doOnNext(list -> {
                    String json = new Gson().toJson(list);
                    preferences.edit().putString(key, json).apply();
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }


}
