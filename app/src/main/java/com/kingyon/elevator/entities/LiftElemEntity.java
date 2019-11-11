package com.kingyon.elevator.entities;

import android.os.Parcel;

/**
 * Created by GongLi on 2019/1/17.
 * Email：lc824767150@163.com
 */

public class LiftElemEntity extends NormalElemEntity {

    /**
     * objectId : 1
     * name : 楼栋名称
     * liftNo :
     * highest : 16
     * lowest : -2
     */

    private String liftNo;
    private Integer highest;
    private Integer lowest;
    private Integer base;
    private CameraEntity camera;

    public String getLiftNo() {
        return liftNo;
    }

    public void setLiftNo(String liftNo) {
        this.liftNo = liftNo;
    }

    public Integer getHighest() {
        return highest;
    }

    public void setHighest(Integer highest) {
        this.highest = highest;
    }

    public Integer getLowest() {
        return lowest;
    }

    public void setLowest(Integer lowest) {
        this.lowest = lowest;
    }

    public Integer getBase() {
        return base;
    }

    public void setBase(Integer base) {
        this.base = base;
    }

    public CameraEntity getCamera() {
        return camera;
    }

    public void setCamera(CameraEntity camera) {
        this.camera = camera;
    }

    public LiftElemEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.liftNo);
        dest.writeValue(this.highest);
        dest.writeValue(this.lowest);
        dest.writeValue(this.base);
        dest.writeParcelable(this.camera, flags);
    }

    protected LiftElemEntity(Parcel in) {
        super(in);
        this.liftNo = in.readString();
        this.highest = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lowest = (Integer) in.readValue(Integer.class.getClassLoader());
        this.base = (Integer) in.readValue(Integer.class.getClassLoader());
        this.camera = in.readParcelable(CameraEntity.class.getClassLoader());
    }

    public static final Creator<LiftElemEntity> CREATOR = new Creator<LiftElemEntity>() {
        @Override
        public LiftElemEntity createFromParcel(Parcel source) {
            return new LiftElemEntity(source);
        }

        @Override
        public LiftElemEntity[] newArray(int size) {
            return new LiftElemEntity[size];
        }
    };
}
