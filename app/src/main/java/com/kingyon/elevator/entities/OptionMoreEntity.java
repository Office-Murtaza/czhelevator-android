package com.kingyon.elevator.entities;


import com.kingyon.elevator.others.OnChoosedInterface;

/**
 * Created by GongLi on 2018/9/5.
 * Email：lc824767150@163.com
 */

public class OptionMoreEntity extends OnChoosedInterface {
    private String name;
    private int drawableRes;

    public OptionMoreEntity() {
    }

    public OptionMoreEntity(String name) {
        this.name = name;
    }

    public OptionMoreEntity(String name, int drawableRes) {
        this.name = name;
        this.drawableRes = drawableRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public void setDrawableRes(int drawableRes) {
        this.drawableRes = drawableRes;
    }

    @Override
    public String getStringName() {
        return name;
    }
}
