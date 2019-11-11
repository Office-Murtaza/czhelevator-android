package com.kingyon.elevator.entities;

import java.util.List;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class FeedBackEntity {

    /**
     * objectId : 1
     * titile : 行业组名称
     * tag :
     * images : ["http://img.zcool.cn/community/0139735a90f9c0a8012192310823b3.jpg@1280w_1l_2o_100sh.jpg","http://img.zcool.cn/community/0139735a90f9c0a8012192310823b3.jpg@1280w_1l_2o_100sh.jpg"]
     */

    private long objectId;
    private String titile;
    private long time;
    private List<String> images;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
