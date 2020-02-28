package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2020/2/28
 * Email : 1531603384@qq.com
 */
public class MsgCommentEntity {


    /**
     * id : 121615
     * comment : 哈哈哈
     * newsId : 3
     * nickname : 郑波波
     * createTime : 2020-02-28 13:09:06
     * photoUrl : https://thirdwx.qlogo.cn/mmopen/vi_32/vKMlkWnKWfwy0zXNhyGzpiaINlLbLdTcZZtbU0JZv8Wgf5XicBPXD1USbTbY5uCNCENWuib7PBica29r7touicnfGgw/132
     * coverUrl : http://scitech.people.com.cn/NMediaFile/2020/0226/MAIN202002261819000551538481417.jpg
     * read : false
     */

    private int id;
    private String comment;
    private int newsId;
    private String nickname;
    private String createTime;
    private String photoUrl;
    private String coverUrl;
    private boolean read;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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
}
