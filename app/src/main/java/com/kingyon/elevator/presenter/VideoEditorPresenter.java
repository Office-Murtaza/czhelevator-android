package com.kingyon.elevator.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.utils.TimeUtil;
import com.kingyon.elevator.utils.utilstwo.FileUtil;
import com.kingyon.elevator.utils.utilstwo.FileUtils;
import com.kingyon.elevator.videocrop.EditVideoActivity;
import com.kingyon.elevator.videocrop.VideoClipUtils;
import com.kingyon.elevator.videocrop.VideoCropListener;
import com.kingyon.elevator.videocrop.VideoEditorActivity;
import com.kingyon.elevator.view.VideoEditorView;
import com.marvhong.videoeffect.FillMode;
import com.marvhong.videoeffect.composer.Mp4Composer;

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

    public void startCropVideo(Activity context, String videoPath, float startTime, float endTime,long cropTime) {

         Mp4Composer mMp4Composer;
        try {
            LogUtils.e("裁剪参数：", RuntimeUtils.selectVideoPath, videoPath, startTime, endTime);

            LogUtils.e((startTime/1000),(endTime/1000),cropTime,(int)(startTime/1000));

            float endTime1 = (endTime/1000)-(startTime/1000);
            if (isViewAttached()) {
                getView().showProgressDialog("视频裁剪中...", false);
            }
           new Mp4Composer(RuntimeUtils.selectVideoPath, videoPath)
                    // .rotation(Rotation.ROTATION_270)
                    .size(768, 1220)
                    .videoBitrate(2000000)
                    .fillMode(FillMode.PRESERVE_ASPECT_FIT)
                    .filter(null)
                    .mute(false)
                    .flipHorizontal(false)
                    .flipVertical(false)
                    .listener(new Mp4Composer.Listener() {
                        @Override
                        public void onProgress(final double progress) {
//                            Log.d("TAG", "filterVideo---onProgress: " + (int) (progress * 100));
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getView().showProgressDialog("视频裁剪中..."+ (int) (progress * 100)+"%", false);
                                }
                            });
                        }
                        @Override
                        public void onCompleted() {
                            if (isViewAttached()) {
                                getView().hideProgressDailog();
                                getView().cropVideoSuccess(videoPath);
                                LogUtils.d(videoPath);
                             }
                        }

                        @Override
                        public void onCanceled() {
//                        NormalProgressDialog.stopLoading();
                            LogUtils.e("取消");
                            getView().hideProgressDailog();
                        }

                        @Override
                        public void onFailed(Exception exception) {
                            Log.e("TAG", "filterVideo---onFailed()");
                            LogUtils.e("filterVideo---onFailed()");
                            if (isViewAttached()) {
                                getView().hideProgressDailog();
                                getView().cropVideoSuccess(videoPath);
                                LogUtils.d(videoPath);
                            }


                        }
                    })
                    .start();


//            EpVideo epVideo = new EpVideo(RuntimeUtils.selectVideoPath);
//            EpEditor.OutputOption outputOption = new EpEditor.OutputOption(videoPath);
//            epVideo.clip((int)(startTime/1000),15);
//            EpEditor.exec(epVideo, outputOption, new OnEditorListener() {
//                @Override
//                public void onSuccess() {
//                LogUtils.e( RuntimeUtils.selectVideoPath,videoPath);
//                    if (isViewAttached()) {
////                        getView().hideProgressDailog();
//                        getView().cropVideoSuccess(videoPath);
//                        LogUtils.d(videoPath);
//                    }
//                }
//                @Override
//                public void onFailure() {
//                    ToastUtils.showShort("视频裁剪失败,请重新尝试！");
//                    LogUtils.e("视频裁剪失败-----------------------");
//                    if (isViewAttached()) {
//                        getView().hideProgressDailog();
//                    }
//                }
//
//                @Override
//                public void onProgress(float progress) {
//                    //这里获取处理进度
//                    LogUtils.d("onProgress=="+progress);
//                }
//            });



//            VideoClipUtils.clip(context,RuntimeUtils.selectVideoPath, videoPath,
//                    startTime,
//                    endTime,
//                    new VideoCropListener() {
//                        @Override
//                        public void cropSuccess() {
//                            getView().hideProgressDailog();
//                            getView().cropVideoSuccess(videoPath);
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
