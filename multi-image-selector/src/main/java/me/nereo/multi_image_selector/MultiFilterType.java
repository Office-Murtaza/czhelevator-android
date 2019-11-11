package me.nereo.multi_image_selector;

import android.provider.MediaStore;

/**
 * Created by GongLi on 2018/10/23.
 * Email：lc824767150@163.com
 */

public interface MultiFilterType {
    int IMAGE = 0;
    int VIDEO = 1;
    int ALL = 2;
    String[] IMAGE_TYPE = new String[]{"image/jpeg", "image/png"};
    String[] VIDEO_TYPE = new String[]{"video/mp4", "video/x-ms-wmv", "video/x-flv", "video/3gpp", "video/x-msvideo", "video/quicktime"};
    String[] ALL_TYPE = new String[]{"image/jpeg", "image/png", "video/mp4", "video/x-ms-wmv", "video/x-flv", "video/3gpp", "video/x-msvideo", "video/quicktime"};
}
