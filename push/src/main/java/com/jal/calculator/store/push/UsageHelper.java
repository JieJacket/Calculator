package com.jal.calculator.store.push;

import android.app.Activity;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2019/2/8.
 *
 * @author Jie.Wu
 */
public class UsageHelper {

    private UsageHelper() {
    }

    private static final class LazyHolder {
        private static final UsageHelper instance = new UsageHelper();
    }

    public static UsageHelper getInst() {
        return LazyHolder.instance;
    }

    public void onActivityResume(Activity activity) {
        MobclickAgent.onResume(activity);
    }


    public void onActivityPause(Activity activity) {
        MobclickAgent.onPause(activity);
    }

    public void onPageStart(String tag) {
        MobclickAgent.onPageStart(tag);
    }

    public void onPageEnd(String tag) {
        MobclickAgent.onPageEnd(tag);
    }


    public void recordSearchEvent(Context context, String query) {
        Map<String, Object> map = new HashMap<>();
        map.put("query", query);
        recordEvent(context, "search_event", map);
    }

    public void recordEvent(Context context, String key, Map<String, Object> map) {
        MobclickAgent.onEventObject(context, key, map);
    }
}
