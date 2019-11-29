package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2019/11/29
 * Email : 1531603384@qq.com
 * 收益记录 顶部总收益 收入 支出三个数据实体
 */
public class IncomeOrPayEntity {


    /**
     * gains : 19878.06
     * income : 21341.38
     * pay : 1463.32
     */
    private double gains;
    private double income;
    private double pay;

    public double getGains() {
        return gains;
    }

    public void setGains(double gains) {
        this.gains = gains;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

}
