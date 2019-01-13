package com.jie.calculator.calculator;

import android.app.Application;

import com.jie.calculator.calculator.cache.Repository;

/**
 * Created on 2019/1/4.
 *
 * @author Jie.Wu
 */
public class CTApplication extends Application {

    private static Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = Repository.init(getCacheDir());
    }

    public static Repository getRepository() {
        return repository;
    }

}
