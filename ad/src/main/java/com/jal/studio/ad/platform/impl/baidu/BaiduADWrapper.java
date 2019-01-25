package com.jal.studio.ad.platform.impl.baidu;

import android.content.Context;
import android.view.View;

import com.baidu.mobad.feeds.NativeResponse;
import com.jal.studio.ad.platform.AD;

import java.util.List;

/**
 * Created on 13/09/2017.
 *
 * @author leveme
 */

public class BaiduADWrapper implements AD {

    private final NativeResponse raw;

    public BaiduADWrapper(NativeResponse raw) {
        this.raw = raw;
    }

    @Override
    public int getType() {
        if (raw != null && raw.getMaterialType() == NativeResponse.MaterialType.VIDEO) {
            return TYPE_VIDEO;
        }

        return TYPE_IMAGE;
    }

    @Override
    public Object getRaw() {
        return raw;
    }

    @Override
    public boolean isAvailable(Context context) {
        return raw != null && raw.isAdAvailable(context);
    }

    @Override
    public boolean isApp() {
        return raw != null && raw.isDownloadApp();
    }

    @Override
    public String getBrand() {
        if (raw != null) {
            return raw.getBrandName();
        }
        return null;
    }

    @Override
    public String getTitle() {
        if (raw != null) {
            return raw.getTitle();
        }
        return null;
    }

    @Override
    public String getDesc() {
        if (raw != null) {
            return raw.getDesc();
        }
        return null;
    }

    @Override
    public String getIconUrl() {
        if (raw != null) {
            return raw.getIconUrl();
        }
        return null;
    }

    @Override
    public String getImageUrl() {
        if (raw != null) {
            return raw.getImageUrl();
        }
        return null;
    }

    @Override
    public List<String> getImageUrls() {
        if (raw != null) {
            return raw.getMultiPicUrls();
        }
        return null;
    }

    @Override
    public void onExposed(View view) {
        if (raw != null) {
            raw.recordImpression(view);
        }
    }

    @Override
    public void onClicked(View view) {
        if (raw != null) {
            raw.handleClick(view);
        }
    }
}
