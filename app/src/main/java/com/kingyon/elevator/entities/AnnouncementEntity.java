package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2018/12/25.
 * Email：lc824767150@163.com
 */

public class AnnouncementEntity {

    /**
     * objectId : 1
     * createTime : 1596566466565165
     * title : 公告
     * content : 公告描述
     */

    private long objectId;
    private long createTime;
    private String title;
    private String content;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
