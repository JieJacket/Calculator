package com.jal.calculator.store.ds.util;

import com.google.gson.GsonBuilder;

import io.reactivex.ObservableTransformer;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class ConvertUtil {


    public static <T> ObservableTransformer<String, T> convert(Class<T> tClass) {
        return upstream -> upstream.map(json ->
                new GsonBuilder().setLenient().create().fromJson(json, tClass));
    }
}
