package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.kingyon.elevator.others.OnChoosedInterface;

/**
 * Created by GongLi on 2019/1/17.
 * Email：lc824767150@163.com
 */

public class NormalElemEntity extends OnChoosedInterface implements Parcelable {

    /**
     * objectId : 1
     * name : 楼栋名称
     */

    protected boolean canEdit;
    protected long objectId;
    protected String name;

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
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

    public NormalElemEntity() {
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getStringName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.canEdit ? (byte) 1 : (byte) 0);
        dest.writeLong(this.objectId);
        dest.writeString(this.name);
    }

    protected NormalElemEntity(Parcel in) {
        this.canEdit = in.readByte() != 0;
        this.objectId = in.readLong();
        this.name = in.readString();
    }

    public static final Creator<NormalElemEntity> CREATOR = new Creator<NormalElemEntity>() {
        @Override
        public NormalElemEntity createFromParcel(Parcel source) {
            return new NormalElemEntity(source);
        }

        @Override
        public NormalElemEntity[] newArray(int size) {
            return new NormalElemEntity[size];
        }
    };
}
