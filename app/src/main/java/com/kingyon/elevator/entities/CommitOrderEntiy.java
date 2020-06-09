package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/8.
 * Email：lc824767150@163.com
 */

public class CommitOrderEntiy {
    private String orderSn;
    private float priceActual;

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
