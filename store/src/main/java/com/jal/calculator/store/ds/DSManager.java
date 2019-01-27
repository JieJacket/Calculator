package com.jal.calculator.store.ds;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.jal.calculator.store.ds.util.Constants;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created on 2019/1/24.
 *
 * @author Jie.Wu
 */
public class DSManager {

    private static final String TAG = "DSManager";
    private String session;

    private String pid;
    private String adzoneId;

    private DSManager() {
    }

    private static final class LazyHolder {

        private static final DSManager instance = new DSManager();

    }

    public static DSManager getInst() {
        return LazyHolder.instance;
    }

    public void init(Application context) {
        AlibcTradeSDK.asyncInit(context, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "onSuccess");
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.e(TAG, code + ":" + msg);
            }
        });
        initBaseConfig(context.getApplicationContext());
        syncPublicConfig();
    }

    private void initBaseConfig(Context context) {
        pid = context.getResources().getString(R.string.ali_pid);
        if (!TextUtils.isEmpty(pid)) {
            adzoneId = pid.substring(pid.lastIndexOf("_") + 1);
        }
    }

    public Observable<Integer> authLogin() {
        return Observable.just(AlibcLogin.getInstance())
                .flatMap(alibcLogin -> Observable.create((ObservableOnSubscribe<Integer>) e -> {
                    alibcLogin.showLogin(new AlibcLoginCallback() {
                        @Override
                        public void onSuccess(int i) {
                            if (!e.isDisposed()) {
                                e.onNext(i);
                                e.onComplete();
                            }
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            if (!e.isDisposed()) {
                                e.tryOnError(new RuntimeException(i + "K" + s));
                            }
                        }
                    });

                }));
    }

    public boolean isAliAuth() {
        Session session = AlibcLogin.getInstance().getSession();
        return session != null && session.topAccessToken != null;
    }

    public void showDetails(Activity activity, String url) {
        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams(); // 若非淘客taokeParams设置为null即可
        alibcTaokeParams.adzoneid = adzoneId;
        alibcTaokeParams.pid = pid;
        alibcTaokeParams.subPid = pid;
        alibcTaokeParams.extraParams = new HashMap<>();
        alibcTaokeParams.extraParams.put("taokeAppkey", Constants.ALI_APP_KEY);

        AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.Native, false);
        Map<String, String> exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        AlibcTrade.show(activity, new AlibcPage(url), alibcShowParams, alibcTaokeParams, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                Log.e(TAG, "onTradeSuccess");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG, i + ":" + s);
            }
        });

    }


    public void syncPublicConfig() {
        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams(); // 若非淘客taokeParams设置为null即可
        alibcTaokeParams.adzoneid = adzoneId;
        alibcTaokeParams.pid = pid;
        alibcTaokeParams.subPid = pid;
        alibcTaokeParams.extraParams = new HashMap<>();
        alibcTaokeParams.extraParams.put("taokeAppkey", Constants.ALI_APP_KEY);
        AlibcTradeSDK.setTaokeParams(alibcTaokeParams);
    }


    public String getPid() {
        return pid;
    }

    public String getAdzoneId() {
        return adzoneId;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void destroy() {
        AlibcTradeSDK.destory();
    }
}
