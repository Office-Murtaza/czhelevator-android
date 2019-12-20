package com.kingyon.elevator.entities;

import java.util.ArrayList;

/**
 * Created By SongPeng  on 2019/12/20
 * Email : 1531603384@qq.com
 * 点击去下单时存放需要的参数
 */
public class GoPlaceAnOrderEntity {
    /**
     * 下单时选择的小区数据
     */
    private ArrayList<CellItemEntity> cellItemEntityArrayList;
    private Long startTime;
    private Long endTime;
    private String planType;

    public GoPlaceAnOrderEntity(ArrayList<CellItemEntity> cellItemEntityArrayList, Long startTime, Long endTime, String planType) {
        this.cellItemEntityArrayList = cellItemEntityArrayList;
        this.startTime = startTime;
        this.endTime = endTime;
        this.planType = planType;
    }

    public ArrayList<CellItemEntity> getCellItemEntityArrayList() {
        return cellItemEntityArrayList;
    }

    public void setCellItemEntityArrayList(ArrayList<CellItemEntity> cellItemEntityArrayList) {
        this.cellItemEntityArrayList = cellItemEntityArrayList;
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

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }
}
