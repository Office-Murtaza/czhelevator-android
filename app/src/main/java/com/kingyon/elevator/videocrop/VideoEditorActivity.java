package com.kingyon.elevator.videocrop;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.VideoEditorPresenter;
import com.kingyon.elevator.utils.MyStatusBarUtils;
import com.kingyon.elevator.utils.PublicFuncation;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.utils.utilstwo.VideoClip;
import com.kingyon.elevator.videocrop.video.EditSpacingItemDecoration;
import com.kingyon.elevator.videocrop.video.ExtractFrameWorkThread;
import com.kingyon.elevator.videocrop.video.ExtractVideoInfoUtil;
import com.kingyon.elevator.videocrop.video.PictureUtils;
import com.kingyon.elevator.videocrop.video.RangeSeekBar;
import com.kingyon.elevator.videocrop.video.UIUtil;
import com.kingyon.elevator.videocrop.video.VideoEditAdapter;
import com.kingyon.elevator.videocrop.video.VideoEditInfo;
import com.kingyon.elevator.view.VideoEditorView;
import com.lansosdk.NoFree.LSOVideoScale;
import com.lansosdk.videoeditor.VideoEditor;
import com.lansosdk.videoeditor.onVideoEditorProgressListener;
import com.marvhong.videoeffect.FillMode;
import com.marvhong.videoeffect.composer.Mp4Composer;
import com.zhaoss.weixinrecorded.activity.RecordedActivity;
import com.zhaoss.weixinrecorded.util.MyVideoEditor;
import com.zhaoss.weixinrecorded.util.RxJavaUtil;
import com.zhaoss.weixinrecorded.util.Utils;

import java.io.File;
import java.lang.ref.WeakReference;

import VideoHandle.EpDraw;
import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static com.kingyon.elevator.photopicker.MimeType.getVideoDuration;

//import com.iceteck.silicompressorr.SiliCompressor;

/**
 * 视频编辑界面
 */
public class VideoEditorActivity extends MvpBaseActivity<VideoEditorPresenter> implements VideoEditorView {

    private static final String TAG = VideoEditorActivity.class.getSimpleName();
    private long MIN_CUT_DURATION = 15 * 1000L;// 最小剪辑时间3s
    private long MAX_CUT_DURATION = 15 * 1000L;//视频最多剪切多长时间
    private static final int MAX_COUNT_RANGE = 10;//seekBar的区域内一共有多少张图片
    @BindView(R.id.id_seekBarLayout)
    LinearLayout seekBarLayout;
    private ExtractVideoInfoUtil mExtractVideoInfoUtil;
    private int mMaxWidth;
    private long duration;
    RangeSeekBar seekBar;
    @BindView(R.id.uVideoView)
    VideoView mVideoView;
    @BindView(R.id.id_rv_id)
    RecyclerView mRecyclerView;
    @BindView(R.id.positionIcon)
    ImageView positionIcon;
    @BindView(R.id.cancel_crop)
    TextView cancel_crop;
    @BindView(R.id.start_crop)
    TextView start_crop;
    @BindView(R.id.crop_video_time)
    TextView crop_video_time;
    private VideoEditAdapter videoEditAdapter;
    private float averageMsPx;//每毫秒所占的px
    private float averagePxMs;//每px所占用的ms毫秒
    private String OutPutFileDirPath;
    private ExtractFrameWorkThread mExtractFrameWorkThread;
    private String path;
    private long leftProgress, rightProgress;
    private long scrollPos = 0;
    private int mScaledTouchSlop;
    private int lastScrollX;
    private boolean isSeeking;
    private String localCache = "";
    private String videoPath;
    private long startTime = 0;
    private long endTime = 0;
    private int fromType = Constants.FROM_TYPE_TO_SELECT_MEDIA.PLAN;//来自于哪个界面
    private String planType = "";
    private LSOVideoScale videoCompress;
    private Mp4Composer mMp4Composer;
    private long cropTime;
    MyVideoEditor myVideoEditor = new MyVideoEditor();
    private TextView editorTextView;
    private Disposable frameSubscribe;
    private int voideTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_editor);
        MyStatusBarUtils.setStatusBarWhite(this, "#000000");
        ButterKnife.bind(this);
        fromType = getIntent().getIntExtra("fromType", 1001);
        planType = getIntent().getStringExtra("planType");
        if (planType.equals(Constants.PLAN_TYPE.BUSINESS)) {
            MAX_CUT_DURATION = 15000;
            MIN_CUT_DURATION = 15000;
            crop_video_time.setText("裁剪时长15S");
            cropTime  = 15;
        } else if (planType.equals(Constants.PLAN_TYPE.DIY)) {
            MAX_CUT_DURATION = 60000;
            MIN_CUT_DURATION = 60000;
            crop_video_time.setText("裁剪时长60S");
            cropTime  = 60;
        }
        initData();
        initView();
        initEditVideo();
        initPlay();

        myVideoEditor.setOnProgessListener(new onVideoEditorProgressListener() {
            @Override
            public void onProgress(VideoEditor v, int percent) {
//                if(!parsingFrame){
//                    editorTextView.setText("视频编辑中"+percent+"%");
//                }
                LogUtils.e("321123"+percent);
            }
        });
    }


    @OnClick({R.id.start_crop, R.id.cancel_crop})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.start_crop:
                String name = System.currentTimeMillis() + ".mp4";
                videoPath = RuntimeUtils.videoPath + File.separator + name;
                RuntimeUtils.cropVideoPath = videoPath;
                presenter.startCropVideo(this,videoPath, startTime, startTime + MIN_CUT_DURATION > duration ? duration : startTime + MIN_CUT_DURATION,cropTime);

//                cutMp4(startTime,(startTime+(cropTime*1000)),RuntimeUtils.selectVideoPath,"/storage/emulated/0/PDD/",name);
//                LogUtils.e(startTime,(startTime+(cropTime*1000)),RuntimeUtils.selectVideoPath,"/storage/emulated/0/PDD/",name);
                break;
            case R.id.cancel_crop:
                finish();
                break;
        }
    }



    @Override
    public void finish() {
        super.finish();
    }

    private void initData() {
        localCache = PublicFuncation.getDiskCacheDir(this) + File.separator + "video";
        PublicFuncation.creatCacheFolder(localCache);
        RuntimeUtils.videoPath = localCache;
        path = RuntimeUtils.selectVideoPath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        if (!new File(path).exists()) {
            Toast.makeText(this, "视频文件不存在", Toast.LENGTH_LONG).show();
            finish();
        }
        mExtractVideoInfoUtil = new ExtractVideoInfoUtil(path);
        duration = Long.valueOf(mExtractVideoInfoUtil.getVideoLength());
        mMaxWidth = UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70);
        mScaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

    }

    private void initView() {
        seekBarLayout = (LinearLayout) findViewById(R.id.id_seekBarLayout);
        mVideoView = (VideoView) findViewById(R.id.uVideoView);
        positionIcon = (ImageView) findViewById(R.id.positionIcon);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_rv_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        videoEditAdapter = new VideoEditAdapter(this,
                (UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70)) / 10);
        mRecyclerView.setAdapter(videoEditAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }


    private void initEditVideo() {
        //for video edit
        long startPosition = 0;
        long endPosition = duration;
        int thumbnailsCount;
        int rangeWidth;
        boolean isOver_60_s;
        if (endPosition <= MAX_CUT_DURATION) {
            isOver_60_s = false;
            thumbnailsCount = MAX_COUNT_RANGE;
            rangeWidth = mMaxWidth;
        } else {
            isOver_60_s = true;
            thumbnailsCount = (int) (endPosition * 1.0f / (MAX_CUT_DURATION * 1.0f) * MAX_COUNT_RANGE);
            rangeWidth = mMaxWidth / MAX_COUNT_RANGE * thumbnailsCount;
        }
        mRecyclerView.addItemDecoration(new EditSpacingItemDecoration(UIUtil.dip2px(this, 35), thumbnailsCount));

        //init seekBar
        if (isOver_60_s) {
            seekBar = new RangeSeekBar(this, 0L, MAX_CUT_DURATION);
            seekBar.setSelectedMinValue(0L);
            seekBar.setSelectedMaxValue(MAX_CUT_DURATION);
        } else {
            seekBar = new RangeSeekBar(this, 0L, endPosition);
            seekBar.setSelectedMinValue(0L);
            seekBar.setSelectedMaxValue(endPosition);
        }
        seekBar.setMin_cut_time(MIN_CUT_DURATION);//设置最小裁剪时间
        seekBar.setNotifyWhileDragging(true);
        seekBar.setOnRangeSeekBarChangeListener(mOnRangeSeekBarChangeListener);
        seekBarLayout.addView(seekBar);
        averageMsPx = duration * 1.0f / rangeWidth * 1.0f;
        OutPutFileDirPath = PictureUtils.getSaveEditThumbnailDir(this);
        int extractW = (UIUtil.getScreenWidth(this) - UIUtil.dip2px(this, 70)) / MAX_COUNT_RANGE;
        int extractH = UIUtil.dip2px(this, 55);
        mExtractFrameWorkThread = new ExtractFrameWorkThread(extractW, extractH, mUIHandler, path, OutPutFileDirPath, startPosition, endPosition, thumbnailsCount);
        mExtractFrameWorkThread.start();

        //init pos icon start
        leftProgress = 0;
        if (isOver_60_s) {
            rightProgress = MAX_CUT_DURATION;
        } else {
            rightProgress = endPosition;
        }
        averagePxMs = (mMaxWidth * 1.0f / (rightProgress - leftProgress));
    }


    private void initPlay() {
        mVideoView.setVideoPath(path);
        //设置videoview的OnPrepared监听
        mVideoView.setOnPreparedListener(mp -> {
            //设置MediaPlayer的OnSeekComplete监听
            mp.setOnSeekCompleteListener(mp1 -> {
                if (!isSeeking) {
                    videoStart();
                }
            });
        });
        videoStart();
    }

    private boolean isOverScaledTouchSlop;

    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.d(TAG, "-------newState:>>>>>" + newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                isSeeking = false;
//                videoStart();
            } else {
                isSeeking = true;
                if (isOverScaledTouchSlop && mVideoView != null && mVideoView.isPlaying()) {
                    videoPause();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isSeeking = false;
            int scrollX = getScrollXDistance();
            //达不到滑动的距离
            if (Math.abs(lastScrollX - scrollX) < mScaledTouchSlop) {
                isOverScaledTouchSlop = false;
                return;
            }
            isOverScaledTouchSlop = true;
            Log.d(TAG, "-------scrollX:>>>>>" + scrollX);
            //初始状态,why ? 因为默认的时候有35dp的空白！
            if (scrollX == -UIUtil.dip2px(VideoEditorActivity.this, 35)) {
                scrollPos = 0;
            } else {
                if (mVideoView != null && mVideoView.isPlaying()) {
                    videoPause();
                }
                isSeeking = true;
                scrollPos = (long) (averageMsPx * (UIUtil.dip2px(VideoEditorActivity.this, 35) + scrollX));
                Log.d(TAG, "-------scrollPos:>>>>>" + scrollPos);
                leftProgress = seekBar.getSelectedMinValue() + scrollPos;
                rightProgress = seekBar.getSelectedMaxValue() + scrollPos;
                startTime = leftProgress;
                Log.d(TAG, "-------leftProgress:>>>>>" + leftProgress);
                mVideoView.seekTo((int) leftProgress);
            }
            lastScrollX = scrollX;
        }
    };

    /**
     * 水平滑动了多少px
     *
     * @return int px
     */
    private int getScrollXDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemWidth = firstVisibleChildView.getWidth();
        return (position) * itemWidth - firstVisibleChildView.getLeft();
    }

    private ValueAnimator animator;

    private void anim() {
        Log.d(TAG, "--anim--onProgressUpdate---->>>>>>>" + mVideoView.getCurrentPosition());
        if (positionIcon.getVisibility() == View.GONE) {
            positionIcon.setVisibility(View.VISIBLE);
        }
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) positionIcon.getLayoutParams();
        int start = (int) (UIUtil.dip2px(this, 35) + (leftProgress/*mVideoView.getCurrentPosition()*/ - scrollPos) * averagePxMs);
        int end = (int) (UIUtil.dip2px(this, 35) + (rightProgress - scrollPos) * averagePxMs);
        animator = ValueAnimator
                .ofInt(start, end)
                .setDuration((rightProgress - scrollPos) - (leftProgress/*mVideoView.getCurrentPosition()*/ - scrollPos));
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.leftMargin = (int) animation.getAnimatedValue();
                positionIcon.setLayoutParams(params);
            }
        });
        animator.start();

    }

    private final MainHandler mUIHandler = new MainHandler(this);

    @Override
    public void cropVideoSuccess(String path) {
//        剪切
        LogUtils.e((int)(startTime/1000),cropTime,seekBar.getSelectedMinValue()
                ,(seekBar.getSelectedMaxValue()-seekBar.getSelectedMinValue())
                ,startTime,endTime,path);

        ClipDataVideo(path);


    }

    private void ClipDataVideo(String path) {
//        showProgressDialog(getString(R.string.wait),false);
        final String outputPath = Utils.getTrimmedVideoPath(this, "small_video/PDD",
                "testVideo_");
        EpVideo epVideo = new EpVideo(path);
        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(outputPath);
        epVideo.clip((startTime/1000),cropTime);
        EpEditor.exec(epVideo, outputOption, new OnEditorListener() {
            @Override
            public void onSuccess() {
                hideProgressDailog();
                voideTime = (int) (getVideoDuration(outputPath)/1000);
                LogUtils.e( RuntimeUtils.selectVideoPath,outputPath,voideTime,getVideoDuration(outputPath));
                if(voideTime == cropTime){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (fromType == Constants.FROM_TYPE_TO_SELECT_MEDIA.MYADSELECT) {
                                //            剪切完成
                                Intent intent = new Intent(VideoEditorActivity.this, EditVideoActivity.class);
                                intent.putExtra("path",outputPath);
                                intent.putExtra("fromType",fromType);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(VideoEditorActivity.this, EditVideoActivity.class);
                                intent.putExtra("path",outputPath);
                                intent.putExtra("fromType",fromType);
                                startActivity(intent);
                            }
                            finish();
                        }
                    });
                }else {
                    ToastUtils.showShort("视频裁剪失败,请拖动后重新尝试！");
                }
            }
            @Override
            public void onFailure() {
                ToastUtils.showShort("视频裁剪失败,请重新尝试！");
                LogUtils.e("视频裁剪失败-----------------------");
                hideProgressDailog();
            }

            @Override
            public void onProgress(float progress) {
                //这里获取处理进度
                hideProgressDailog();
                LogUtils.d("onProgress=="+progress);

            }
        });

    }


    private static class MainHandler extends Handler {
        private final WeakReference<VideoEditorActivity> mActivity;

        MainHandler(VideoEditorActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
//            LogUtils.e(msg.obj.toString());
            VideoEditorActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == ExtractFrameWorkThread.MSG_SAVE_SUCCESS) {
                    if (activity.videoEditAdapter != null) {
                        VideoEditInfo info = (VideoEditInfo) msg.obj;
//                        LogUtils.e(info.toString());
                        activity.videoEditAdapter.addItemVideoInfo(info);
                    }
                }
            }
        }
    }

    private final RangeSeekBar.OnRangeSeekBarChangeListener mOnRangeSeekBarChangeListener = new RangeSeekBar.OnRangeSeekBarChangeListener() {
        @Override
        public void onRangeSeekBarValuesChanged(RangeSeekBar bar, long minValue, long maxValue, int action, boolean isMin, RangeSeekBar.Thumb pressedThumb) {
            leftProgress = minValue + scrollPos;
            rightProgress = maxValue + scrollPos;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    isSeeking = false;
                    videoPause();
                    break;
                case MotionEvent.ACTION_MOVE:
                    isSeeking = true;
//                    mVideoView.seekTo((int) (pressedThumb == RangeSeekBar.Thumb.MIN ?
//                            leftProgress : rightProgress));
                    mVideoView.seekTo((int) leftProgress);
                    break;
                case MotionEvent.ACTION_UP:
                    isSeeking = false;
                    //从minValue开始播
                    //startTime = leftProgress;
                    mVideoView.seekTo((int) leftProgress);
//                    videoStart();
                    break;
                default:
                    break;
            }
        }
    };


    private void videoStart() {
        Log.d(TAG, "----videoStart----->>>>>>>");
        mVideoView.start();
        positionIcon.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        anim();
        handler.removeCallbacks(run);
        handler.post(run);
    }

    private void videoProgressUpdate() {
        long currentPosition = mVideoView.getCurrentPosition();
        Log.d(TAG, "----onProgressUpdate-cp---->>>>>>>" + currentPosition);
        if (currentPosition >= (rightProgress)) {
            mVideoView.seekTo((int) leftProgress);
            positionIcon.clearAnimation();
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
            anim();
        }
    }

    private void videoPause() {
        isSeeking = false;
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.pause();
            handler.removeCallbacks(run);
        }
        Log.d(TAG, "----videoPause----->>>>>>>");
        if (positionIcon.getVisibility() == View.VISIBLE) {
            positionIcon.setVisibility(View.GONE);
        }
        positionIcon.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.seekTo((int) leftProgress);
//            videoStart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null && mVideoView.isPlaying()) {
            videoPause();
        }
    }

    private Handler handler = new Handler();
    private Runnable run = new Runnable() {

        @Override
        public void run() {
            videoProgressUpdate();
            handler.postDelayed(run, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeAllAction();
    }

    private void removeAllAction() {
        if (animator != null) {
            animator.cancel();
        }
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        if (mExtractVideoInfoUtil != null) {
            mExtractVideoInfoUtil.release();
        }
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        if (mExtractFrameWorkThread != null) {
            mExtractFrameWorkThread.stopExtract();
        }
        mUIHandler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        if (!TextUtils.isEmpty(OutPutFileDirPath)) {
            PictureUtils.deleteFile(new File(OutPutFileDirPath));
        }
        mRecyclerView = null;
        videoEditAdapter = null;
        handler = null;
        mVideoView = null;
        animator = null;
    }

    @Override
    public VideoEditorPresenter initPresenter() {
        return new VideoEditorPresenter(this);
    }


    /**
     * 视频剪切
     * @param startTime 视频剪切的开始时间
     * @param endTime 视频剪切的结束时间
     * @param FilePath 被剪切视频的路径
     * @param WorkingPath 剪切成功保存的视频路径
     * @param fileName 剪切成功保存的文件名
     */
    private synchronized void cutMp4(final long startTime, final long endTime, final String FilePath, final String WorkingPath, final String fileName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //视频剪切
                    VideoClip videoClip= new VideoClip();//实例化VideoClip类
                    videoClip.setFilePath(FilePath);//设置被编辑视频的文件路径  FileUtil.getMediaDir()+"/test/laoma3.mp4"
                    videoClip.setWorkingPath(WorkingPath);//设置被编辑的视频输出路径  FileUtil.getMediaDir()
                    videoClip.setStartTime(startTime);//设置剪辑开始的时间
                    videoClip.setEndTime(endTime);//设置剪辑结束的时间
                    videoClip.setOutName(fileName);//设置输出的文件名称
                    videoClip.clip();//调用剪辑并保存视频文件方法（建议作为点击保存时的操作并加入等待对话框）

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
