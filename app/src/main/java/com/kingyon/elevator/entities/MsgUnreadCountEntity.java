package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2020/2/28
 * Email : 1531603384@qq.com
 */
public class MsgUnreadCountEntity {

    private int notice;
    private int review;
    private int userComment;
    private int userLike;

    public int getNotice() {
        return notice;
    }

    public void setNotice(int notice) {
        this.notice = notice;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public int getUserComment() {
        return userComment;
    }

    public void setUserComment(int userComment) {
        this.userComment = userComment;
    }

    public int getUserLike() {
        return userLike;
    }

    public void setUserLike(int userLike) {
        this.userLike = userLike;
    }
}
