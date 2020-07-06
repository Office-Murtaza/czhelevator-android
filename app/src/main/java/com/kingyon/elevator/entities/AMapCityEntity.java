package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by GongLi on 2018/12/26.
 * Email：lc824767150@163.com
 */

public class AMapCityEntity implements Parcelable {

    private String citycode;
    private String adcode;
    private String name;
    private String center;
    private String level;
    private String pingYin;
    private List<AMapCityEntity> districts;

    public AMapCityEntity(String name) {
        this.name = name;
    }

    public AMapCityEntity(String name, String adcode) {
        this.name = name;
        this.adcode = adcode;
    }
  public AMapCityEntity(String name, String center,String adcode) {
        this.name = name;
        this.center = center;
        this.adcode = adcode;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPingYin() {
        return pingYin;
    }

    public void setPingYin(String pingYin) {
        this.pingYin = pingYin;
    }

    public List<AMapCityEntity> getDistricts() {
        return districts;
    }

    public void setDistricts(List<AMapCityEntity> districts) {
        this.districts = districts;
    }

    public String getAcronyms() {
        String acronyms;
        if (!TextUtils.isEmpty(pingYin)) {
            acronyms = String.valueOf(pingYin.charAt(0)).toUpperCase();
        } else {
            acronyms = "#";
        }
        return acronyms;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.citycode);
        dest.writeString(this.adcode);
        dest.writeString(this.name);
        dest.writeString(this.center);
        dest.writeString(this.level);
        dest.writeString(this.pingYin);
        dest.writeTypedList(this.districts);
    }

    public AMapCityEntity() {
    }

    protected AMapCityEntity(Parcel in) {
        this.citycode = in.readString();
        this.adcode = in.readString();
        this.name = in.readString();
        this.center = in.readString();
        this.level = in.readString();
        this.pingYin = in.readString();
        this.districts = in.createTypedArrayList(AMapCityEntity.CREATOR);
    }

    public static final Creator<AMapCityEntity> CREATOR = new Creator<AMapCityEntity>() {
        @Override
        public AMapCityEntity createFromParcel(Parcel source) {
            return new AMapCityEntity(source);
        }

        @Override
        public AMapCityEntity[] newArray(int size) {
            return new AMapCityEntity[size];
        }
    };

}
