package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/4.
 * Email：lc824767150@163.com
 */

public class DeviceParamCache {
    private long cellId;
    private int count;
    private String deviceIds;

    public long getCellId() {
        return cellId;
    }

    public void setCellId(long cellId) {
        this.cellId = cellId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(String deviceIds) {
        this.deviceIds = deviceIds;
    }
}
