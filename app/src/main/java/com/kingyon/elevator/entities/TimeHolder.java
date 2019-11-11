package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/2/19.
 * Email：lc824767150@163.com
 */

public class TimeHolder {
    private Long startTime;
    private Long endTime;

    public TimeHolder(Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
