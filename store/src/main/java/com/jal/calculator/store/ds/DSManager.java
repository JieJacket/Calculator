package com.jal.calculator.store.ds;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.jal.calculator.store.ds.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2019/1/24.
 *
 * @author Jie.Wu
 */
public class DSManager {

    private static final String TAG = "DSManager";
    private String session;

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
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void showDetails(Activity activity, String url, String pid, String subPid, long adzoneid) {
        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams(); // 若非淘客taokeParams设置为null即可
        alibcTaokeParams.adzoneid = String.valueOf(adzoneid);
        alibcTaokeParams.pid = pid;
        alibcTaokeParams.subPid = subPid;
        alibcTaokeParams.extraParams = new HashMap<>();
        alibcTaokeParams.extraParams.put("taokeAppkey", Constants.ALI_APP_KEY);

        AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.Auto, false);
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
}
