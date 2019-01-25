package com.jal.studio.ad.platform;

import android.content.Context;

import com.jal.studio.ad.platform.impl.PlatformHolder;


/**
 * Created on 19/09/2017.
 *
 * @author leveme
 */

public class Platforms {

    public static int ADCount = 3;

    public static void install(Context context, String... configs) {
        PlatformHolder.init(context, configs);
    }

    public static Platform obtain(int id) {
        return PlatformHolder.getPlatform(id);
    }

}
