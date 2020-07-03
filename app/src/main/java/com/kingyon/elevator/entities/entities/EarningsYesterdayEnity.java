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
          *  "total": 0,
          "yesterdayMoney": -0.245,
          "withdrawal": 0,
          "canWithdraw": 0,
          "source": "BANDWIDTH_PAY",
          "createTime": 1593274560000,
          "type": null
          *
    * */

    public String amount;
    public String dateValue;
    public String timeValue;
    public String typeName;

    public String total;
    public String yesterdayMoney;
    public String withdrawal;
    public String canWithdraw;
    public String source;
    public String createTime;
    public String type;

    @Override
    public String toString() {
        return "EarningsYesterdayEnity{" +
                "amount='" + amount + '\'' +
                ", dateValue='" + dateValue + '\'' +
                ", timeValue='" + timeValue + '\'' +
                ", typeName='" + typeName + '\'' +
                ", total='" + total + '\'' +
                ", yesterday='" + yesterdayMoney + '\'' +
                ", withdrawal='" + withdrawal + '\'' +
                ", canWithdraw='" + canWithdraw + '\'' +
                ", source='" + source + '\'' +
                ", createTime='" + createTime + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
