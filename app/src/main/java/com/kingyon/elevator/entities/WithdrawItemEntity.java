package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class WithdrawItemEntity {

    /**
     * objectId : 1
     * withDrawWay : ali
     * status : dealing
     * amount : 256.36
     * faildReason : 备注（比如支付方式）
     * aliAcount :
     * bankName :
     * cardNo :
     * cardholder :
     */

    private long time;
    private long objectId;
    private String withDrawWay;
    private String status;
    private double amount;
    private String faildReason;
    private String aliAcount;
    private String bankName;
    private String cardNo;
    private String cardholder;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getWithDrawWay() {
        return withDrawWay;
    }

    public void setWithDrawWay(String withDrawWay) {
        this.withDrawWay = withDrawWay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFaildReason() {
        return faildReason;
    }

    public void setFaildReason(String faildReason) {
        this.faildReason = faildReason;
    }

    public String getAliAcount() {
        return aliAcount;
    }

    public void setAliAcount(String aliAcount) {
        this.aliAcount = aliAcount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }
}
