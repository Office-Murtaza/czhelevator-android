package com.kingyon.elevator.uis.activities.advertising;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.MusicUtils;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class AdPreviewActivity extends BaseSwipeBackActivity {
    @BindView(R.id.fl_video)
    FrameLayout flVideo;
    @BindView(R.id.img_image)
    ImageView imgImage;
    @BindView(R.id.ll_incise)
    LinearLayout llIncise;
    @BindView(R.id.video_view)
    StandardGSYVideoPlayer video_view;


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
                GlideUtils.loadImage(this, entity.getImageUrl(), imgImage);
                break;
            default:
                flVideo.setVisibility(View.GONE);
                imgImage.setVisibility(View.GONE);
                break;
        }
    }

    private void initVideoPlay() {
        video_view.setUp(entity.getVideoUrl(), true, "");
        video_view.getBackButton().setVisibility(View.GONE);
        video_view.getFullscreenButton().setVisibility(View.GONE);
        video_view.startPlayLogic();
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
        if (!TextUtils.isEmpty(entity.getBgMusic()) && MusicUtils.getInstance().isPlaying(entity.getBgMusic())) {
            MusicUtils.getInstance().pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    private void startPlay() {
        if (!TextUtils.isEmpty(entity.getBgMusic()) && !MusicUtils.getInstance().isPlaying(entity.getBgMusic())) {
            MusicUtils.getInstance().play(entity.getBgMusic());
        }
    }

    @Override
    protected void onDestroy() {
        GSYVideoManager.releaseAllVideos();
        if (!TextUtils.isEmpty(entity.getBgMusic())) {
            MusicUtils.getInstance().clear();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @OnClick(R.id.fl_video)
    public void onViewClicked() {
        startPlay();
    }
}
