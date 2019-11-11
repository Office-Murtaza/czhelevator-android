package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.kingyon.elevator.others.OnChoosedInterface;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class NormalParamEntity extends OnChoosedInterface implements Parcelable {
    private String type;
    private String name;

    public NormalParamEntity(String type, String name) {
        this.type = type;
        this.name = name;
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
        dest.writeString(this.type);
        dest.writeString(this.name);
    }

    protected NormalParamEntity(Parcel in) {
        this.type = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<NormalParamEntity> CREATOR = new Parcelable.Creator<NormalParamEntity>() {
        @Override
        public NormalParamEntity createFromParcel(Parcel source) {
            return new NormalParamEntity(source);
        }

        @Override
        public NormalParamEntity[] newArray(int size) {
            return new NormalParamEntity[size];
        }
    };
}
