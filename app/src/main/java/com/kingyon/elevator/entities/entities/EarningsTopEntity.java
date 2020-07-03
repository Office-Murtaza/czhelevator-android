package com.kingyon.elevator.entities.entities;

import java.util.Collections;
import java.util.List;

/**
 * @Created By Admin  on 2020/6/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class EarningsTopEntity<T> {

    /**
     * "totalEarning": -16.453,
     *     "totalIncome": 18.386,
     *     "totalPay": -34.839,
     *     "lstMonthItem": null,
     *     "lstDayItem": [
     * */
    public double totalEarning;
    public double totalIncome;
    public double totalPay;
    public double maxIncomeDay;
    public double maxPayDay;
    public List<T> lstMonthItem;
    public List<T> lstDayItem;


    @Override
    public String toString() {
        return "EarningsTopEntity{" +
                "totalEarning=" + totalEarning +
                ", totalIncome=" + totalIncome +
                ", totalPay=" + totalPay +
                ", lstMonthItem=" + lstMonthItem +
                ", lstDayItem=" + lstDayItem +
                '}';
    }

}
