package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.kingyon.elevator.others.OnChoosedInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GongLi on 2019/1/3.
 * Email：lc824767150@163.com
 */

public class IndustryEntity extends OnChoosedInterface implements Parcelable {

    /**
     * objectId : 1
     * name : 武术
     * type : child
     */

    private long objectId;
    private String name;
    private String type;
    private List<IndustryEntity> childs;
    private boolean isChoose;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    @Override
    public String toString() {
        return "IndustryEntity{" +
                "objectId=" + objectId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", childs=" + childs +
                ", isChoose=" + isChoose +
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<IndustryEntity> getChilds() {
        return childs;
    }

    public void setChilds(List<IndustryEntity> childs) {
        this.childs = childs;
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
        dest.writeLong(this.objectId);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeList(this.childs);
    }

    public IndustryEntity() {
    }

    protected IndustryEntity(Parcel in) {
        this.objectId = in.readLong();
        this.name = in.readString();
        this.type = in.readString();
        this.childs = new ArrayList<IndustryEntity>();
        in.readList(this.childs, IndustryEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<IndustryEntity> CREATOR = new Parcelable.Creator<IndustryEntity>() {
        @Override
        public IndustryEntity createFromParcel(Parcel source) {
            return new IndustryEntity(source);
        }

        @Override
        public IndustryEntity[] newArray(int size) {
            return new IndustryEntity[size];
        }
    };
}
