package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2018/12/25.
 * Email：lc824767150@163.com
 */

public class BannerEntity {

    /**
     * objectId : 1
     * imageUrl : http://img.zcool.cn/community/0139735a90f9c0a8012192310823b3.jpg@1280w_1l_2o_100sh.jpg
     * type : link
     * link : 1幢
     * jumpId : 120
     */

    private long objectId;
    private String imageUrl;
    private String type;
    private String link;
    private long jumpId;
    private ADEntity example;
    private NormalOptionEntity baike;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getJumpId() {
        return jumpId;
    }

    public void setJumpId(long jumpId) {
        this.jumpId = jumpId;
    }

    @Override
    public String toString() {
        return imageUrl;
    }

    public ADEntity getExample() {
        return example;
    }

    public void setExample(ADEntity example) {
        this.example = example;
    }

    public NormalOptionEntity getBaike() {
        return baike;
    }

    public void setBaike(NormalOptionEntity baike) {
        this.baike = baike;
    }
}
