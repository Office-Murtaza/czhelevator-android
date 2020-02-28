package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2020/2/28
 * Email : 1531603384@qq.com
 */
public class DianZanEntity {


    /**
     * id : 121603
     * newsId : 1
     * nickname : 项瑞青
     * createTime : 2020-02-28 11:01:36
     * coverUrl : http://cdn.tlwgz.com/FjicIz3YmP4R-D_HCe96awIQ0Tcv
     * read : false
     */

    private int id;
    private int newsId;
    private String nickname;
    private String createTime;
    private String coverUrl;
    private boolean read;
    private String photoUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
