package com.kingyon.elevator.utils;

import android.Manifest;
import android.os.Bundle;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import me.nereo.multi_image_selector.MultiFilterType;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by gongli on 2017/2/6 11:15
 * email: lc824767150@163.com
 */

public class PictureSelectorUtil {
    public static void showPictureSelector(final BaseActivity activity, final int requestCode) {
        showPictureSelector(activity, requestCode, 1);
    }

    public static void showPictureSelector(final BaseActivity activity, final int requestCode, final int maxCount) {
        if (maxCount < 1) {
            return;
        }
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        activity.checkPermission(new BaseActivity.CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                MultiImageSelector.create()
                        .filterType(MultiFilterType.IMAGE)
                        .showCamera(true)
                        .crop(true)
                        .cropProperty(1)
                        .count(maxCount)
                        .start(activity, requestCode);
            }
        }, "读取系统相册需要以下权限", perms);
    }

    public static void showPictureSelectorNoCrop(final BaseActivity activity, final int requestCode) {
        showPictureSelectorNoCrop(activity, requestCode, 1);
    }

    public static void showPictureSelectorNoCrop(final BaseActivity activity, final int requestCode, final int maxCount) {
        if (maxCount < 1) {
            return;
        }
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        activity.checkPermission(new BaseActivity.CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                MultiImageSelector.create()
                        .filterType(MultiFilterType.IMAGE)
                        .showCamera(true)
                        .crop(false)
                        .count(maxCount)
                        .start(activity, requestCode);
            }
        }, "读取系统相册需要以下权限", perms);
    }

    public static void showPictureSelectorCropProperty(final BaseActivity activity, final int requestCode, float property) {
        showPictureSelectorCropProperty(activity, requestCode, 1, property);
    }

    public static void showPictureSelectorCropProperty(final BaseActivity activity, final int requestCode, final int maxCount, final float property) {
        if (maxCount < 1) {
            return;
        }
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        activity.checkPermission(new BaseActivity.CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                MultiImageSelector.create()
                        .filterType(MultiFilterType.IMAGE)
                        .showCamera(true)
                        .crop(true)
                        .cropProperty(property)
                        .count(maxCount)
                        .start(activity, requestCode);
            }


        }, "读取系统相册需要以下权限", perms);
    }

    //    public static void clearAsync() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                PSConfigUtil.clearCache();
//            }
//        }).start();
//    }
//
    public static void clear() {
    }
//
//    public static long getCacheSize() {
//        return PSConfigUtil.getCacheSize();
//    }

    public static void showVideoSelector(final BaseActivity activity, final int requestCode) {
        showVideoSelector(activity, requestCode, 1);
    }

    public static void showVideoSelector(final BaseActivity activity, final int requestCode, final int maxCount) {
        if (maxCount < 1) {
            return;
        }
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        activity.checkPermission(new BaseActivity.CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                MultiImageSelector.create()
                        .filterType(MultiFilterType.VIDEO)
                        .showCamera(false)
                        .crop(false)
                        .count(maxCount)
                        .start(activity, requestCode);
            }


        }, "读取系统相册需要以下权限", perms);
    }

    public static void showAllSelector(final BaseActivity activity, final int requestCode) {
        showAllSelector(activity, requestCode, 1);
    }

    public static void showAllSelector(final BaseActivity activity, final int requestCode, final int maxCount) {
        if (maxCount < 1) {
            return;
        }
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        activity.checkPermission(new BaseActivity.CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                MultiImageSelector.create()
                        .filterType(MultiFilterType.ALL)
                        .showCamera(false)
                        .crop(false)
                        .count(maxCount)
                        .start(activity, requestCode);
            }


        }, "读取系统相册需要以下权限", perms);
    }

    public static void showCameraIdCardFace(final BaseActivity activity, final int requestCode) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        activity.checkPermission(new BaseActivity.CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                Bundle front = new Bundle();
                front.putString(CameraActivity.KEY_OUTPUT_FILE_PATH, OCRUtil.getInstance().getFaceFile(activity).getAbsolutePath());
                front.putBoolean(CameraActivity.KEY_NATIVE_ENABLE, true);
                front.putBoolean(CameraActivity.KEY_NATIVE_MANUAL, true);
                front.putString(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                activity.startActivityForResult(CameraActivity.class, CommonUtil.REQ_CODE_3, front);
            }
        }, "读取系统相册需要以下权限", perms);
    }

    public static void showCameraIdCardBack(final BaseActivity activity, final int requestCode) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        activity.checkPermission(new BaseActivity.CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                Bundle back = new Bundle();
                back.putString(CameraActivity.KEY_OUTPUT_FILE_PATH, OCRUtil.getInstance().getBackFile(activity).getAbsolutePath());
                back.putBoolean(CameraActivity.KEY_NATIVE_ENABLE, true);
                back.putBoolean(CameraActivity.KEY_NATIVE_MANUAL, true);
                back.putString(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                activity.startActivityForResult(CameraActivity.class, CommonUtil.REQ_CODE_3, back);
            }
        }, "读取系统相册需要以下权限", perms);
    }

}
