package com.kingyon.elevator.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.videocrop.VideoClipUtils;
import com.kingyon.elevator.videocrop.VideoCropListener;
import com.kingyon.elevator.view.VideoEditorView;

/**
 * Created By SongPeng  on 2019/12/12
 * Email : 1531603384@qq.com
 */
public class VideoEditorPresenter extends BasePresenter<VideoEditorView> {

    public VideoEditorPresenter(Context mContext) {
        super(mContext);
    }

    public void startCropVideo(Activity context, String videoPath, long startTime, long endTime) {
        try {
            LogUtils.e("裁剪参数：", RuntimeUtils.selectVideoPath, videoPath, startTime, endTime);
            if (isViewAttached()) {
                getView().showProgressDialog("视频裁剪中...", false);
            }
            VideoClipUtils.clip(context,RuntimeUtils.selectVideoPath, videoPath,
                    startTime,
                    endTime,
                    new VideoCropListener() {
                        @Override
                        public void cropSuccess() {
                            ToastUtils.showShort("视频裁剪完成");
                            if (isViewAttached()) {
                                getView().hideProgressDailog();
                                getView().cropVideoSuccess(videoPath);
                                LogUtils.d(videoPath);
                            }
                        }

                        @Override
                        public void cropError() {
                            ToastUtils.showShort("视频裁剪失败,请重新尝试！");
                            LogUtils.e("视频裁剪失败-----------------------");
                            if (isViewAttached()) {
                                getView().hideProgressDailog();
                            }
                        }
                    });
        } catch (Exception e) {
            LogUtils.e("裁剪视频异常：" + e.toString());
            ToastUtils.showShort("视频裁剪失败,请重新尝试！");
            if (isViewAttached()) {
                getView().hideProgressDailog();
            }
        }
    }

}
