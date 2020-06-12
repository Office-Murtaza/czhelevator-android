package com.kingyon.elevator.entities;

import com.kingyon.elevator.entities.entities.ConentEntity;
import com.leo.afbaselibrary.nets.entities.PageListEntity;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class MyWalletInfo {
    private Float balance;
    private ConentEntity<WalletRecordEntity> recordPage;

    public MyWalletInfo(Float balance, ConentEntity<WalletRecordEntity> recordPage) {
        this.balance = balance;
        this.recordPage = recordPage;
    }

    @Override
    public String toString() {
        return "MyWalletInfo{" +
                "balance=" + balance +
                ", recordPage=" + recordPage +
                '}';
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public ConentEntity<WalletRecordEntity> getRecordPage() {
        return recordPage;
    }

    public void setRecordPage(ConentEntity<WalletRecordEntity> recordPage) {
        this.recordPage = recordPage;
    }
}
