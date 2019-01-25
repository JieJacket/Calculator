package com.jal.calculator.store.ds.network;

import com.jal.calculator.store.ds.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 2019/1/23.
 *
 * @author Jie.Wu
 */
public class AliServerManager {

    private OkHttpClient okHttpClient;

    private AliServerManager(){}

    private static final class LazyHolder{
        private static final AliServerManager instance = new AliServerManager();
    }

    public static AliServerManager getInst(){
        return LazyHolder.instance;
    }

    private OkHttpClient getOKHttpClient() {
        if (okHttpClient != null){
            return okHttpClient;
        }
        return okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .client(getOKHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.ALI_URL)
                .build();
    }

    public AliServer getServer(){
        return getRetrofit().create(AliServer.class);
    }


}
