package com.kingyon.elevator.uis.activities.advertising;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.App;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.uis.widgets.video.MediaController;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.MusicUtils;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class AdPreviewActivity extends BaseSwipeBackActivity {
    @BindView(R.id.video_texture_view)
    PLVideoTextureView videoTextureView;
    @BindView(R.id.cover_image)
    ImageView coverImage;
    @BindView(R.id.loading_view)
    LinearLayout loadingView;
    @BindView(R.id.pause_view)
    ImageView pauseView;
    @BindView(R.id.controller_stop_play)
    ImageButton controllerStopPlay;
    @BindView(R.id.controller_current_time)
    TextView controllerCurrentTime;
    @BindView(R.id.controller_progress_bar)
    SeekBar controllerProgressBar;
    @BindView(R.id.controller_end_time)
    TextView controllerEndTime;
    @BindView(R.id.full_screen_image)
    ImageButton fullScreenImage;
    @BindView(R.id.media_controller)
    MediaController mediaController;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;
    @BindView(R.id.img_image)
    ImageView imgImage;
    @BindView(R.id.ll_incise)
    LinearLayout llIncise;

    private ADEntity entity;
    private boolean firstPlay = true;
    private boolean hasVideo;

    @Override
    protected String getTitleText() {
        entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        return "广告";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_ad_preview;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        switch (entity.getScreenType()) {
            case Constants.AD_SCREEN_TYPE.FULL_VIDEO:
                flVideo.setVisibility(View.VISIBLE);
                imgImage.setVisibility(View.GONE);
                hasVideo = true;
                initVideoPlay();
                GlideUtils.loadVideoFrame(this, entity.getVideoUrl(), coverImage);
                break;
            case Constants.AD_SCREEN_TYPE.FULL_IMAGE:
                flVideo.setVisibility(View.GONE);
                imgImage.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(this, entity.getImageUrl(), imgImage);
                break;
            case Constants.AD_SCREEN_TYPE.VIDEO_IMAGE:
                flVideo.setVisibility(View.VISIBLE);
                imgImage.setVisibility(View.VISIBLE);
                hasVideo = true;
                initVideoPlay();
                GlideUtils.loadVideoFrame(this, entity.getVideoUrl(), coverImage);
                GlideUtils.loadImage(this, entity.getImageUrl(), imgImage);
                break;
            default:
                flVideo.setVisibility(View.GONE);
                imgImage.setVisibility(View.GONE);
                break;
        }
    }

    private void initVideoPlay() {
        videoTextureView.setAVOptions(createAVOptions());
        videoTextureView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        videoTextureView.setBufferingIndicator(loadingView);
        videoTextureView.setMediaController(mediaController);
        videoTextureView.setOnInfoListener(new PLOnInfoListener() {
            @Override
            public void onInfo(int i, int i1) {
                if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
                    coverImage.setVisibility(View.GONE);
                    pauseView.setVisibility(View.GONE);
                }
            }
        });
        videoTextureView.setOnCompletionListener(new PLOnCompletionListener() {
            @Override
            public void onCompletion() {
                pauseView.setVisibility(View.VISIBLE);
            }
        });
        videoTextureView.setOnErrorListener(new PLOnErrorListener() {
            @Override
            public boolean onError(int i) {
                showToast(String.format("视频播放出错：%s", i));
                return false;
            }
        });
        mediaController.setPauseView(pauseView);
        mediaController.setMediaPauseId(R.drawable.img_loading_transparent);
        mediaController.setMediaPlayId(R.drawable.img_video_play);
        mediaController.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        videoTextureView.setLooping(true);
        coverImage.setVisibility(View.VISIBLE);
        videoTextureView.setVideoPath(entity.getVideoUrl());
    }

    public AVOptions createAVOptions() {
        AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_AUTO);
//        options.setInteger(AVOptions.KEY_PREFER_FORMAT, AVOptions.PREFER_FORMAT_MP4);
        // 打开重试次数，设置后若打开流地址失败，则会进行重试
        options.setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 5);
        boolean disableLog = App.getInstance().isDebug();
        options.setInteger(AVOptions.KEY_LOG_LEVEL, disableLog ? 5 : 0);
        return options;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (hasVideo && videoTextureView != null && videoTextureView.isPlaying()) {
            videoTextureView.pause();
            pauseView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(entity.getBgMusic()) && MusicUtils.getInstance().isPlaying(entity.getBgMusic())) {
            MusicUtils.getInstance().pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        videoTextureView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startPlay();
            }
        }, 100);
    }

    private void startPlay() {
        if (hasVideo && videoTextureView != null && !videoTextureView.isPlaying()) {
            firstPlay = false;
            mediaController.startPlay();
        }
        if (!TextUtils.isEmpty(entity.getBgMusic()) && !MusicUtils.getInstance().isPlaying(entity.getBgMusic())) {
            MusicUtils.getInstance().play(entity.getBgMusic());
        }
    }

    @Override
    protected void onDestroy() {
        if (videoTextureView != null) {
            videoTextureView.stopPlayback();
            videoTextureView = null;
        }
        if (!TextUtils.isEmpty(entity.getBgMusic())) {
            MusicUtils.getInstance().clear();
        }
        super.onDestroy();
    }

    @OnClick(R.id.fl_video)
    public void onViewClicked() {
        startPlay();
    }
}
