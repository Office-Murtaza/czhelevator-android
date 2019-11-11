package com.kingyon.elevator.entities;

import com.leo.afbaselibrary.nets.entities.PageListEntity;

/**
 * Created by GongLi on 2019/1/15.
 * Email：lc824767150@163.com
 */

public class DeviceDetailsInfo {
    private PointItemEntity device;
    private PageListEntity<IncomeRecordEntity> incomePage;

    public DeviceDetailsInfo(PointItemEntity device, PageListEntity<IncomeRecordEntity> incomePage) {
        this.device = device;
        this.incomePage = incomePage;
    }

    public PointItemEntity getDevice() {
        return device;
    }

    public void setDevice(PointItemEntity device) {
        this.device = device;
    }

    public PageListEntity<IncomeRecordEntity> getIncomePage() {
        return incomePage;
    }

    public void setIncomePage(PageListEntity<IncomeRecordEntity> incomePage) {
        this.incomePage = incomePage;
    }
}
