package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.kingyon.elevator.others.OnChoosedInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GongLi on 2018/12/29.
 * Email：lc824767150@163.com
 */

public class PointItemEntity extends OnChoosedInterface implements Parcelable {

    /**
     * objectId : 1
     * cellName : 小区名称
     * cellId : 12
     * build : 1幢
     * buildId : 120
     * unit : 单元
     * lift : 电梯号
     * liftId : 123
     * device : L
     * deviceId : 123654
     * deviceNo : 设备编号
     * deviceType : diy
     */

    private List<Long> occupyTimes;
    private String deliverState;
    private long objectId;
    private String cellName;
    private String cellAddress;
    private long cellId;
    private String build;
    private long buildId;
    private String unit;
//    private long unitId;
    private String liftNo;
    private String lift;
    private long liftId;
    private String device;
    private String deviceNo;
    private String deviceType;
    private String status;
    private String adStatus;
    private String liveAddress;
    private long netTime;
    private long installTime;

    @Override
    public String toString() {
        return "PointItemEntity{" +
                "occupyTimes=" + occupyTimes +
                ", deliverState='" + deliverState + '\'' +
                ", objectId=" + objectId +
                ", cellName='" + cellName + '\'' +
                ", cellAddress='" + cellAddress + '\'' +
                ", cellId=" + cellId +
                ", build='" + build + '\'' +
                ", buildId=" + buildId +
                ", unit='" + unit + '\'' +
                ", liftNo='" + liftNo + '\'' +
                ", lift='" + lift + '\'' +
                ", liftId=" + liftId +
                ", device='" + device + '\'' +
                ", deviceNo='" + deviceNo + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", status='" + status + '\'' +
                ", adStatus='" + adStatus + '\'' +
                ", liveAddress='" + liveAddress + '\'' +
                ", netTime=" + netTime +
                ", installTime=" + installTime +
                '}';
    }

    public List<Long> getOccupyTimes() {
        return occupyTimes;
    }

    public void setOccupyTimes(List<Long> occupyTimes) {
        this.occupyTimes = occupyTimes;
    }

    public String getDeliverState() {
        return deliverState;
    }

    public void setDeliverState(String deliverState) {
        this.deliverState = deliverState;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
    }

    public String getLiveAddress() {
        return liveAddress;
    }

    public void setLiveAddress(String liveAddress) {
        this.liveAddress = liveAddress;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getCellAddress() {
        return cellAddress;
    }

    public void setCellAddress(String cellAddress) {
        this.cellAddress = cellAddress;
    }

    public long getCellId() {
        return cellId;
    }

    public void setCellId(long cellId) {
        this.cellId = cellId;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public long getBuildId() {
        return buildId;
    }

    public void setBuildId(long buildId) {
        this.buildId = buildId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

//    public long getUnitId() {
//        return unitId;
//    }
//
//    public void setUnitId(long unitId) {
//        this.unitId = unitId;
//    }

    public String getLiftNo() {
        return liftNo;
    }

    public void setLiftNo(String liftNo) {
        this.liftNo = liftNo;
    }

    public String getLift() {
        return lift;
    }

    public void setLift(String lift) {
        this.lift = lift;
    }

    public long getLiftId() {
        return liftId;
    }

    public void setLiftId(long liftId) {
        this.liftId = liftId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public long getNetTime() {
        return netTime;
    }

    public void setNetTime(long netTime) {
        this.netTime = netTime;
    }

    public long getInstallTime() {
        return installTime;
    }

    public void setInstallTime(long installTime) {
        this.installTime = installTime;
    }

    @Override
    public String getStringName() {
        return String.valueOf(objectId);
    }

    public PointItemEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.occupyTimes);
        dest.writeString(this.deliverState);
        dest.writeLong(this.objectId);
        dest.writeString(this.cellName);
        dest.writeString(this.cellAddress);
        dest.writeLong(this.cellId);
        dest.writeString(this.build);
        dest.writeLong(this.buildId);
        dest.writeString(this.unit);
//        dest.writeLong(this.unitId);
        dest.writeString(this.liftNo);
        dest.writeString(this.lift);
        dest.writeLong(this.liftId);
        dest.writeString(this.device);
        dest.writeString(this.deviceNo);
        dest.writeString(this.deviceType);
        dest.writeString(this.status);
        dest.writeString(this.adStatus);
        dest.writeString(this.liveAddress);
        dest.writeLong(this.netTime);
        dest.writeLong(this.installTime);
    }

    protected PointItemEntity(Parcel in) {
        this.occupyTimes = new ArrayList<Long>();
        in.readList(this.occupyTimes, Long.class.getClassLoader());
        this.deliverState = in.readString();
        this.objectId = in.readLong();
        this.cellName = in.readString();
        this.cellAddress = in.readString();
        this.cellId = in.readLong();
        this.build = in.readString();
        this.buildId = in.readLong();
        this.unit = in.readString();
//        this.unitId = in.readLong();
        this.liftNo = in.readString();
        this.lift = in.readString();
        this.liftId = in.readLong();
        this.device = in.readString();
        this.deviceNo = in.readString();
        this.deviceType = in.readString();
        this.status = in.readString();
        this.adStatus = in.readString();
        this.liveAddress = in.readString();
        this.netTime = in.readLong();
        this.installTime = in.readLong();
    }

    public static final Creator<PointItemEntity> CREATOR = new Creator<PointItemEntity>() {
        @Override
        public PointItemEntity createFromParcel(Parcel source) {
            return new PointItemEntity(source);
        }

        @Override
        public PointItemEntity[] newArray(int size) {
            return new PointItemEntity[size];
        }
    };
}
