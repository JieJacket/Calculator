package com.jal.studio.ad.platform.manager;

import android.content.Context;
import android.view.ViewGroup;

import com.jal.studio.ad.platform.AD;
import com.jal.studio.ad.platform.Platform;
import com.jal.studio.ad.platform.Platforms;
import com.jal.studio.ad.platform.impl.toutiao.TtPlatform;
import com.jal.studio.ad.platform.model.Channel;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2019/1/21.
 *
 * @author Jie.Wu
 */
public class ADManager {

    private ADManager() {
    }

    private static final class LazyHolder {
        private static final ADManager instance = new ADManager();
    }

    public static ADManager getInst() {
        return LazyHolder.instance;
    }


    public Observable<AD> getNativeAD(Context context, Channel channel) {
        return Platforms.obtain(channel.getChannel())
                .getNativeAd(context, channel.getPid())
                .observeOn(Schedulers.io());
    }

    public Observable<AD> showFullScreenVideoAD(Context context, ViewGroup viewGroup, Channel channel){
        Platform platform = Platforms.obtain(channel.getChannel());
        if (platform instanceof TtPlatform){
            TtPlatform ttPlatform = (TtPlatform) platform;
            return ttPlatform.getRewardAd(context,channel.getPid(),"")
                    ;
        }
        return Observable.empty();
    }


}
