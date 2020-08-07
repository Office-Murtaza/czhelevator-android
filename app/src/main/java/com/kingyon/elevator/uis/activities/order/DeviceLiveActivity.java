package com.kingyon.elevator.uis.activities.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.App;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetApi;
import com.kingyon.elevator.others.CameraWebSocketClient;
import com.kingyon.elevator.uis.widgets.video.MediaController;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.orhanobut.logger.Logger;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2019/1/22.
 * Email：lc824767150@163.com
 */

public class DeviceLiveActivity extends BaseSwipeBackActivity {

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

    private PointItemEntity entity;
    private CameraWebSocketClient socketClient;
    private Subscription heartSubscribe;
//    String LiveAddress = "rtmp://58.200.131.2:1935/livetv/hunanhd";
    @Override
    protected String getTitleText() {
        entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        return "视频监控";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_device_live;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initVideoPlay();
    }

    private void initVideoPlay() {
        videoTextureView.setAVOptions(createAVOptions());
        videoTextureView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        videoTextureView.setBufferingIndicator(loadingView);
//        videoTextureView.setMediaController(mediaController);
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
        videoTextureView.setLooping(false);
        coverImage.setVisibility(View.VISIBLE);

        //设置拉流地址
        LogUtils.d("监播地址："+entity.getLiveAddress());
        videoTextureView.setVideoPath(entity.getLiveAddress());
//        videoTextureView.setVideoPath(LiveAddress);

        //初始化和打开socket连接
        socketClient = new CameraWebSocketClient(NetApi.socketDomainName);
        socketClient.setWebSocketListener(webSocketListener);
        socketClient.connect();
        showProgressDialog(getString(R.string.wait),true);
    }

    private CameraWebSocketClient.WebSocketListener webSocketListener = new CameraWebSocketClient.WebSocketListener() {
        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            sendLogin();
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            dealReceived(text);
        }

        @Override
        public void onConnectedError(WebSocket webSocket, WebSocketException exception) {
            showToast("监控连接出错");
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
            closeHeartBeat();
        }

        @Override
        public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
            dealReceived(new String(binary, "UTF-8"));
        }
    };

    private void startHeartBeat() {
        closeHeartBeat();
        heartSubscribe = Observable.interval(0, 30, TimeUnit.SECONDS)
                .compose(this.<Long>bindLifeCycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomApiCallback<Long>() {
                    @Override
                    protected void onResultError(ApiException ex) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        sendHeart();
                    }
                });
    }

    protected void closeHeartBeat() {
        if (heartSubscribe != null && !heartSubscribe.isUnsubscribed()) {
            heartSubscribe.unsubscribe();
            heartSubscribe = null;
        }
    }

    private void dealReceived(String received) {
        Logger.i(String.format("SocketConnect onReceived\t%s", received));
        try {
            JSONObject jsonObject = JSONObject.parseObject(received);
            String type = jsonObject.getString("type");
            switch (type.toLowerCase()) {
                case "login":
                    dealLogin(jsonObject);
                    break;
                case "monitor":
                    dealMonitor(jsonObject);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("SocketConnect onReceived(byte[] msg) error");
        }
    }

    private void dealLogin(JSONObject jsonObject) {
        String result = jsonObject.getString("result");
        if (result != null && TextUtils.equals("ok", result.toLowerCase())) {
            startHeartBeat();
            sendOpen(FormatUtils.getInstance().getLiveLiftNo(entity.getLiveAddress()));
//            sendOpen(FormatUtils.getInstance().getLiveLiftNo(LiveAddress));
            LogUtils.e(FormatUtils.getInstance().getLiveLiftNo(entity.getLiveAddress()));
        } else {
            postFinishWithToast("登录异常");
        }
    }

    private void dealMonitor(JSONObject jsonObject) {
        String result = jsonObject.getString("result");
        LogUtils.d("返回的监播状态：",result);
        if (result != null) {
            switch (result.toLowerCase()) {
                case "offline":
                    postFinishWithToast("摄像头已离线");
                    break;
                case "nocamera":
                    postFinishWithToast("没有找到摄像头");
                    break;
                case "ban monitor":
                    postFinishWithToast("该设备禁止监播");
                    break;
                case "stopedmonitor":
                    postFinishWithToast("监播已结束");
                    break;
                case "ok":
                    postStartPlay();
                    break;
            }
        }
    }

    private void sendLogin() {
        try {
            socketClient.sendLogin();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            postFinishWithToast("登录失败");
        }
    }

    private void sendHeart() {
        try {
            socketClient.sendHeart();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void sendOpen(String cid) {
        try {
            socketClient.sendOpen(cid);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            showToast("打开监控失败");
        }
    }

    private void sendStop() {
        try {
            socketClient.sendStop();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            showToast("关闭监控失败");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        flVideo.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 20);
//        if (videoTextureView != null && videoTextureView.isPlaying()) {
//            videoTextureView.pause();
//            pauseView.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void postFinish() {
        videoTextureView.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 100);
    }

    private void postFinishWithToast(final String toast) {
        videoTextureView.postDelayed(new Runnable() {
            @Override
            public void run() {
                showToast(toast);
                hideProgress();
                finish();
            }
        }, 100);
    }

    private void postStartPlay() {
        videoTextureView.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                loadingView.setVisibility(View.VISIBLE);
                startPlay();
            }
        }, 100);
    }

    private void startPlay() {
        if (videoTextureView != null && !videoTextureView.isPlaying()) {
            videoTextureView.start();
        }
    }

    @Override
    protected void onDestroy() {
        sendStop();
        closeHeartBeat();
        socketClient.disconnect();
        if (videoTextureView != null) {
            videoTextureView.stopPlayback();
            videoTextureView = null;
        }
        super.onDestroy();
    }

//    @OnClick(R.id.fl_video)
//    public void onViewClicked() {
//        startPlay();
//    }

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
}
