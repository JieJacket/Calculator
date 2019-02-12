package com.jie.calculator.calculator.util;

import android.text.TextUtils;

import com.jie.calculator.calculator.ui.BaseActivity;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created on 2019/2/11.
 *
 * @author Jie.Wu
 */
public class ActivityCacheManager {

    private final Set<BaseActivity> cached = new LinkedHashSet<>();

    private ActivityCacheManager() {
    }

    private static final class LazyHolder {
        private static final ActivityCacheManager instance = new ActivityCacheManager();
    }

    public static ActivityCacheManager getInst() {
        return LazyHolder.instance;
    }

    public void add(BaseActivity activity) {
        cached.add(activity);
    }

    public void remove(BaseActivity activity) {
        cached.remove(activity);
    }

    public boolean contain(BaseActivity activity) {
        return cached.contains(activity);
    }

    public boolean contain(Class<? extends BaseActivity> bc) {
        for (BaseActivity activity : cached) {
            if (activity != null) {
                if (TextUtils.equals(activity.getClass().getSimpleName(), bc.getSimpleName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void clear() {
        cached.clear();
    }


}
