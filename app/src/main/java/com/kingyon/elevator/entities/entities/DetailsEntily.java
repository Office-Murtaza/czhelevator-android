package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/8/18
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class DetailsEntily {

    /*
    * orderSn
    * payWay
    * payTime
    * realPrice
    * */

    public String orderSn;
    public String payWay;
    public long payTime;
    public String realPrice;

    @Override
    public String toString() {
        return "DetailsEntily{" +
                "orderSn='" + orderSn + '\'' +
                ", payWay='" + payWay + '\'' +
                ", payTime=" + payTime +
                ", realPrice='" + realPrice + '\'' +
                '}';
    }
}
