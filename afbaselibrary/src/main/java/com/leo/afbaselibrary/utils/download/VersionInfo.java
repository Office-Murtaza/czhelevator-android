package com.leo.afbaselibrary.utils.download;

/**
 * Created by gongli on 2017/6/7 14:39
 * email: lc824767150@163.com
 */
public interface VersionInfo {

    String getVersionName();

    int getVersionCode();

    String getContent();

    String getUrl();

    long getUpdateTime();

    long getObjectId();

    boolean isMandatory();
}
