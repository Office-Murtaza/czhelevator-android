package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/8.
 * Email：lc824767150@163.com
 */

public class CommitOrderEntiy {
    private String orderSn;
    private String payPopUpMessage;
    private float priceActual;

    public String getPayPopUpMessage() {
        return payPopUpMessage;
    }

    public void setPayPopUpMessage(String payPopUpMessage) {
        this.payPopUpMessage = payPopUpMessage;
    }

    @Override
    public String toString() {
        return "CommitOrderEntiy{" +
                "orderId=" + orderSn +
                ", payMoney=" + priceActual +
                '}';
    }

    public String getOrderId() {
        return orderSn;
    }

    public void setOrderId(String orderId) {
        this.orderSn = orderId;
    }

    public float getPayMoney() {
        return priceActual;
    }

    public void setPayMoney(float payMoney) {
        this.priceActual = payMoney;
    }
}
