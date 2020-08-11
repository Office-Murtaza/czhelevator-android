package com.kingyon.elevator.presenter;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.VideoEditorView;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;

/**
 * Created By SongPeng  on 2019/12/12
 * Email : 1531603384@qq.com
 */
public class VideoEditorPresenter extends BasePresenter<VideoEditorView> {

    public VideoEditorPresenter(Context mContext) {
        super(mContext);
    }

    public void startCropVideo(Activity context, String videoPath, float startTime, float endTime) {
        try {
            LogUtils.e("裁剪参数：", RuntimeUtils.selectVideoPath, videoPath, startTime, endTime);

            LogUtils.e((startTime/1000),(endTime/1000));
            float endTime1 = (endTime/1000)-(startTime/1000);
            if (isViewAttached()) {
                getView().showProgressDialog("视频裁剪中...", false);
            }
            LogUtils.e(endTime1,endTime,startTime/1000,(int)(startTime/1000),(int)endTime1);
            EpVideo epVideo = new EpVideo(RuntimeUtils.selectVideoPath);
            EpEditor.OutputOption outputOption = new EpEditor.OutputOption(videoPath);
            epVideo.clip((startTime/1000),endTime1);
            EpEditor.exec(epVideo, outputOption, new OnEditorListener() {
                @Override
                public void onSuccess() {
                LogUtils.e( RuntimeUtils.selectVideoPath,videoPath);
                    if (isViewAttached()) {
//                        getView().hideProgressDailog();
                        getView().cropVideoSuccess(videoPath);
                        LogUtils.d(videoPath);
                    }
                }
                @Override
                public void onFailure() {
                    ToastUtils.showShort("视频裁剪失败,请重新尝试！");
                    LogUtils.e("视频裁剪失败-----------------------");
                    if (isViewAttached()) {
                        getView().hideProgressDailog();
                    }
                }

                @Override
                public void onProgress(float progress) {
                    //这里获取处理进度
                    LogUtils.d("onProgress=="+progress);
                }
            });
//            VideoClipUtils.clip(context,RuntimeUtils.selectVideoPath, videoPath,
//                    startTime,
//                    endTime,
//                    new VideoCropListener() {
//                        @Override
//                        public void cropSuccess() {
//
//
//                        }
//
//                        @Override
//                        public void cropError() {
//                            ToastUtils.showShort("视频裁剪失败,请重新尝试！");
//                            LogUtils.e("视频裁剪失败-----------------------");
//                            if (isViewAttached()) {
//                                getView().hideProgressDailog();
//                            }
//                        }
//                    });
        } catch (Exception e) {
            LogUtils.e("裁剪视频异常：" + e.toString());
            ToastUtils.showShort("视频裁剪失败,请重新尝试！");
            if (isViewAttached()) {
                getView().hideProgressDailog();
            }
        }
    }

}
