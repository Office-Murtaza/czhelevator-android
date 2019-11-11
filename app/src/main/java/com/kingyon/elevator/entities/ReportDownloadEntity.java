package com.kingyon.elevator.entities;

/**
 * Created by GongLi on 2018/6/29.
 * Email：lc824767150@163.com
 */

public class ReportDownloadEntity {
    private String name;
    private int progress;

    public ReportDownloadEntity(String name, int progress) {
        this.name = name;
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
