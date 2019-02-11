package com.jie.calculator.calculator.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.jal.calculator.store.push.wrapper.NotificationClickHandlerWrapper;
import com.jal.calculator.store.push.wrapper.UMessageWrapper;
import com.jie.calculator.calculator.ui.DSMainActivity;

import java.util.Map;

/**
 * Created on 2019/2/11.
 *
 * @author Jie.Wu
 */
public class UmengNotificationClickHandler extends NotificationClickHandlerWrapper {

    private Context context;
    private static final int SEARCH_ID = 12;
    public static final String QUERY_KEY = "query";

    public UmengNotificationClickHandler(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public boolean canHandlerAction(int buildId) {
        return buildId == SEARCH_ID;
    }

    @Override
    public void dealWithAction(UMessageWrapper msg) {
        for (Map.Entry entry : msg.extra.entrySet()) {
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (TextUtils.equals(key, QUERY_KEY)) {
                Intent intent = new Intent(context, DSMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(QUERY_KEY, (String) value);
                context.startActivity(intent);
            }
        }
    }
}
