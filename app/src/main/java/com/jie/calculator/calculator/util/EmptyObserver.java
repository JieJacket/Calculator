package com.jie.calculator.calculator.util;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 2019/1/16.
 *
 * @author Jie.Wu
 */
public class EmptyObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
