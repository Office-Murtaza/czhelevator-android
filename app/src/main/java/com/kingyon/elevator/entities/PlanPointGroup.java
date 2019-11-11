package com.kingyon.elevator.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GongLi on 2019/1/2.
 * Email：lc824767150@163.com
 */

public class PlanPointGroup {
    private String key;
    private String cellName;
    private String buildName;
    private long cellId;
    private long buildId;
    private boolean choosed;
    private boolean expand;
    private List<PointItemEntity> points;

    public PlanPointGroup(String key) {
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

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public long getCellId() {
        return cellId;
    }

    public void setCellId(long cellId) {
        this.cellId = cellId;
    }

    public long getBuildId() {
        return buildId;
    }

    public void setBuildId(long buildId) {
        this.buildId = buildId;
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
