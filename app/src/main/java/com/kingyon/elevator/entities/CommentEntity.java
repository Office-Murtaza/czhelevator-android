package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2020/2/25
 * Email : 1531603384@qq.com
 */
public class CommentEntity {


    /**
     * id : 1
     * comment : 真棒
     * nickname : 万吉相
     * photoUrl : http://cdn.tlwgz.com/FmnwwzRGGh4yyaFlzzvdnw9qoicw
     * replyName : null
     * replyUrl : null
     * comCount : 1
     * like : true
     */

    private int id;
    private String comment;
    private String nickname;
    private String photoUrl;
    private String replyName;
    private String replyUrl;
    private int comCount;
    private boolean like;
    private String showTime;

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public String getReplyUrl() {
        return replyUrl;
    }

    public void setReplyUrl(String replyUrl) {
        this.replyUrl = replyUrl;
    }

    public int getComCount() {
        return comCount;
    }

    public void setComCount(int comCount) {
        this.comCount = comCount;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
