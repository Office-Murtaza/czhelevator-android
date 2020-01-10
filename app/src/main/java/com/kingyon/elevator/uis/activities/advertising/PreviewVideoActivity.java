package com.kingyon.elevator.uis.activities.advertising;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.App;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.customview.MyActionBar;
import com.kingyon.elevator.interfaces.OnItemClick;
import com.kingyon.elevator.uis.activities.order.ConfirmOrderActivity;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.MyStatusBarUtils;
import com.kingyon.elevator.utils.MyToastUtils;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 裁剪视频之后的预览界面
 */
public class PreviewVideoActivity extends AppCompatActivity {
    private String videoPath = "";
    private long videoTime = 0;
    VideoView video_view;
    @BindView(R.id.play_video)
    ImageView play_video;
    @BindView(R.id.my_action_bar)
    MyActionBar my_action_bar;
    @BindView(R.id.iv_preview_video_img)
    ImageView iv_preview_video_img;
    @BindView(R.id.tv_video_time)
    TextView tv_video_time;
    @BindView(R.id.video_view_container)
    RelativeLayout video_view_container;
    private Boolean isClickPlay = false;//是否点击了播放

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyStatusBarUtils.setStatusBar(this, "#ffffff");
        setContentView(R.layout.activity_preview_video);
        ButterKnife.bind(this);
        video_view = new VideoView(App.getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        video_view.setLayoutParams(layoutParams);
        video_view_container.addView(video_view);
        my_action_bar.setLeftIconClick(new OnItemClick() {
            @Override
            public void onItemClick(int position) {
                DialogUtils.getInstance().showBackTipsDialog(PreviewVideoActivity.this, new OnItemClick() {
                    @Override
                    public void onItemClick(int position) {
                        finish();
                    }
                });
            }
        });
        videoPath = getIntent().getStringExtra("videoPath");
        videoTime = getIntent().getLongExtra("videoTime", 0);
        if (videoPath.equals("")) {
            MyToastUtils.showShort("视频路径为空");
            finish();
            return;
        }
        video_view.setVideoPath(videoPath);
        video_view.setOnCompletionListener(mp -> {
            mp.seekTo(0);
            mp.start();
        });
        tv_video_time.setText("视频时长" + videoTime / 1000 + "s");
        GlideUtils.loadLocalFrame(this, videoPath, iv_preview_video_img);
        // video_view.start();
    }

    @OnClick({R.id.play_video, R.id.go_place_an_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_video:
                isClickPlay = true;
                iv_preview_video_img.setVisibility(View.GONE);
                play_video.setVisibility(View.GONE);
                video_view.start();
                break;
            case R.id.go_place_an_order:
                if (video_view.isPlaying()) {
                    video_view.pause();
                }
                video_view.postDelayed(() ->
                {
                    MyActivityUtils.goConfirmOrderActivity(PreviewVideoActivity.this,
                            Constants.FROM_TYPE.MEDIADATA, videoPath, Constants.Materia_Type.VIDEO);
                    finish();
                }, 300);
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (video_view.isPlaying()) {
            video_view.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (isClickPlay) {
            if (!video_view.isPlaying()) {
                video_view.postDelayed(() -> {
                    iv_preview_video_img.setVisibility(View.GONE);
                    play_video.setVisibility(View.GONE);
                    video_view.start();
                }, 200);
            }
        }
    }

    @Override
    protected void onDestroy() {
        video_view_container.removeAllViews();
        video_view.stopPlayback();
        video_view.setOnCompletionListener(null);
        video_view.setOnPreparedListener(null);
        video_view = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DialogUtils.getInstance().showBackTipsDialog(PreviewVideoActivity.this, new OnItemClick() {
            @Override
            public void onItemClick(int position) {
                if (video_view.isPlaying()) {
                    video_view.pause();
                }
                finish();
            }
        });
    }
}
