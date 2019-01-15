package com.jie.calculator.calculator.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Rxbus is just used to deliever event
 */
public class RxBus {

    private static volatile RxBus ins;

    private final Subject<Object> bus;
    private final Map<Class<?>, Object> mStickyEventMap;

    public RxBus() {
        bus = PublishSubject.create().toSerialized();
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus getIns() {
        if (ins == null) {
            synchronized (RxBus.class) {
                if (ins == null) {
                    ins = new RxBus();
                }
            }
        }
        return ins;
    }

    public void post(Object event) {
        bus.onNext(event);
    }

    public void postStickyEvent(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        bus.onNext(event);

    }


    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }


    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = bus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);

            if (event != null) {
                return observable.mergeWith(Observable.create(subscriber -> {
                    if (subscriber != null) {
                        subscriber.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }


    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }


    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }


}