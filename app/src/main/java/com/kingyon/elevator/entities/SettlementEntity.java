package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class SettlementEntity {

    /**
     * objectId : 1
     * billName : 计算单号
     * amount : 23.65
     * remarks : 备注
     * createTime : 15864656
     */

    private String billSn;
    private long objectId;
    private String billName;
    private float amount;
    private String remarks;
    private long createTime;

    public String getBillSn() {
        return billSn;
    }

    public void setBillSn(String billSn) {
        this.billSn = billSn;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
