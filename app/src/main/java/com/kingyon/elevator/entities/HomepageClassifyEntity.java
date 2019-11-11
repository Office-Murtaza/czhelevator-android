package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2018/12/25.
 * Email：lc824767150@163.com
 */

public class HomepageClassifyEntity extends NormalOptionEntity {
    private int resIcon;
    private String link;

    public HomepageClassifyEntity(String title, int resIcon, long objectId) {
        super(title, objectId);
        this.resIcon = resIcon;
    }

    public HomepageClassifyEntity(String title, int resIcon, String link, long objectId) {
        super(title, objectId);
        this.resIcon = resIcon;
        this.link = link;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
