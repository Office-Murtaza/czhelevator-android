package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2020/2/26
 * Email : 1531603384@qq.com
 */
public class NewsDetailsEntity {


    /**
     * id : 1
     * contentUrl : http://111.85.152.201:1510/promotion/news/in/1
     * shareUrl : http://111.85.152.201:1510/promotion/news/1
     * like : false
     */

    private int id;
    private String contentUrl;
    private String shareUrl;
    private boolean like;
    private String commentCount;
    private String title;
    private String summary;
    private String coverUrl;


    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
