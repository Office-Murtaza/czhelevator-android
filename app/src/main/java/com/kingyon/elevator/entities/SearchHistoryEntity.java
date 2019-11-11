package com.kingyon.elevator.entities;

import org.litepal.crud.DataSupport;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class SearchHistoryEntity extends DataSupport {
    private String keyWord;
    private long latestTime;
    private long ownerId;

    public SearchHistoryEntity(String keyWord, long latestTime, long ownerId) {
        this.keyWord = keyWord;
        this.latestTime = latestTime;
        this.ownerId = ownerId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public long getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(long latestTime) {
        this.latestTime = latestTime;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return keyWord;
    }
}
