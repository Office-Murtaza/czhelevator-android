package com.kingyon.elevator.others;

/**
 * Created by GongLi on 2018/3/2.
 * Email：lc824767150@163.com
 */

public abstract class OnChoosedInterface {
    protected boolean choosed;

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }

    public abstract String getStringName();
}
