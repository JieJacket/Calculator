package com.jie.calculator.calculator.model.rx;

/**
 * Created on 2019/2/1.
 *
 * @author Jie.Wu
 */
public class RxUpdatePageInfos {
    public int listMode;

    public static RxUpdatePageInfos create(){
        return new RxUpdatePageInfos();
    }

    public RxUpdatePageInfos listMode(int listMode){
        this.listMode = listMode;
        return this;
    }
}
