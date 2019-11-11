package com.kingyon.elevator.entities;

import com.kingyon.elevator.others.OnChoosedInterface;

/**
 * Created by GongLi on 2018/12/25.
 * Email：lc824767150@163.com
 */

public class NormalOptionEntity extends OnChoosedInterface {

    /**
     * title : 如何下单
     * objectId : 100
     */

    protected String title;
    protected long objectId;

    public NormalOptionEntity() {
    }

    public NormalOptionEntity(String title, long objectId) {
        this.title = title;
        this.objectId = objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public String getStringName() {
        return title;
    }
}
