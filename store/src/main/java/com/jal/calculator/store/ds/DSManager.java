package com.jal.calculator.store.ds;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.jal.calculator.store.ds.model.AliSessionWrapper;
import com.jal.calculator.store.ds.util.Constants;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created on 2019/1/24.
 *
 * @author Jie.Wu
 */
public class DSManager {

    private static final String TAG = "DSManager";
    private String session;
    private Session authSession;

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
                syncPublicConfig();
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.e(TAG, code + ":" + msg);
            }
        });
        initBaseConfig(context.getApplicationContext());
    }

    private void initBaseConfig(Context context) {
        pid = context.getResources().getString(R.string.ali_pid);
        if (!TextUtils.isEmpty(pid)) {
            adzoneId = pid.substring(pid.lastIndexOf("_") + 1);
        }
    }

    public Observable<Boolean> authLogin() {
        return Observable.just(AlibcLogin.getInstance())
                .flatMap(loginService -> Observable.create(e -> loginService.showLogin(new AlibcLoginCallback() {

                            @Override
                            public void onSuccess(int i) {
                                if (!e.isDisposed()) {
                                    e.onNext(true);
                                    e.onComplete();
                                }
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                if (!e.isDisposed()) {
                                    e.onError(new Throwable("Auth error :" + i + "-" + s));
                                }
                            }
                        }))
                );
    }

    public void onAuthActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackContext.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLogin() {
        return AlibcLogin.getInstance().isLogin();
    }

    public AliSessionWrapper getAliSession() {
        AliSessionWrapper alSession = new AliSessionWrapper();
        Session session = AlibcLogin.getInstance().getSession();
        if (session != null) {
            alSession.userid = session.userid;
            alSession.nick = session.nick;
            alSession.avatarUrl = session.avatarUrl;
            alSession.openId = session.openId;
            alSession.openSid = session.openSid;
            alSession.topAccessToken = session.topAccessToken;
            alSession.topAuthCode = session.topAuthCode;
            alSession.topExpireTime = session.topExpireTime;
        }

        return alSession;
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

    public void openOrders(Activity context, int orderType, WebView webView, WebViewClient webViewClient,
                           WebChromeClient webChromeClient) {
        AlibcBasePage page = new AlibcMyOrdersPage(orderType, true);
        AlibcTrade.show(context, webView, webViewClient, webChromeClient, page,
                new AlibcShowParams(), null, null, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                        Log.e(TAG, "openOrders onTradeSuccess");
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
        AlibcTradeSDK.setSyncForTaoke(true);
        AlibcTradeSDK.setForceH5(false);
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
