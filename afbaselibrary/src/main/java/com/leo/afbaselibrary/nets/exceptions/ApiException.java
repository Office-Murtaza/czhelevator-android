package com.leo.afbaselibrary.nets.exceptions;

/**
 * Created by Leo on 2016/5/4
 */
public class ApiException extends Exception {
    private final int code;
    private String message;

    public static final int NO_LOGIN = 999;
    public static final int RE_LOGIN = 1000;
    public static final int PARSE_ERROR = 1001;
    public static final int UNKNOWN = 1002;
    public static final int NET_NOT_AVAILABLE = 1003;
    public static final int VERSION_LOW = 1005;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ApiException(Throwable throwable, int code, String message) {
        super(throwable);
        this.code = code;
        this.message = message;
    }



    public int getCode() {
        return code;
    }

    public String getDisplayMessage() {
        return message;
    }

    public void setDisplayMessage(String msg) {
        this.message = msg;
    }
}