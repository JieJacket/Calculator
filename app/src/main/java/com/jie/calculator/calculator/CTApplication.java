package com.jie.calculator.calculator;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.jal.calculator.store.ds.DSManager;
import com.jal.calculator.store.push.Analysis;
import com.jie.calculator.calculator.cache.HistorySearchManager;
import com.jie.calculator.calculator.cache.Repository;
import com.jie.calculator.calculator.push.UmengMessageHandlerWrapper;
import com.jie.calculator.calculator.push.UmengNotificationClickHandler;
import com.jie.calculator.calculator.util.CommonConstants;

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
        initDS();
        initPushSDK();
        initCacheUtils();
    }

    private void initDS() {
        DSManager.getInst().init(this);
    }

    private void initPushSDK() {
        Analysis.getInst().init(this, BuildConfig.CHANNEL,
                CommonConstants.UM_APP_KEY, CommonConstants.UM_SECRET_KEY);

        Analysis.getInst().setLogEnable(BuildConfig.DEBUG);
        Analysis.getInst().setMessageHandler(new UmengMessageHandlerWrapper(this));
        Analysis.getInst().setNotificationClickHandler(new UmengNotificationClickHandler(this));
    }

    private void initCacheUtils() {
        repository = Repository.init(getCacheDir());
        HistorySearchManager.getInst().init(this);
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
