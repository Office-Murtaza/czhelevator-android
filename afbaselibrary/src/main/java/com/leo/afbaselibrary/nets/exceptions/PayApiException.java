package com.leo.afbaselibrary.nets.exceptions;

import com.leo.afbaselibrary.nets.entities.WxPayEntity;

/**
 * Created by GongLi on 2018/3/7.
 * Email：lc824767150@163.com
 */

public class PayApiException extends ApiException {
    private WxPayEntity payEntity;

    public PayApiException(Throwable throwable, int code, WxPayEntity payEntity) {
        super(throwable, code);
        this.payEntity = payEntity;
    }

    public WxPayEntity getPayEntity() {
        return payEntity;
    }

    public void setPayEntity(WxPayEntity payEntity) {
        this.payEntity = payEntity;
    }
}
