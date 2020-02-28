package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2020/2/28
 * Email : 1531603384@qq.com
 * 通知小助手消息实体
 */
public class MsgNoticeEntity {


    /**
     * id : 109731
     * title : 公告《关于“便民信息”版块用户投放使用说
     * content : null
     * type : OFFICIAL
     * typeChild :
     * extraId : 100006
     * time : 114天前
     * isRead : false
     * image : http://cdn.tlwgz.com//Fo9Px-2gwZ0_1ZSpHgPij5LPE65Q
     * link : https://mp.weixin.qq.com/s/IL5reBaj-h1TmZRHTfyMYg
     */

    private int id;
    private String title;
    private String content;
    private String type;
    private String typeChild;
    private int extraId;
    private String time;
    private boolean isRead;
    private String image;
    private String link;
    private long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeChild() {
        return typeChild;
    }

    public void setTypeChild(String typeChild) {
        this.typeChild = typeChild;
    }

    public int getExtraId() {
        return extraId;
    }

    public void setExtraId(int extraId) {
        this.extraId = extraId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
