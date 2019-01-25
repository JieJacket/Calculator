package com.jal.studio.ad.platform.manager;



import com.jal.studio.ad.platform.callback.ADCallback;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created on 2019/1/7.
 *
 * @author Jie.Wu
 */
public class AdsObserverManager {

    public static final int READY = 0;
    public static final int PRESENT = 1;
    public static final int CLICKED = 2;
    public static final int DISMISS = 3;
    public static final int ERROR = 4;

    private final List<ADCallback> callbacks = new CopyOnWriteArrayList<>();


    private AdsObserverManager() {
    }

    private static final class LazyHolder {
        private static final AdsObserverManager instance = new AdsObserverManager();
    }

    public static AdsObserverManager getInst() {
        return LazyHolder.instance;
    }


    public void addObserver(ADCallback callback) {
        if (callback != null) {
            callbacks.add(callback);
        }
    }

    public void removeObserver(ADCallback callback) {
        if (callback != null) {
            callbacks.remove(callback);
        }
    }

    public void notifyStatusChanged(int status) {
        for (ADCallback callback : callbacks) {
            switch (status) {
                case READY:
                    callback.onADReady();
                    break;
                case PRESENT:
                    callback.onADPresent();
                    break;
                case CLICKED:
                    callback.onADClicked();
                    break;
                case DISMISS:
                    callback.onADDismissed();
                    break;
                default:
                    break;
            }
        }
    }

    public void notifyStatusError(String msg) {
        for (ADCallback callback : callbacks) {
            callback.onADError(msg);
        }
    }


}
