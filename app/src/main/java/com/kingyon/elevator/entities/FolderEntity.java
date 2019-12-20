package com.kingyon.elevator.entities;

import android.text.TextUtils;

import java.util.List;

import me.nereo.multi_image_selector.bean.Image;

/**
 * Created By SongPeng  on 2019/12/17
 * Email : 1531603384@qq.com
 */
public class FolderEntity {

    public String name;
    public String path;
    public Image cover;
    public List<Image> images;
    public int type;
    public boolean custom;
    /**
     * 图片- 宽
     */
    private int imageWidth;

    /**
     * 图片 - 高
     */
    private int imageHeight;

    /**
     * 文件大小
     */
    private long mediaSize;

    /**
     * 媒体文件类型
     */
    private int mimeType;

    /**
     * 图片类型
     */
    private String imageType;

    /**
     * 媒体文件时长（音视频）
     */
    private long duration;


    public boolean hasVideo() {
        boolean result = false;
        if (images != null) {
            for (Image image : images) {
                if (image.video) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public boolean hasImage() {
        boolean result = false;
        if (images != null) {
            for (Image image : images) {
                if (!image.video) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        try {
            FolderEntity other = (FolderEntity) o;
            return TextUtils.equals(other.path, path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Image getCover() {
        return cover;
    }

    public void setCover(Image cover) {
        this.cover = cover;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public long getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(long mediaSize) {
        this.mediaSize = mediaSize;
    }

    public int getMimeType() {
        return mimeType;
    }

    public void setMimeType(int mimeType) {
        this.mimeType = mimeType;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
