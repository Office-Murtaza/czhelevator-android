package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.bean.VideoEditInfo;
import com.czh.myversiontwo.uiutils.DeviceUtils;
import com.czh.myversiontwo.uiutils.ExtractFrameWorkThread;
import com.czh.myversiontwo.uiutils.ExtractVideoInfoUtil;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adaptertwo.VideoEditAdapter;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_VIDEO_COVER;

/**
 * @Created By Admin  on 2020/5/14
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
@Route(path = ACTIVITY_VIDEO_COVER)
public class VideoCoverActivity extends Activity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ivw_to)
    ImageView ivwTo;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    private ExtractVideoInfoUtil mExtractVideoInfoUtil;
    private ExtractFrameWorkThread mExtractFrameWorkThread;
    private VideoEditAdapter videoEditAdapter;
    private  String OutPutFileDirPath;
    @BindView(R.id.rv_imga)
    RecyclerView mRecyclerView;
    String videoPath;
    String videoCover;
    Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_cover);
        videoPath = getIntent().getStringExtra("videoPath");
        OutPutFileDirPath = getExternalCacheDir() + File.separator +"/sdcard/PDD/";
        LogUtils.e(OutPutFileDirPath);
        ButterKnife.bind(this);
        tvRight.setText("确认");
        tvTopTitle.setText("选择封面");
        tvRight.setVisibility(View.VISIBLE);
        initThumbs();

    }


    /**
     * 初始化缩略图
     */
    private void initThumbs() {
        if (!new File(videoPath).exists()) {
            Toast.makeText(this, "视频文件不存在", Toast.LENGTH_SHORT).show();
            finish();
        }
        mExtractVideoInfoUtil = new ExtractVideoInfoUtil(videoPath);
        long endPosition = Long.valueOf(mExtractVideoInfoUtil.getVideoLength());
        long startPosition = 0;
        int thumbnailsCount = 10;
        int extractW = (DeviceUtils.getScreenWidth(this));
        int extractH = DeviceUtils.dip2px(this, 80);
        mExtractFrameWorkThread = new ExtractFrameWorkThread(
                extractW, extractH, mUIHandler, videoPath,
                OutPutFileDirPath, startPosition, endPosition, thumbnailsCount);
        mExtractFrameWorkThread.start();

//        videoCover = mExtractVideoInfoUtil.extractFrames(videoPath);
//        GlideUtils.loadImage(VideoCoverActivity.this,videoCover,ivwTo);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        videoEditAdapter = new VideoEditAdapter(this,
                (DeviceUtils.getScreenWidth(this)) / 4);
        mRecyclerView.setAdapter(videoEditAdapter);
        videoEditAdapter.OnClickList(new VideoEditAdapter.OnClickList() {
            @Override
            public void OnClickList(int position,String path) {
                LogUtils.e(position,path);
                GlideUtils.loadImage(VideoCoverActivity.this,path,ivwTo);
                videoCover = path;
            }
        });

    }


    private final MainHandler mUIHandler = new MainHandler(this);

    private static class MainHandler extends Handler {
        private final WeakReference<VideoCoverActivity> mActivity;

        MainHandler(VideoCoverActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoCoverActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == ExtractFrameWorkThread.MSG_SAVE_SUCCESS) {
                    if (activity.videoEditAdapter != null) {
                        VideoEditInfo info = (VideoEditInfo) msg.obj;
                        activity.videoEditAdapter.addItemVideoInfo(info);
                    }
                }
            }
        }
    }
    @OnClick({R.id.img_top_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_right:
                Intent intent = new Intent();
                intent.putExtra("videoCover",  videoCover);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
