package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class WalletRecordEntity {

    /**
     * objectId : 1
     * name : 行业组名称
     * type : income
     * amount : 256.36
     * remarks : 备注（比如支付方式）
     */

    private long objectId;
    private String name;
    private String type;
    private float amount;
    private String remarks;
    private long time;
    private String payType;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
