package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2018/12/28.
 * Email：lc824767150@163.com
 */

public class CityCellEntity {

    /**
     * cityName : 成都市
     * longitude : 104.07
     * latitude : 30.67
     * cellCount : 5000
     *
     */

    private long cityCode;
    private String cityName;
    private double longitude;
    private double latitude;
    private long cellCount;

    @Override
    public String toString() {
        return "CityCellEntity{" +
                "cityCode=" + cityCode +
                ", cityName='" + cityName + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", cellCount=" + cellCount +
                '}';
    }

    public long getCityCode() {
        return cityCode;
    }

    public void setCityCode(long cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getCellCount() {
        return cellCount;
    }

    public void setCellCount(long cellCount) {
        this.cellCount = cellCount;
    }
}
