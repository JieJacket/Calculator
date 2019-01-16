package com.jie.calculator.calculator.util;

/**
 * Created on 2019/1/14.
 *
 * @author Jie.Wu
 */
public class EmptyWrapper<T> {
    private  T t;

    public EmptyWrapper(T t) {
        this.t = t;
    }

    public boolean isNonNull(){
        return t != null;
    }

    public T getValue() {
        return t;
    }
}
