package com.jie.calculator.calculator.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created on 2019/1/13.
 *
 * @author Jie.Wu
 */
public class HttpUtils {

    private OkHttpClient client = new OkHttpClient();

    private HttpUtils() {
    }

    private static final class LazyHolder {
        private static final HttpUtils instance = new HttpUtils();
    }

    public static HttpUtils getInst() {
        return LazyHolder.instance;
    }

    public <T> Observable<T> get(String url, Class<T> tClass) {
        Request req = new Request.Builder()
                .url(url)
                .build();
        return Observable.just(client.newCall(req))
                .subscribeOn(Schedulers.io())
                .map(Call::execute)
                .flatMap(response -> {
                    if (response.body() != null) {
                        Gson gson = new GsonBuilder().setLenient().create();
                        return Observable.just(gson.fromJson(response.body().string(), tClass));
                    } else {
                        return Observable.error(new NullPointerException("Response is empty!"));
                    }
                });
    }

    public <T> Observable<List<T>> getList(String url, Class<T> t, Type type) {
        Request req = new Request.Builder()
                .url(url)
                .build();
        return Observable.just(client.newCall(req))
                .subscribeOn(Schedulers.io())
                .map(Call::execute)
                .flatMap(response -> {
                    if (response.body() != null) {
                        Gson gson = new GsonBuilder().setLenient().create();
                        return Observable.just(gson.fromJson(response.body().string(), type));
                    } else {
                        return Observable.error(new NullPointerException("Response is empty!"));
                    }
                });
    }


}
