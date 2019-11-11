package com.kingyon.elevator.entities;

import com.leo.afbaselibrary.utils.download.VersionInfo;

/**
 * Created by GongLi on 2017/9/14.
 * Email：lc824767150@163.com
 */

public class VersionEntity implements VersionInfo {

    /**
     * content : 优化用户体验修复了bug
     * objectId : 12
     * updateTime : 1493931905000
     * url :
     * versionCode : 2
     * versionNumber : 1.0.2
     */

    private boolean mandatory;
    private String content;
    private long objectId;
    private long updateTime;
    private String url;
    private int versionCode;
    private String versionName;

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    @Override
    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
