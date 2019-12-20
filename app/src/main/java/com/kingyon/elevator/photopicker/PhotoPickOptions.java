package com.kingyon.elevator.photopicker;

import android.os.Environment;

import java.io.File;

/**
 * @author:duyu
 * @org :   www.yudu233.com
 * @email : yudu233@gmail.com
 * @date :  2019/5/10 11:42
 * @filename : PhotoPickOptions
 * @describe :
 */
public class PhotoPickOptions {

    public static PhotoPickOptions DEFAULT = new PhotoPickOptions();

    /**
     * App文件保存路径
     */
    public String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "PhotoPick/";

    /**
     * App图片文件夹名称
     */
    public String imagePath = filePath + "image";


    /**
     * 适配Android7.0文件共享
     */
    public String photoPickAuthority = "com.kingyon.elevator.provider";



}
