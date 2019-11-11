package com.leo.afbaselibrary.nets.exceptions;

import com.leo.afbaselibrary.nets.entities.WxPayEntity;

/**
 * Created by GongLi on 2018/3/7.
 * Email：lc824767150@163.com
 */

public class PayResultException extends ResultException {

    private WxPayEntity payEntity;

    public PayResultException(int errCode, String msg, WxPayEntity payEntity) {
        super(errCode, msg);
        this.payEntity = payEntity;
    }

    public WxPayEntity getPayEntity() {
        return payEntity;
    }

    public void setPayEntity(WxPayEntity payEntity) {
        this.payEntity = payEntity;
    }
}
