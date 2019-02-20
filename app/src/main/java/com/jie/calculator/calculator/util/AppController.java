package com.jie.calculator.calculator.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jie.calculator.calculator.CTApplication;

/**
 * Created on 2019/2/18.
 *
 * @author Jie.Wu
 */
public class AppController {

    private SharedPreferences preferences;

    public static final String KEY_OF_PAGE_LAYOUT = "main_page_layout";
    public static final int MODE_LINEAR = 0;
    public static final int MODE_GRID = 1;

    private AppController() {
        preferences = CTApplication.getContext()
                .getSharedPreferences("app-controller-infos", Context.MODE_PRIVATE);
    }

    private static final class LazyHolder {
        private static final AppController instance = new AppController();
    }

    public static AppController getInst() {
        return LazyHolder.instance;
    }

    public <T> T getValue(String key, Class<T> tClass) {
        return getValue(key, tClass, null);
    }

    public <T> T getValue(String key, Class<T> tClass, T t) {

        String data = preferences.getString(key, "");
        if (!TextUtils.isEmpty(data)) {
            //noinspection unchecked
            return (T) new Gson().toJson(data, tClass);
        }
        return t;
    }

    public <T> void putValue(String key, T t) {
        if (t instanceof Integer) {
            preferences.edit().putInt(key, (Integer) t).apply();
        } else if (t instanceof Long) {
            preferences.edit().putLong(key, (Long) t).apply();
        } else if (t instanceof Boolean) {
            preferences.edit().putBoolean(key, (Boolean) t).apply();
        } else if (t instanceof Float) {
            preferences.edit().putFloat(key, (Float) t).apply();
        } else if (t != null) {
            preferences.edit()
                    .putString(key, new Gson().toJson(t))
                    .apply();
        }
    }

    public Object getBaseValue(String key, Object obj) {
        if (obj instanceof Integer) {
            return preferences.getInt(key, (Integer) obj);
        }
        if (obj instanceof Long) {
            return preferences.getLong(key, (Long) obj);
        }
        if (obj instanceof Boolean) {
            return preferences.getBoolean(key, (Boolean) obj);
        }
        if (obj instanceof Float) {
            return preferences.getFloat(key, (Float) obj);
        }

        return null;
    }


}
