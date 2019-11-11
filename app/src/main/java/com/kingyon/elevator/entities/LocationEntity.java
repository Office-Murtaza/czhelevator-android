package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GongLi on 2018/12/26.
 * Email：lc824767150@163.com
 */

public class LocationEntity implements Parcelable {
    private String name;
    private String city;
    private String cityCode;
    private Double longitude;
    private Double latitude;

    public LocationEntity() {
    }

    public LocationEntity(String name, String city, String cityCode, Double longitude, Double latitude) {
        this.name = name;
        this.city = city;
        this.cityCode = cityCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.city);
        dest.writeString(this.cityCode);
        dest.writeValue(this.longitude);
        dest.writeValue(this.latitude);
    }

    protected LocationEntity(Parcel in) {
        this.name = in.readString();
        this.city = in.readString();
        this.cityCode = in.readString();
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<LocationEntity> CREATOR = new Creator<LocationEntity>() {
        @Override
        public LocationEntity createFromParcel(Parcel source) {
            return new LocationEntity(source);
        }

        @Override
        public LocationEntity[] newArray(int size) {
            return new LocationEntity[size];
        }
    };
}
