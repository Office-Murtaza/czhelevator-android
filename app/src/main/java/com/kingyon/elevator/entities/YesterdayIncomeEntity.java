package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2019/12/2
 * Email : 1531603384@qq.com
 * 昨日收益或者已提现数据
 */
public class YesterdayIncomeEntity {

    private String amount;
    private String dateValue;
    private String timeValue;
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDateValue() {
        return dateValue;
    }

    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }
}
