package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class InvoiceEntity {

    /**
     * objectId : 1
     * invoiceType : company
     * invoiceStart : 发票抬头
     * invoiceNo : 税号
     * invoiceContent : 发票内容
     * receiveEmail : 接收邮件
     * invoiceAmount :
     */

    private long objectId;
    private String invoiceType;
    private String invoiceStart;
    private String invoiceNo;
    private String receiveEmail;
    private float invoiceAmount;
    private String bank;
    private long time;
    private String invoiceImg;
    private String content;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceStart() {
        return invoiceStart;
    }

    public void setInvoiceStart(String invoiceStart) {
        this.invoiceStart = invoiceStart;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(String receiveEmail) {
        this.receiveEmail = receiveEmail;
    }

    public float getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(float invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getInvoiceImg() {
        return invoiceImg;
    }

    public void setInvoiceImg(String invoiceImg) {
        this.invoiceImg = invoiceImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
