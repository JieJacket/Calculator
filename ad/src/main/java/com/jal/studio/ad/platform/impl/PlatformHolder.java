package com.jal.studio.ad.platform.impl;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import com.jal.studio.ad.platform.Platform;
import com.jal.studio.ad.platform.Platforms;
import com.jal.studio.ad.platform.impl.baidu.BaiduPlatform;
import com.jal.studio.ad.platform.impl.gdt.GdtPlatform;
import com.jal.studio.ad.platform.impl.toutiao.TtPlatform;


/**
 * Created on 26/09/2017.
 *
 * @author leveme
 */

public class PlatformHolder {
    private static final int PLATFORM_ID_BAIDU = 100;
    private static final int PLATFORM_ID_GDT = 101;
    private static final int PLATFORM_ID_TT = 107;

    private static SparseArray<String> ids;
    private static volatile SparseArray<Platform> platforms;

    public static void init(Context context, String... configs) {
        ids = parse(ids, getMetaData(context, "ADS_PLATFORM_IDS"));
        if (configs != null) {
            for (String config : configs) {
                ids = parse(ids, config);
            }
        }
    }

    private static String getMetaData(Context context, String key) {
        try {
            Object value = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA).metaData.get(key);
            return String.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    private static SparseArray<String> parse(SparseArray<String> out, String data) {
        if (data == null) return out;

        if (out == null) {
            out = new SparseArray<>();
        }
        for (String item : data.split(",")) {
            try {
                int index = item.indexOf(':');
                if (index > 0) {
                    String key = item.substring(0, index).trim();
                    String value = item.substring(index + 1).trim();

                    int id = Integer.parseInt(key);
                    if (value.length() > 0) {
                        out.put(id, value);
                    }
                }
            } catch (Exception e) {
                // continue
            }
        }

        return out;
    }

    private static String id(int id) {
        SparseArray<String> ids = PlatformHolder.ids;
        if (ids != null) {
            return ids.get(id);
        }
        return null;
    }

    public static SparseArray<Platform> getPlatforms() {
        if (platforms == null) {
            synchronized (Platforms.class) {
                if (platforms == null) {
                    platforms = new SparseArray<>();
                    platforms.put(PLATFORM_ID_BAIDU, new BaiduPlatform(id(PLATFORM_ID_BAIDU)));
                    platforms.put(PLATFORM_ID_GDT, new GdtPlatform(id(PLATFORM_ID_GDT)));
                    platforms.put(PLATFORM_ID_TT, new TtPlatform(id(PLATFORM_ID_TT)));
                }
            }
        }

        return platforms;
    }

    public static Platform getPlatform(int id) {
        return getPlatforms().get(id);
    }
}
