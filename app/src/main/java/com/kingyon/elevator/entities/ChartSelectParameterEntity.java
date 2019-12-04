package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2019/12/2
 * Email : 1531603384@qq.com
 * 点击图标marker时的参数
 */
public class ChartSelectParameterEntity {

    private int selectCatType = 0;//当前按年查看  1表示为按月查看
    private int currentSelectYear = 2019;//当前选择的年份
    private int currentSelectDay = 1;//日期
    private int currentSelectMonth = 1;//当前选择的月份
    private int selectIncomeOrPay = 0;//选择的是收入还是支出  0收入 1支出

    public ChartSelectParameterEntity(int selectCatType, int currentSelectYear, int currentSelectMonth, int selectIncomeOrPay) {
        this.selectCatType = selectCatType;
        this.currentSelectYear = currentSelectYear;
        this.currentSelectMonth = currentSelectMonth;
        this.selectIncomeOrPay = selectIncomeOrPay;
    }

    public int getSelectCatType() {
        return selectCatType;
    }

    public void setSelectCatType(int selectCatType) {
        this.selectCatType = selectCatType;
    }

    public int getCurrentSelectYear() {
        return currentSelectYear;
    }

    public void setCurrentSelectYear(int currentSelectYear) {
        this.currentSelectYear = currentSelectYear;
    }

    public int getCurrentSelectMonth() {
        return currentSelectMonth;
    }

    public void setCurrentSelectMonth(int currentSelectMonth) {
        this.currentSelectMonth = currentSelectMonth;
    }

    public int getSelectIncomeOrPay() {
        return selectIncomeOrPay;
    }

    public void setSelectIncomeOrPay(int selectIncomeOrPay) {
        this.selectIncomeOrPay = selectIncomeOrPay;
    }

    public int getCurrentSelectDay() {
        return currentSelectDay;
    }

    public void setCurrentSelectDay(int currentSelectDay) {
        this.currentSelectDay = currentSelectDay;
    }
}
