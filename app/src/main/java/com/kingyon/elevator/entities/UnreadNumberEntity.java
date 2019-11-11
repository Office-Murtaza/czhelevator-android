package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class UnreadNumberEntity {

    /**
     * systemMessage : 120
     * reviewMessage : 230
     * officialMessage : 236
     */

    private long systemMessage;
    private long reviewMessage;
    private long officialMessage;

    public long getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(long systemMessage) {
        this.systemMessage = systemMessage;
    }

    public long getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(long reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public long getOfficialMessage() {
        return officialMessage;
    }

    public void setOfficialMessage(long officialMessage) {
        this.officialMessage = officialMessage;
    }

    public long getTotalUnread() {
        return systemMessage + reviewMessage + officialMessage;
    }
}
