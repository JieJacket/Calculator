package com.jie.calculator.calculator.cache;

import android.content.Context;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created on 2019/2/11.
 *
 * 记录用户的搜索内容
 *
 * @author Jie.Wu
 */
public class HistorySearchManager {

    private RepositoryExecutor<String> executor;

    private HistorySearchManager() {
    }

    private static final class LazyHolder {
        private static final HistorySearchManager instance = new HistorySearchManager();
    }

    public static HistorySearchManager getInst() {
        return LazyHolder.instance;
    }

    public void init(Context context) {
        executor = new RepositoryExecutor<>("history-item", context);
    }

    public void put(String value) {
        executor.put(value);
    }

    public void remove(String value) {
        executor.remove(value);
    }

    public Observable<List<String>> getData() {
        return executor.getData();
    }

    public void save() {

    }

    public void setMaxSize(int size) {
        executor.setMaxSize(size);
    }


}
