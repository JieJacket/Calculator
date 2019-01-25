package com.jal.studio.ad.platform;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created on 13/09/2017.
 *
 * @author leveme
 */

public interface AD {
    int TYPE_IMAGE = 0;
    int TYPE_VIDEO = 1;

    int getType();

    Object getRaw();

    boolean isAvailable(Context context);

    boolean isApp();

    String getBrand();

    String getTitle();

    String getDesc();

    String getIconUrl();

    String getImageUrl();

    List<String> getImageUrls();

    void onExposed(View view);

    void onClicked(View view);

}
