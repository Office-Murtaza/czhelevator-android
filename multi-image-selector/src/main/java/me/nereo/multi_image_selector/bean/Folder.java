package me.nereo.multi_image_selector.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * 文件夹
 * Created by Nereo on 2015/4/7.
 */
public class Folder {
    public String name;
    public String path;
    public Image cover;
    public List<Image> images;
    public int type;
    public boolean custom;

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
            Folder other = (Folder) o;
            return TextUtils.equals(other.path, path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
