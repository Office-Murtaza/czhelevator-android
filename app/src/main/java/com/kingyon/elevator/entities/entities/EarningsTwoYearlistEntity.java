package com.kingyon.elevator.entities.entities;

/**
 * @Created By Admin  on 2020/6/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class EarningsTwoYearlistEntity {
    /**
     *  "month": "06",
     *         "totalIncomeMonth": 18.386,
     *         "totalPayMonth": -34.839
     *          "day": "22",
     *         "totalIncomeDay": 8.586,
     *         "totalPayDay": 0
     *
     * */

    public int month;
    public int day;
    public double totalIncomeMonth;
    public double totalIncomeDay;
    public double totalPayMonth;
    public double totalPayDay;

    @Override
    public String toString() {
        return "EarningsTwoYearlistEntity{" +
                "month=" + month +
                ", day=" + day +
                ", totalIncomeMonth=" + totalIncomeMonth +
                ", totalIncomeDay=" + totalIncomeDay +
                ", totalPayMonth=" + totalPayMonth +
                ", totalPayDay=" + totalPayDay +
                '}';
    }



}
