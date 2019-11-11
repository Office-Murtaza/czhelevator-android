package com.kingyon.elevator.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.kingyon.elevator.application.App;
import com.kingyon.elevator.entities.AdvertisionEntity;

import java.io.File;

/**
 * Created by GongLi on 2018/6/5.
 * Email：lc824767150@163.com
 */

public class PathUtils {

    private static String getAdvertisionDownloadPath(AdvertisionEntity entity) {
        return getResourceDownloadPath() + File.separator + "advertising" + File.separator + getAdvertisionFileName(entity.getPicture());
    }

    public static File getAdvertisionDownloadFile(AdvertisionEntity entity) {
        String advertisionFileName = getAdvertisionFileName(entity.getPicture());
        return TextUtils.equals(advertisionFileName, "_advertision") ? null : new File(getAdvertisionDownloadPath(entity));
    }

//    public static String getResourceDownloadPath(ResourceEntity entity) {
//        return getResourceDownloadPath() + File.separator + "voice" + File.separator + getFileName("_" + entity.getObjectId() + "_" + entity.getOwnerId(), entity.getDownUrl());
//    }
//
//    public static File getResourceDownloadFile(ResourceEntity entity) {
//        return new File(getResourceDownloadPath(entity));
//    }

    public static File getResourceDownloadPath() {
        return App.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    }

    private static String getAdvertisionFileName(String url) {
        String result;
        if (!TextUtils.isEmpty(url)) {
            int index1 = url.lastIndexOf("/");
            int index2 = url.lastIndexOf(".");
            if (index1 >= 0) {
                if (index1 >= index2) {
                    result = url.substring(index1);
                } else {
                    result = url.substring(index1, index2);
                }
            } else {
                result = "";
            }
        } else {
            result = "";
        }
        return String.format("%s_advertision", result);
    }
}
