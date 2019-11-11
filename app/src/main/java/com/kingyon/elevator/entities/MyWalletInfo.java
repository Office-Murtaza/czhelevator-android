package com.kingyon.elevator.entities;

import com.leo.afbaselibrary.nets.entities.PageListEntity;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class MyWalletInfo {
    private Float balance;
    private PageListEntity<WalletRecordEntity> recordPage;

    public MyWalletInfo(Float balance, PageListEntity<WalletRecordEntity> recordPage) {
        this.balance = balance;
        this.recordPage = recordPage;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public PageListEntity<WalletRecordEntity> getRecordPage() {
        return recordPage;
    }

    public void setRecordPage(PageListEntity<WalletRecordEntity> recordPage) {
        this.recordPage = recordPage;
    }
}
