package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2018/8/14.
 * Email：lc824767150@163.com
 */

public class AdvertisionEntity {

    /**
     * link : https://www.baidu.com/
     * picture : http://img4.imgtn.bdimg.com/it/u=2695178228,3662036982&fm=27&gp=0.jpg
     * useable : true
     */

    private String link;
    private String picture;
    private boolean useable;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isUseable() {
        return useable;
    }

    public void setUseable(boolean useable) {
        this.useable = useable;
    }
}
