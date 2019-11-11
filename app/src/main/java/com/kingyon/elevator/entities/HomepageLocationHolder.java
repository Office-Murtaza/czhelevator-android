package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class HomepageLocationHolder {
    private LocationEntity location;

    public HomepageLocationHolder(LocationEntity location) {
        this.location = location;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }
}
