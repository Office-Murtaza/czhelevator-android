package com.kingyon.elevator.entities;

import com.leo.afbaselibrary.nets.entities.PageListEntity;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class FeedBackCache {
    private FeedBackEntity feedBack;
    private PageListEntity<FeedBackMessageEntity> messagePage;

    public FeedBackCache(FeedBackEntity feedBack, PageListEntity<FeedBackMessageEntity> messagePage) {
        this.feedBack = feedBack;
        this.messagePage = messagePage;
    }

    public FeedBackEntity getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(FeedBackEntity feedBack) {
        this.feedBack = feedBack;
    }

    public PageListEntity<FeedBackMessageEntity> getMessagePage() {
        return messagePage;
    }

    public void setMessagePage(PageListEntity<FeedBackMessageEntity> messagePage) {
        this.messagePage = messagePage;
    }
}
