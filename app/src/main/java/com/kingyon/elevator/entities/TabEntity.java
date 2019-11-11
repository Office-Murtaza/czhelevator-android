package com.kingyon.elevator.entities;

/**
 * Created by gongli on 2017/7/17 17:26
 * email: lc824767150@163.com
 */
public class TabEntity {
    private int pos;
    private boolean isExit;
    private NormalParamEntity orderType;
    private String planType;

    public TabEntity(int pos) {
        this.pos = pos;
    }

    public TabEntity(int pos, NormalParamEntity orderType) {
        this.pos = pos;
        this.orderType = orderType;
    }

    public TabEntity(boolean isExit) {
        this.isExit = isExit;
    }

    public TabEntity(int pos, String planType) {
        this.pos = pos;
        this.planType = planType;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    public NormalParamEntity getOrderType() {
        return orderType;
    }

    public void setOrderType(NormalParamEntity orderType) {
        this.orderType = orderType;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }
}
