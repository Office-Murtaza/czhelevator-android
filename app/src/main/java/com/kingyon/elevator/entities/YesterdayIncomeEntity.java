package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2019/12/2
 * Email : 1531603384@qq.com
 * 昨日收益或者已提现数据
 *
 "typeName": null,
 "amount": "10.000",
 "dateValue": "2020年06月24日",
 "timeValue": "17:16",
 "createTime": "2020-06-24 17:16:41"
 */
public class YesterdayIncomeEntity {

    private String amount;
    private String dateValue;
    private String timeValue;
    private String typeName;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

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
