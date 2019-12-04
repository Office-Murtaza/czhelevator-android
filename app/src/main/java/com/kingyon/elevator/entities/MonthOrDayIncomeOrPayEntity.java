package com.kingyon.elevator.entities;

import java.util.List;

/**
 * Created By SongPeng  on 2019/11/29
 * Email : 1531603384@qq.com
 * 收益记录 图表数据实体 收入或者支出
 */
public class MonthOrDayIncomeOrPayEntity {

    private double maxValue;
    private List<ListBean> list;

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * title : 2019年11月01日
         * value : 176.4
         * step : 1
         */

        private String title;
        private double value;
        private int step;

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

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }
    }
}
