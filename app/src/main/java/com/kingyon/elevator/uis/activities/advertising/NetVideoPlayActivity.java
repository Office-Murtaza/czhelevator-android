package com.kingyon.elevator.uis.activities.advertising;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.MusicUtils;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;

/**
 * Created by GongLi on 2019/1/22.
 * Email：lc824767150@163.com
 */

public class NetVideoPlayActivity extends BaseSwipeBackActivity {
    @BindView(R.id.video_view)
    StandardGSYVideoPlayer video_view;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;

    private String url;

    @Override
    protected String getTitleText() {
        url = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        LogUtils.e(url);
        return "视频播放";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_net_video_play;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initVideoPlay();
    }

    private void initVideoPlay() {
//        videoTextureView.setAVOptions(createAVOptions());
//        videoTextureView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
//        videoTextureView.setBufferingIndicator(loadingView);
////        videoTextureView.setMediaController(mediaController);
//        videoTextureView.setOnInfoListener(new PLOnInfoListener() {
//            @Override
//            public void onInfo(int i, int i1) {
//                if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
//                    coverImage.setVisibility(View.GONE);
//                    pauseView.setVisibility(View.GONE);
//                }
//            }
//        });
//        videoTextureView.setOnCompletionListener(new PLOnCompletionListener() {
//            @Override
//            public void onCompletion() {
//                pauseView.setVisibility(View.VISIBLE);
//            }
//        });
//        videoTextureView.setOnErrorListener(new PLOnErrorListener() {
//            @Override
//            public boolean onError(int i) {
//                showToast(String.format("视频播放出错：%s", i));
//                return false;
//            }
//        });
//        mediaController.setPauseView(pauseView);
//        mediaController.setMediaPauseId(R.drawable.img_loading_transparent);
//        mediaController.setMediaPlayId(R.drawable.img_video_play);
//        mediaController.setVisibility(View.GONE);
//        loadingView.setVisibility(View.GONE);
//        videoTextureView.setLooping(true);
//        coverImage.setVisibility(View.VISIBLE);
//        videoTextureView.setVideoPath(url);
        video_view.setUp(url, true, "");
        video_view.getBackButton().setVisibility(View.GONE);
        video_view.getFullscreenButton().setVisibility(View.GONE);
        video_view.startPlayLogic();
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    private void startPlay() {
//        if (videoTextureView != null && !videoTextureView.isPlaying()) {
//            videoTextureView.start();
//        }
    }

    @Override
    protected void onDestroy() {
//        if (videoTextureView != null) {
//            videoTextureView.stopPlayback();
//            videoTextureView = null;
//        }
        GSYVideoManager.releaseAllVideos();
        super.onDestroy();
    }

//    public AVOptions createAVOptions() {
//        AVOptions options = new AVOptions();
//        // the unit of timeout is ms
//        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
//        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
//        // 1 -> hw codec enable, 0 -> disable [recommended]
//        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_AUTO);
////        options.setInteger(AVOptions.KEY_PREFER_FORMAT, AVOptions.PREFER_FORMAT_MP4);
//        // 打开重试次数，设置后若打开流地址失败，则会进行重试
//        options.setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 5);
//        boolean disableLog = App.getInstance().isDebug();
//        options.setInteger(AVOptions.KEY_LOG_LEVEL, disableLog ? 5 : 0);
//        return options;
//    }
}
