package com.kingyon.elevator.entities;

import com.gerry.scaledelete.ScanleImageUrl;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class ImageScan implements ScanleImageUrl {
    private String url;

    public ImageScan(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getImageUrl() {
        return url;
    }

    @Override
    public boolean isVideo() {
        return false;
    }
}
