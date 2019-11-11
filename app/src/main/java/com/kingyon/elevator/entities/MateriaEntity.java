package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class MateriaEntity {

    /**
     * objectId : 123
     * type : video
     * path : http://
     * createTime : 1
     */

    private String name;
    private long objectId;
    private String type;
    private String path;
    private long createTime;
    private long duration;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
