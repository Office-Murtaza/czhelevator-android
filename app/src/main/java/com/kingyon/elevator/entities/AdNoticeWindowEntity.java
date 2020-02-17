package com.kingyon.elevator.entities;

public class AdNoticeWindowEntity {


    /**
     * id : 1
     * position : HOME
     * showWay : 1
     * showType : 2
     * showContent : http://cdn.tlwgz.com/Picture/20200116/1579172206VvlOdtEw.jpg
     * link : false
     * linkUrl :
     * status : true
     * createAccount : null
     * createTime : 1581499993000
     * modifyAccount : null
     * modifyTime : null
     * isDelete : null
     */

    private int id;
    private String position;
    private int showWay;
    private int showType;
    private String showContent;
    private boolean link;
    private String linkUrl;
    private boolean status;
    private long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getShowWay() {
        return showWay;
    }

    public void setShowWay(int showWay) {
        this.showWay = showWay;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public String getShowContent() {
        return showContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public boolean isLink() {
        return link;
    }

    public void setLink(boolean link) {
        this.link = link;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

}
