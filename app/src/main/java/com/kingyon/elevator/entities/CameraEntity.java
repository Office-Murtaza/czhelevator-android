package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GongLi on 2019/2/20.
 * Email：lc824767150@163.com
 */

public class CameraEntity implements Parcelable {
    private CameraBrandEntity brand;
    private String ipAddress;
    private long objectId;

    public CameraBrandEntity getBrand() {
        return brand;
    }

    public void setBrand(CameraBrandEntity brand) {
        this.brand = brand;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.brand, flags);
        dest.writeString(this.ipAddress);
        dest.writeLong(this.objectId);
    }

    public CameraEntity() {
    }

    protected CameraEntity(Parcel in) {
        this.brand = in.readParcelable(CameraBrandEntity.class.getClassLoader());
        this.ipAddress = in.readString();
        this.objectId = in.readLong();
    }

    public static final Parcelable.Creator<CameraEntity> CREATOR = new Parcelable.Creator<CameraEntity>() {
        @Override
        public CameraEntity createFromParcel(Parcel source) {
            return new CameraEntity(source);
        }

        @Override
        public CameraEntity[] newArray(int size) {
            return new CameraEntity[size];
        }
    };
}
