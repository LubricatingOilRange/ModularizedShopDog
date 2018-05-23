package com.library.core.module.http.exception;


public class AppException extends Exception {

    private String errorCode;

    private String msg;

    public AppException(String errorCode) {
        this.errorCode = errorCode;
        this.msg = getMessage();
    }

    public AppException( String errorCode,String message) {
        this.errorCode = errorCode;
        this.msg = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
