package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/8.
 * Email：lc824767150@163.com
 */

public class CommitOrderEntiy {
    private long orderId;
    private float payMoney;

    @Override
    public String toString() {
        return "CommitOrderEntiy{" +
                "orderId=" + orderId +
                ", payMoney=" + payMoney +
                '}';
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public float getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(float payMoney) {
        this.payMoney = payMoney;
    }
}
