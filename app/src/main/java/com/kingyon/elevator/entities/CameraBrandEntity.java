package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GongLi on 2019/2/20.
 * Email：lc824767150@163.com
 */

public class CameraBrandEntity implements Parcelable {
    private long objectId;
    private String name;

    @Override
    public String toString() {
        return "CameraBrandEntity{" +
                "objectId=" + objectId +
                ", name='" + name + '\'' +
                '}';
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.objectId);
        dest.writeString(this.name);
    }

    public CameraBrandEntity() {
    }

    protected CameraBrandEntity(Parcel in) {
        this.objectId = in.readLong();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<CameraBrandEntity> CREATOR = new Parcelable.Creator<CameraBrandEntity>() {
        @Override
        public CameraBrandEntity createFromParcel(Parcel source) {
            return new CameraBrandEntity(source);
        }

        @Override
        public CameraBrandEntity[] newArray(int size) {
            return new CameraBrandEntity[size];
        }
    };
}
