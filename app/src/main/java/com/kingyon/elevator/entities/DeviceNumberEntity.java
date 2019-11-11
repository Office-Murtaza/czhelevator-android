package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class DeviceNumberEntity {

    /**
     * totalScreen : 1000
     * usedScreen : 800
     * onlineScreen : 900
     */

    private int totalScreen;
    private int usedScreen;
    private int onlineScreen;

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
