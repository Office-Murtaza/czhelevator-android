package com.kingyon.paylibrary.wechatpay;

/**
 * created by arvin on 17/1/23 09:55
 * email：1035407623@qq.com
 */
public class WxPayStatusEntity {
    private int code;

    public WxPayStatusEntity(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
