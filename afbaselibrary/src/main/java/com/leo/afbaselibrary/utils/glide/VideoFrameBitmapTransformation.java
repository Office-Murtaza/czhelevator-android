package com.leo.afbaselibrary.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by GongLi on 2018/9/7.
 * Email：lc824767150@163.com
 */

public class VideoFrameBitmapTransformation extends BitmapTransformation {

    private long timeMillis;
    private String link;

    public VideoFrameBitmapTransformation(String link, long timeMillis) {
        this.link = link;
        this.timeMillis = timeMillis;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return toTransform;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        try {
            messageDigest.update(String.format("VideoTransform_%s_%s", link, timeMillis).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
