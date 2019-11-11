package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class IncomeRecordEntity {

    /**
     * objectId : 1
     * orderSn : 订单号
     * cellId : 123
     * partnerAccount : 0
     * deviceId : 1
     * type : income
     * who : partner
     * amount : 23.65
     * remarks : 备注
     * createTime : 15864656
     * isReadly : true
     */

    private long objectId;
    private String orderSn;
    private long cellId;
    private String partnerAccount;
    private long deviceId;
    private String type;
    private String who;
    private float amount;
    private String remarks;
    private long createTime;
    private boolean isReadly;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public long getCellId() {
        return cellId;
    }

    public void setCellId(long cellId) {
        this.cellId = cellId;
    }

    public String getPartnerAccount() {
        return partnerAccount;
    }

    public void setPartnerAccount(String partnerAccount) {
        this.partnerAccount = partnerAccount;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
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

    public boolean isReadly() {
        return isReadly;
    }

    public void setReadly(boolean readly) {
        isReadly = readly;
    }
}
