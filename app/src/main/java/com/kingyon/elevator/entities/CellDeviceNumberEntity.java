package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class CellDeviceNumberEntity {

    /**
     * cellName : 美年广场
     * cellId : 152
     * totalScreen : 100
     * usedScreen : 80
     * onlineScreen : 90
     */

    private String cellAddress;
    private String cellName;
    private long cellId;
    private int totalScreen;
    private int usedScreen;
    private int onlineScreen;

    public String getCellAddress() {
        return cellAddress;
    }

    public void setCellAddress(String cellAddress) {
        this.cellAddress = cellAddress;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public long getCellId() {
        return cellId;
    }

    public void setCellId(long cellId) {
        this.cellId = cellId;
    }

    public int getTotalScreen() {
        return totalScreen;
    }

    public void setTotalScreen(int totalScreen) {
        this.totalScreen = totalScreen;
    }

    public int getUsedScreen() {
        return usedScreen;
    }

    public void setUsedScreen(int usedScreen) {
        this.usedScreen = usedScreen;
    }

    public int getOnlineScreen() {
        return onlineScreen;
    }

    public void setOnlineScreen(int onlineScreen) {
        this.onlineScreen = onlineScreen;
    }
}
