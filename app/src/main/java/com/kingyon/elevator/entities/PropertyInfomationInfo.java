package com.kingyon.elevator.entities;

import com.leo.afbaselibrary.nets.entities.PageListEntity;

/**
 * Created by GongLi on 2019/1/15.
 * Email：lc824767150@163.com
 */

public class PropertyInfomationInfo {
    private Integer freeNumber;
    private PageListEntity<OrderDetailsEntity> infomationPage;

    public PropertyInfomationInfo(Integer freeNumber, PageListEntity<OrderDetailsEntity> infomationPage) {
        this.freeNumber = freeNumber;
        this.infomationPage = infomationPage;
    }

    public Integer getFreeNumber() {
        return freeNumber;
    }

    public void setFreeNumber(Integer freeNumber) {
        this.freeNumber = freeNumber;
    }

    public PageListEntity<OrderDetailsEntity> getInfomationPage() {
        return infomationPage;
    }

    public void setInfomationPage(PageListEntity<OrderDetailsEntity> infomationPage) {
        this.infomationPage = infomationPage;
    }
}
