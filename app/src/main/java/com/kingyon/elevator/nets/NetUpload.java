package com.kingyon.elevator.nets;

import android.content.Context;
import android.text.TextUtils;

import com.kingyon.elevator.application.App;
import com.kingyon.elevator.entities.UploadParamsEnitty;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by GongLi on 2018/1/5.
 * Email：lc824767150@163.com
 */

public class NetUpload {

    private NetApi netApi;
    private UploadManager uploadManager;

    NetUpload(NetApi netApi, UploadManager uploadManager) {
        this.netApi = netApi;
        this.uploadManager = uploadManager;
    }

    void uploadFile(final BaseActivity baseActivity, final byte[] bytes, final String suffix, final OnUploadCompletedListener onUploadCompletedListener) {
        Logger.d("开始上传 --> bytes");
        netApi.getUploadToken()
                .compose(baseActivity.<UploadParamsEnitty>bindLifeCycle())
                .subscribeOn(Schedulers.io())
                .subscribe(new CustomApiCallback<UploadParamsEnitty>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        uploadCallback(baseActivity, onUploadCompletedListener, null, ex);
                    }

                    @Override
                    public void onNext(UploadParamsEnitty enitty) {
                        if (enitty == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (TextUtils.isEmpty(enitty.getToken()) || TextUtils.isEmpty(enitty.getDomain())) {
                            throw new ResultException(9001, "图片上传返回参数有误");
                        }
                        uploadByQiNiu(baseActivity, enitty.getToken(), enitty.getDomain(), bytes, suffix, onUploadCompletedListener);
                    }
                });
    }

    void uploadFile(BaseActivity baseActivity, File file, OnUploadCompletedListener onUploadCompletedListener, boolean needComptess) {
        List<File> files = new ArrayList<>();
        files.add(file);
        uploadFiles(baseActivity, files, onUploadCompletedListener, needComptess);
    }

    void uploadFileNoActivity(Context context, File file, OnUploadCompletedListener onUploadCompletedListener, boolean needComptess) {
        List<File> files = new ArrayList<>();
        files.add(file);
        uploadFilesNoActivity(context, files, onUploadCompletedListener, needComptess);
    }

    void uploadFilesNoActivity(Context context, final List<File> files, final OnUploadCompletedListener onUploadCompletedListener, final boolean needCompress) {
        Logger.d("开始上传 --> " + files);
        netApi.getUploadToken()
                .subscribeOn(Schedulers.io())
                .subscribe(new CustomApiCallback<UploadParamsEnitty>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        uploadCallbackNoActivity(onUploadCompletedListener, null, ex);
                    }

                    @Override
                    public void onNext(UploadParamsEnitty enitty) {
                        if (files == null || files.size() < 1) {
                            throw new ResultException(205, "LuBanUtils compress image error");
                        }
                        if (needCompress) {
                            try {
                                List<File> dstFiles = new ArrayList<>();
                                for (File srcfile : files) {
                                    if (srcfile == null || !srcfile.exists() || srcfile.isDirectory() || srcfile.length() <= 0) {
                                        throw new ResultException(205, "LuBanUtils compress image error");
                                    }
                                    File dstFile = Luban.with(context).load(srcfile).get();
                                    dstFiles.add(dstFile);
                                }
                                uploadByQiNiuNoActivity(enitty.getToken(), enitty.getDomain(), dstFiles, onUploadCompletedListener);
                            } catch (IOException e) {
                                throw new ResultException(205, "LuBanUtils compress image error");
                            }
                        } else {
                            uploadByQiNiuNoActivity(enitty.getToken(), enitty.getDomain(), files, onUploadCompletedListener);
                        }
                    }
                });
    }

    void uploadFiles(final BaseActivity baseActivity, final List<File> files, final OnUploadCompletedListener onUploadCompletedListener, final boolean needCompress) {
        Logger.d("开始上传 --> " + files);
        netApi.getUploadToken()
                .compose(baseActivity.<UploadParamsEnitty>bindLifeCycle())
                .subscribeOn(Schedulers.io())
                .subscribe(new CustomApiCallback<UploadParamsEnitty>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        uploadCallback(baseActivity, onUploadCompletedListener, null, ex);
                    }

                    @Override
                    public void onNext(UploadParamsEnitty enitty) {
                        if (files == null || files.size() < 1) {
                            throw new ResultException(205, "LuBanUtils compress image error");
                        }
                        if (needCompress) {
                            try {
                                List<File> dstFiles = new ArrayList<>();
                                for (File srcfile : files) {
                                    if (srcfile == null || !srcfile.exists() || srcfile.isDirectory() || srcfile.length() <= 0) {
                                        throw new ResultException(205, "LuBanUtils compress image error");
                                    }
                                    File dstFile = Luban.with(baseActivity).load(srcfile).get();
                                    dstFiles.add(dstFile);
                                }
                                uploadByQiNiu(baseActivity, enitty.getToken(), enitty.getDomain(), dstFiles, onUploadCompletedListener);
                            } catch (IOException e) {
                                throw new ResultException(205, "LuBanUtils compress image error");
                            }
                        } else {
                            uploadByQiNiu(baseActivity, enitty.getToken(), enitty.getDomain(), files, onUploadCompletedListener);
                        }
                    }
                });
    }


    private void uploadCallbackNoActivity(final OnUploadCompletedListener onUploadCompletedListener, final List<String> images, final ApiException ex) {
        if (onUploadCompletedListener != null) {
            try {
                if (images != null && images.size() > 0) {
                    Collections.sort(images);
                    Logger.d("结束上传(成功) --> " + images);
                    onUploadCompletedListener.uploadSuccess(images);
                } else {
                    Logger.d("结束上传(失败) --> " + ex.getDisplayMessage());
                    onUploadCompletedListener.uploadFailed(ex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadCallback(BaseActivity baseActivity, final OnUploadCompletedListener onUploadCompletedListener, final List<String> images, final ApiException ex) {
        if (onUploadCompletedListener != null) {
            try {
                if (images != null && images.size() > 0) {
                    Collections.sort(images);
                    baseActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d("结束上传(成功) --> " + images);
                            onUploadCompletedListener.uploadSuccess(images);
                        }
                    });
                } else {
                    baseActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d("结束上传(失败) --> " + ex.getDisplayMessage());
                            onUploadCompletedListener.uploadFailed(ex);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadByQiNiu(final BaseActivity baseActivity, String token, final String domain, byte[] bytes, String suffix, final OnUploadCompletedListener onUploadCompletedListener) {
//        uploadManager.put(bytes, getUploadFileName(String.valueOf(UUID.randomUUID()), 0, suffix), token, new UpCompletionHandler() {
//            @Override
//            public void complete(String key, ResponseInfo info, JSONObject response) {
//                List<String> resultImages = new ArrayList<>();
//                if (info.isOK()) {
//                    try {
//                        resultImages.add(getDomain(domain) + response.getString("key"));
//                        uploadCallback(baseActivity, onUploadCompletedListener, resultImages, null);
//                    } catch (JSONException e) {
//                        Logger.d(e);
//                        uploadCallback(baseActivity, onUploadCompletedListener, resultImages, new ApiException(e, 9001, "数据解析错误"));
//                    }
//                } else {
//                    Throwable throwable = new Throwable(info.error);
//                    Logger.d(throwable);
//                    uploadCallback(baseActivity, onUploadCompletedListener, resultImages, new ApiException(throwable, 9001, info.error));
//                }
//            }
//        }, null);
        uploadManager.put(bytes, null, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                List<String> resultImages = new ArrayList<>();
                if (info.isOK()) {
                    try {
                        resultImages.add(getDomain(domain) + response.getString("key"));
                        uploadCallback(baseActivity, onUploadCompletedListener, resultImages, null);
                    } catch (JSONException e) {
                        Logger.d(e);
                        uploadCallback(baseActivity, onUploadCompletedListener, resultImages, new ApiException(e, 9001, "数据解析错误"));
                    }
                } else {
                    Throwable throwable = new Throwable(info.error);
                    Logger.d(throwable);
                    uploadCallback(baseActivity, onUploadCompletedListener, resultImages, new ApiException(throwable, 9001, info.error));
                }
            }
        }, null);
    }

    private String getDomain(String domain) {
        if (!TextUtils.isEmpty(domain) && !domain.endsWith("/")) {
            domain = domain + "/";
        }
        return domain;
    }

    private void uploadByQiNiu(final BaseActivity baseActivity, String token, final String domain, final List<File> files, final OnUploadCompletedListener onUploadCompletedListener) {
        final List<String> uploadResponse = new ArrayList<>();
        String uuid = String.valueOf(UUID.randomUUID());
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String key = getUploadFileName(uuid, i, file.getName());
//            if (file.getAbsolutePath().endsWith(".mov") && App.getInstance().isDebug()) {
//                Auth auth = Auth.create("poJCBSlskPagOYICmTWfeiuLCZb0RvVWdVw1x_Q7_test", "WopBDRH0E6wjnHGgT6Qb-534eRcx2eL-cN3qY7yV_test");
//                token = auth.uploadToken("adver", null, 3600, new StringMap()
//                        .putNotEmpty("persistentOps", "avthumb/mp4/vcodec/libx264"), true);
//            }
            uploadManager.put(file, null, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    if (info.isOK()) {
                        try {
                            uploadResponse.add(getDomain(domain) + response.getString("key"));
                            if (uploadResponse.size() == files.size()) {
                                uploadCallback(baseActivity, onUploadCompletedListener, uploadResponse, null);
                            }
                        } catch (JSONException e) {
                            uploadCallback(baseActivity, onUploadCompletedListener, uploadResponse, new ApiException(e, 9001, "数据解析错误"));
                        }
                    } else {
                        Throwable throwable = new Throwable(info.error);
                        uploadCallback(baseActivity, onUploadCompletedListener, uploadResponse, new ApiException(throwable, 9001, info.error));
                    }
                }
            }, null);
        }
    }


    private void uploadByQiNiuNoActivity(String token, final String domain, final List<File> files, final OnUploadCompletedListener onUploadCompletedListener) {
        final List<String> uploadResponse = new ArrayList<>();
        String uuid = String.valueOf(UUID.randomUUID());
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String key = getUploadFileName(uuid, i, file.getName());
//            if (file.getAbsolutePath().endsWith(".mov") && App.getInstance().isDebug()) {
//                Auth auth = Auth.create("poJCBSlskPagOYICmTWfeiuLCZb0RvVWdVw1x_Q7_test", "WopBDRH0E6wjnHGgT6Qb-534eRcx2eL-cN3qY7yV_test");
//                token = auth.uploadToken("adver", null, 3600, new StringMap()
//                        .putNotEmpty("persistentOps", "avthumb/mp4/vcodec/libx264"), true);
//            }
            uploadManager.put(file, null, token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    if (info.isOK()) {
                        try {
                            uploadResponse.add(getDomain(domain) + response.getString("key"));
                            if (uploadResponse.size() == files.size()) {
                                uploadCallbackNoActivity(onUploadCompletedListener, uploadResponse, null);
                            }
                        } catch (JSONException e) {
                            uploadCallbackNoActivity(onUploadCompletedListener, uploadResponse, new ApiException(e, 9001, "数据解析错误"));
                        }
                    } else {
                        Throwable throwable = new Throwable(info.error);
                        uploadCallbackNoActivity(onUploadCompletedListener, uploadResponse, new ApiException(throwable, 9001, info.error));
                    }
                }
            }, null);
        }
    }

    private String getUploadFileName(String uuid, int sort, String fileName) {
        String result;
        if (!TextUtils.isEmpty(fileName) && fileName.contains(".")) {
            result = CommonUtil.getTwoDigits(sort) + "-" + uuid + fileName.substring(fileName.indexOf("."));
        } else {
            result = CommonUtil.getTwoDigits(sort) + "-" + uuid;
        }
        return result;
    }

    String getUploadResultString(List<String> images) {
        String result;
        StringBuilder sb = new StringBuilder();
        if (images != null && images.size() > 0) {
            for (String item : images) {
                sb.append(item).append(",");
            }
        }
        if (sb.length() > 1) {
            result = sb.substring(0, sb.length() - 1);
        } else {
            result = "";
        }
        return result;
    }

    public interface OnUploadCompletedListener {
        void uploadSuccess(List<String> images);

        void uploadFailed(ApiException ex);
    }
}
