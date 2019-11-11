package com.kingyon.elevator.entities;

import java.util.List;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class DeviceNumberInfo {
    private DeviceNumberEntity deviceNumber;
    private List<CellDeviceNumberEntity> cellDeviceNumbers;

    public DeviceNumberInfo(DeviceNumberEntity deviceNumber, List<CellDeviceNumberEntity> cellDeviceNumbers) {
        this.deviceNumber = deviceNumber;
        this.cellDeviceNumbers = cellDeviceNumbers;
    }

    public DeviceNumberEntity getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(DeviceNumberEntity deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public List<CellDeviceNumberEntity> getCellDeviceNumbers() {
        return cellDeviceNumbers;
    }

    public void setCellDeviceNumbers(List<CellDeviceNumberEntity> cellDeviceNumbers) {
        this.cellDeviceNumbers = cellDeviceNumbers;
    }
}
