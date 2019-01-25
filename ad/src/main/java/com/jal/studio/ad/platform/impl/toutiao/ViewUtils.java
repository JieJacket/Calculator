package com.jal.studio.ad.platform.impl.toutiao;

import android.os.Build;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

class ViewUtils {

    private static final String TAG = "ViewUtils";

    /**
     * Returns the current View.OnClickListener for the given View
     * @param view the View whose click listener to retrieve
     * @return the View.OnClickListener attached to the view; null if it could not be retrieved
     */
    public static View.OnClickListener getOnClickListener(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getOnClickListenerV14(view);
        } else {
            return getOnClickListenerV1(view);
        }
    }

    private static View.OnClickListener getOnClickListenerV1(View view) {
        View.OnClickListener listener = null;

        try {
            Field field = Class.forName("android.view.View").getDeclaredField("mOnClickListener");
            listener = (View.OnClickListener) field.get(view);
        } catch (Exception e) {
            Log.d(TAG, "getOnClickListenerV4: " + e, e);
        }

        return listener;
    }

    private static View.OnClickListener getOnClickListenerV14(View view) {
        View.OnClickListener listener = null;

        try {
            Object mListenerInfo = null;

            Field field = Class.forName("android.view.View").getDeclaredField("mListenerInfo");
            if (field != null) {
                field.setAccessible(true);
                mListenerInfo = field.get(view);
            }

            if (mListenerInfo != null) {
                field = Class.forName("android.view.View$ListenerInfo")
                        .getDeclaredField("mOnClickListener");
                if (field != null) {
                    listener = (View.OnClickListener) field.get(mListenerInfo);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "getOnClickListenerV14: " + e, e);
        }

        return listener;
    }
}
