package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/2.
 * Email：lc824767150@163.com
 */

public class PlanCellDeleteEntity {
    private long planId;
    private String cellIds;

    public PlanCellDeleteEntity(long planId, String cellIds) {
        this.planId = planId;
        this.cellIds = cellIds;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getCellIds() {
        return cellIds;
    }

    public void setCellIds(String cellIds) {
        this.cellIds = cellIds;
    }
}
