package com.jal.studio.ad.platform.impl.gdt;

import android.content.Context;
import android.view.View;

import com.jal.studio.ad.platform.AD;
import com.qq.e.ads.nativ.NativeMediaADData;
import com.qq.e.comm.constants.AdPatternType;

import java.util.List;

/**
 * Created on 19/09/2017.
 *
 * @author leveme
 */

public class GdtADWrapper implements AD {

    private NativeMediaADData raw;

    public GdtADWrapper(NativeMediaADData raw) {
        this.raw = raw;
    }

    @Override
    public int getType() {
        if (raw != null && raw.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
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
        return raw != null;
    }

    @Override
    public boolean isApp() {
        return raw != null && raw.isAPP();
    }

    @Override
    public String getBrand() {
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
            return raw.getImgUrl();
        }
        return null;
    }

    @Override
    public List<String> getImageUrls() {
        if (raw != null) {
            return raw.getImgList();
        }
        return null;
    }

    @Override
    public void onExposed(View view) {
        if (raw != null) {
            raw.onExposured(view);
        }
    }

    @Override
    public void onClicked(View view) {
        if (raw != null) {
            raw.onClicked(view);
        }
    }
}
