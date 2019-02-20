package com.jie.calculator.calculator.clipboard;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Pair;

import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.ui.DSResultActivity;
import com.jie.calculator.calculator.util.EmptyObserver;
import com.jie.calculator.calculator.util.EmptyWrapper;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created on 2019/2/14.
 *
 * @author Jie.Wu
 */
public class DSClipboardManager {

    private ClipboardManager clipboardManager;

    AlertDialog alertDialog;

    private DSClipboardManager() {
    }

    private static final class LazyHolder {
        private static final DSClipboardManager instance = new DSClipboardManager();
    }

    public static DSClipboardManager getInst() {
        return LazyHolder.instance;
    }

    public void init(Context context) {
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void copy2Clipboard(String text) {
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
        }
    }

    public void registerListener(@NonNull Activity activity) {
        if (clipboardManager != null) {
            clipboardManager.addPrimaryClipChangedListener(new ClipboardChangeListener(activity));
        }
    }

    public String getText() {
        if (clipboardManager == null) {
            return null;
        }
        ClipData primaryClip = clipboardManager.getPrimaryClip();
        ClipData.Item item = null;
        if (primaryClip != null) {
            item = primaryClip.getItemAt(0);
        }
        if (item == null) {
            return null;
        }
        if (item.getUri() != null) {
            return null;
        }
        return item.getText().toString();
    }

    public void displayClipData(Activity activity) {
        Observable.just(new EmptyWrapper<>(getText()))
                .filter(EmptyWrapper::isNonNull)
                .map(EmptyWrapper::getValue)
                .filter(text -> !SearchBlacklistManager.getInst().getBlacklist().blockingFirst().contains(text))
                .flatMap(text -> Observable.create((ObservableOnSubscribe<Pair<Boolean, String>>) emitter -> {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                    alertDialog = new AlertDialog.Builder(activity)
                            .setTitle("是否想搜索")
                            .setMessage(text)
                            .setPositiveButton(R.string.str_search, (dialog, which) -> {
                                if (!emitter.isDisposed()) {
                                    emitter.onNext(Pair.create(true, text));
                                    emitter.onComplete();
                                }
                            })
                            .setNegativeButton(R.string.str_cancel, (dialog, which) -> {
                                if (!emitter.isDisposed()) {
                                    emitter.onNext(Pair.create(false, text));
                                    emitter.onComplete();
                                }
                            })
                            .create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }))
                .subscribe(new EmptyObserver<Pair<Boolean, String>>() {
                    @Override
                    public void onNext(Pair<Boolean, String> pair) {
                        SearchBlacklistManager.getInst().add2Blacklist(pair.second);
                        if (pair.first) {
                            Intent intent = new Intent(activity, DSResultActivity.class);
                            intent.putExtra(DSResultActivity.QUERY, pair.second);
                            activity.startActivity(intent);
                        }
                    }
                });
    }

}
