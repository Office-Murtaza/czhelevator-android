package com.kingyon.elevator.entities;

import com.kingyon.elevator.others.OnChoosedInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GongLi on 2018/12/29.
 * Email：lc824767150@163.com
 */

public class PlanItemEntity extends OnChoosedInterface {

    /**
     * objectId : 1
     * planName : 计划名称
     * planType : DIY
     * cells : []
     */

    private long createTime;
    private boolean deleteCache;
    private long objectId;
    private String planName;
    private String planType;
    private ArrayList<CellItemEntity> cells;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public ArrayList<CellItemEntity> getCells() {
        return cells;
    }

    public void setCells(ArrayList<CellItemEntity> cells) {
        this.cells = cells;
    }

    public boolean isDeleteCache() {
        return deleteCache;
    }

    public void setDeleteCache(boolean deleteCache) {
        this.deleteCache = deleteCache;
    }

    public void setAllCellsSameDelete() {
        List<CellItemEntity> cells = getCells();
        if (cells != null) {
            for (CellItemEntity cell : cells) {
                cell.setDeleteCache(deleteCache);
            }
        }
    }

    @Override
    public String getStringName() {
        return planName;
    }
}
