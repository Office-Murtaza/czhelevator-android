package com.kingyon.elevator.entities;

import com.gerry.scaledelete.ScanleImageUrl;

/**
 * Created by GongLi on 2019/1/4.
 * Email：lc824767150@163.com
 */

public class AdDetectingEntity implements ScanleImageUrl {

    /**
     * objectId : 1
     * createTime : 1596566466565165
     * imgPath : http://img.zcool.cn/community/0139735a90f9c0a8012192310823b3.jpg@1280w_1l_2o_100sh.jpg
     * name : 1楼1号监播表
     */

    private long objectId;
    private long createTime;
    private String imgPath;
    private String name;

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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getImageUrl() {
        return imgPath;
    }

    @Override
    public boolean isVideo() {
        return false;
    }
}
