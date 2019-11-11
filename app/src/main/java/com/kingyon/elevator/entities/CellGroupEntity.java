package com.kingyon.elevator.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class CellGroupEntity {
    private String key;
    private String cellName;
    private long cellId;
    private boolean choosed;
    private boolean expand;
    private List<PointItemEntity> points;

    public CellGroupEntity(String key) {
        this.key = key;
        this.points = new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public long getCellId() {
        return cellId;
    }

    public void setCellId(long cellId) {
        this.cellId = cellId;
    }

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public List<PointItemEntity> getPoints() {
        return points;
    }

    public void setPoints(List<PointItemEntity> points) {
        this.points = points;
    }

    public void addPoint(PointItemEntity entity) {
        points.add(entity);
    }
}
