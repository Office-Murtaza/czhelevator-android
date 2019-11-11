package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2018/12/29.
 * Email：lc824767150@163.com
 */

public class LatLonCache {
    private double latitude;
    private double longitude;

    public LatLonCache(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
