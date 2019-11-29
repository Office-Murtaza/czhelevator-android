package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2019/11/29
 * Email : 1531603384@qq.com
 * 收益记录 图表数据实体 收入或者支出
 */
public class MonthOrDayIncomeOrPayEntity {

    private String title;
    private double value;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
