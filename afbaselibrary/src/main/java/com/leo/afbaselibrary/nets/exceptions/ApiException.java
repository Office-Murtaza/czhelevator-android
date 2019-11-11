package com.leo.afbaselibrary.nets.exceptions;

/**
 * Created by Leo on 2016/5/4
 */
public class ApiException extends Exception {
    private final int code;
    private String displayMessage;

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

    public ApiException(Throwable throwable, int code, String displayMessage) {
        super(throwable);
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String msg) {
        this.displayMessage = msg;
    }
}