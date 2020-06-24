package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/6/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class EarningsYesterdayEnity {

    /*
    *    "total": 0,
          "yesterday": 4.767,
          "withdrawal": 0,
          "canWithdraw": 0,
          "source": "EARNINGS",
          "createTime": 1592827349000,
          "type": null
    * */

    public String total;
    public String yesterday;
    public String withdrawal;
    public String canWithdraw;
    public String source;
    public long createTime;
    public String type;

    @Override
    public String toString() {
        return "EarningsYesterdayEnity{" +
                "total='" + total + '\'' +
                ", yesterday='" + yesterday + '\'' +
                ", withdrawal='" + withdrawal + '\'' +
                ", canWithdraw='" + canWithdraw + '\'' +
                ", source='" + source + '\'' +
                ", createTime=" + createTime +
                ", type='" + type + '\'' +
                '}';
    }
}
