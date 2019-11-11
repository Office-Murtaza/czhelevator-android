package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by GongLi on 2017/11/7.
 * Email：lc824767150@163.com
 */

public class CityEntity implements Parcelable {


    /**
     * children : [{"children":[],"code":"110000","name":"北京市","pingYin":"beijingshi","shortCode":"bjs","type":"CITY"}]
     * code : 110000
     * name : 北京市
     * pingYin : beijingshi
     * shortCode : bjs
     * type : PROVINCE
     */

    private List<CityEntity> children;
    private String name;
    private String pingYin;
    private String shortCode;
    private String type;
    private Long id;

    public CityEntity(String name) {
        this.name = name;
    }

    public List<CityEntity> getChildren() {
        return children;
    }

    public void setChildren(List<CityEntity> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPingYin() {
        return pingYin;
    }

    public void setPingYin(String pingYin) {
        this.pingYin = pingYin;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public CityEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.children);
        dest.writeString(this.name);
        dest.writeString(this.pingYin);
        dest.writeString(this.shortCode);
        dest.writeString(this.type);
        dest.writeValue(this.id);
    }

    protected CityEntity(Parcel in) {
        this.children = in.createTypedArrayList(CityEntity.CREATOR);
        this.name = in.readString();
        this.pingYin = in.readString();
        this.shortCode = in.readString();
        this.type = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<CityEntity> CREATOR = new Creator<CityEntity>() {
        @Override
        public CityEntity createFromParcel(Parcel source) {
            return new CityEntity(source);
        }

        @Override
        public CityEntity[] newArray(int size) {
            return new CityEntity[size];
        }
    };
}
