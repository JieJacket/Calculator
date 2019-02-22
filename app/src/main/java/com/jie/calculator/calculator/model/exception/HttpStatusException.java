package com.jie.calculator.calculator.model.exception;

/**
 * Created on 2019/2/22.
 *
 * @author Jie.Wu
 */
public class HttpStatusException extends RuntimeException {

    private int errorCode;
    private String errorMsg;

    public HttpStatusException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
