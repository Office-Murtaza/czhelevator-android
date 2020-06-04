package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/8.
 * Email：lc824767150@163.com
 */

public class CommitOrderEntiy {
    private long orderSn;
    private float priceActual;

    @Override
    public String toString() {
        return "CommitOrderEntiy{" +
                "orderId=" + orderSn +
                ", payMoney=" + priceActual +
                '}';
    }

    public long getOrderId() {
        return orderSn;
    }

    public void setOrderId(long orderId) {
        this.orderSn = orderId;
    }

    public float getPayMoney() {
        return priceActual;
    }

    public void setPayMoney(float payMoney) {
        this.priceActual = payMoney;
    }
}
