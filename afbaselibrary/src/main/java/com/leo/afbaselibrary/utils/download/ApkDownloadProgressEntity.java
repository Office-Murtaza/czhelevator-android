package com.leo.afbaselibrary.utils.download;

/**
 * Created by gongli on 2017/1/19 11:39
 * email: lc824767150@163.com
 */
public class ApkDownloadProgressEntity {
    private int type;
    private int progress;

    public ApkDownloadProgressEntity(int type, int progress) {
        this.progress = progress;
        this.type = type;
    }

    public ApkDownloadProgressEntity() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
