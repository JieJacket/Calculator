package com.jie.calculator.calculator.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created on 2019/2/22.
 *
 * @author Jie.Wu
 */
public class QueryApis {


    private static final String URL_TEMPLATE = "https://suggest.taobao.com/sug?code=utf-8&q=${query}";

    private OkHttpClient client;

    private QueryApis() {
        client = new OkHttpClient();
    }

    private static final class LazyHolder {
        private static final QueryApis instance = new QueryApis();
    }

    public static QueryApis getInst() {
        return LazyHolder.instance;
    }

    public <T> Observable<T> query(String query, Class<T> tClass) {
        return queryInfo(query)
                .map(result -> {
                    //noinspection unchecked
                    return new Gson().fromJson(result, tClass);
                });
    }

    public <T> Observable<T> query(String query, TypeToken<T> token) {
        return queryInfo(query)
                .map(result -> {
                    //noinspection unchecked
                    return new Gson().fromJson(result, token.getType());
                });
    }

    private Observable<String> queryInfo(String query) {
        return Observable.just(new EmptyWrapper<>(query))
                .filter(EmptyWrapper::isNonNull)
                .map(EmptyWrapper::getValue)
                .map(String::trim)
                .flatMap(q -> {
                    String url = URL_TEMPLATE.replace("${query}", q);
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    ResponseBody body;
                    String result;
                    if ((body = response.body()) != null && !TextUtils.isEmpty(result = body.string())) {
                        return Observable.just(result);
                    }
                    return Observable.empty();
                });
    }
}
