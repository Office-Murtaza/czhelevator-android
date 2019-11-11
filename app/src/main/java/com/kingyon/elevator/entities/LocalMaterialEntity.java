package com.kingyon.elevator.entities;

import org.litepal.crud.DataSupport;

/**
 * Created by GongLi on 2019/3/19.
 * Email：lc824767150@163.com
 */
public class LocalMaterialEntity extends DataSupport {


    /**
     * objectId : 123
     * type : video
     * path : http://demo-videos.qnsdk.com/shortvideo/%E7%8B%97.mp4
     * createTime : 1547517600000
     * duration : 100000
     * name : 图片/视频名称
     */

    private long userId;

    private String planType;
    private String screenType;

    private long adId;
    private String type;
    private String url;
    private String path;
    private long createTime;
    private long duration;
    private String name;

    private String extendA;
    private String extendB;
    private String extendC;
    private String extendD;
    private String extendE;

    private boolean delete;

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public LocalMaterialEntity() {
        this.extendA = "";
        this.extendB = "";
        this.extendC = "";
        this.extendD = "";
        this.extendE = "";
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public long getAdId() {
        return adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtendA() {
        return extendA;
    }

    public void setExtendA(String extendA) {
        this.extendA = extendA;
    }

    public String getExtendB() {
        return extendB;
    }

    public void setExtendB(String extendB) {
        this.extendB = extendB;
    }

    public String getExtendC() {
        return extendC;
    }

    public void setExtendC(String extendC) {
        this.extendC = extendC;
    }

    public String getExtendD() {
        return extendD;
    }

    public void setExtendD(String extendD) {
        this.extendD = extendD;
    }

    public String getExtendE() {
        return extendE;
    }

    public void setExtendE(String extendE) {
        this.extendE = extendE;
    }
}
