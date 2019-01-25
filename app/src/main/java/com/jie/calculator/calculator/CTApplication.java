package com.jie.calculator.calculator;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.jal.calculator.store.ds.DSManager;
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
        DSManager.getInst().init(this);
    }

    public static Repository getRepository() {
        return repository;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
