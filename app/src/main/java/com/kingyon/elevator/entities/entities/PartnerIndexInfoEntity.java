package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/6/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class PartnerIndexInfoEntity {

    /**
     *      "total": -25.039,
     *       "yesterday": 0,
     *       "withdrawal": 0,
     *       "canWithdraw": 0,
     *       "source": null,
     *       "createTime": null,
     *       "type": null
     * */

    public double total;
    public double yesterdayMoney;
    public double withdrawal;
    public double canWithdraw;
    public String source;
    public String createTime;
    public String type;

    @Override
    public String toString() {
        return "PartnerIndexInfoEntity{" +
                "total=" + total +
                ", yesterday=" + yesterdayMoney +
                ", withdrawal=" + withdrawal +
                ", canWithdraw=" + canWithdraw +
                ", source='" + source + '\'' +
                ", createTime='" + createTime + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
