/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.kingyon.elevator.utils;

import android.content.Context;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.kingyon.elevator.application.App;
import com.orhanobut.logger.Logger;

import java.io.File;

public class OCRUtil {

    private static OCRUtil ocrUtil;

    private boolean hasGotToken = false;

    private OCRUtil() {
    }

    public static OCRUtil getInstance() {
        if (ocrUtil == null) {
            ocrUtil = new OCRUtil();
        }
        return ocrUtil;
    }

    public File getFaceFile(Context context) {
        return new File(context.getFilesDir(), "face.jpg");
    }

    public File getBackFile(Context context) {
        return new File(context.getFilesDir(), "back.jpg");
    }

    /**
     * 以license文件方式初始化
     */
    public void initAccessToken(Context context) {
        OCR.getInstance(context).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Logger.e(String.format("licence方式获取token失败：%s", error.getMessage()));
            }
        }, App.getContext());
    }

    /**
     * 用明文ak，sk初始化
     */
    public void initAccessTokenWithAkSk(Context context) {
        OCR.getInstance(context).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Logger.e(String.format("AK，SK方式获取token失败：%s", error.getMessage()));
            }
        }, App.getContext(), "AK", "SK");
    }

    /**
     * 自定义license的文件路径和文件名称，以license文件方式初始化
     */
    public void initAccessTokenLicenseFile(Context context) {
        OCR.getInstance(context).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Logger.e(String.format("自定义文件路径licence方式获取token失败：%s", error.getMessage()));
            }
        }, "aip.license", App.getContext());
    }

    public boolean isHasGotToken() {
        return hasGotToken;
    }

    public void setHasGotToken(boolean hasGotToken) {
        this.hasGotToken = hasGotToken;
    }
}
