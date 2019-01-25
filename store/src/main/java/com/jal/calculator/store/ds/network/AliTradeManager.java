package com.jal.calculator.store.ds.network;

import android.app.Activity;
import android.util.Log;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2019/1/24.
 *
 * @author Jie.Wu
 */
public class AliTradeManager {

    private static final String TAG = "AliTradeManager";


    private AliTradeManager() {
    }

    private static final class LazyHolder {
        private static final AliTradeManager instance = new AliTradeManager();
    }

    public static AliTradeManager getInst() {
        return LazyHolder.instance;
    }

    public void showDetails(Activity activity, String itemId) {
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        AlibcBasePage detailPage = new AlibcDetailPage(itemId);
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
        AlibcTrade.show(activity, detailPage, showParams, null, exParams, new AlibcTradeCallback() {
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
