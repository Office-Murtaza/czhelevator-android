package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/2/26.
 * Email：lc824767150@163.com
 */

public class StateHolder {
    private int state;
    private int height;

    public StateHolder(int state, int height) {
        this.state = state;
        this.height = height;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
