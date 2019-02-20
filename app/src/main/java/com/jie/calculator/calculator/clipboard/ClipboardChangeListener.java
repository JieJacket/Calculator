package com.jie.calculator.calculator.clipboard;

import android.app.Activity;
import android.content.ClipboardManager;

import java.lang.ref.WeakReference;

/**
 * Created on 2019/2/14.
 *
 * @author Jie.Wu
 */
public class ClipboardChangeListener implements ClipboardManager.OnPrimaryClipChangedListener {

    private WeakReference<Activity> weakReference;

    public ClipboardChangeListener(Activity activity) {
        this.weakReference = new WeakReference<>(activity);
    }

    @Override
    public void onPrimaryClipChanged() {

    }
}
