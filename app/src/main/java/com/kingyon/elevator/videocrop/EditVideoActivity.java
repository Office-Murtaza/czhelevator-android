package com.kingyon.elevator.videocrop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.uis.activities.advertising.PreviewVideoActivity;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.lansosdk.videoeditor.LanSoEditor;
import com.lansosdk.videoeditor.LanSongFileUtil;
import com.lansosdk.videoeditor.MediaInfo;
import com.lansosdk.videoeditor.VideoEditor;
import com.lansosdk.videoeditor.onVideoEditorProgressListener;
import com.libyuv.LibyuvUtil;
import com.marvhong.videoeffect.FillMode;
import com.marvhong.videoeffect.GlVideoView;
import com.marvhong.videoeffect.IVideoSurface;
import com.marvhong.videoeffect.composer.Mp4Composer;
import com.marvhong.videoeffect.helper.MagicFilterFactory;
import com.marvhong.videoeffect.helper.MagicFilterType;
import com.marvhong.videoeffect.utils.ConfigUtils;
import com.zhaoss.weixinrecorded.R;
import com.zhaoss.weixinrecorded.activity.BaseActivity;
import com.zhaoss.weixinrecorded.activity.CutTimeActivity;
import com.zhaoss.weixinrecorded.activity.MyfilterAdapter;
import com.zhaoss.weixinrecorded.activity.MyiconAdapter;
import com.zhaoss.weixinrecorded.activity.RecordedActivity;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;
import com.zhaoss.weixinrecorded.util.FilterModel;
import com.zhaoss.weixinrecorded.util.MyVideoEditor;
import com.zhaoss.weixinrecorded.util.RxJavaUtil;
import com.zhaoss.weixinrecorded.util.TimeUtils;
import com.zhaoss.weixinrecorded.util.Utils;
import com.zhaoss.weixinrecorded.view.ThumbnailView;
import com.zhaoss.weixinrecorded.view.TouchView;
import com.zhaoss.weixinrecorded.view.TuyaView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import VideoHandle.EpVideo;
import io.reactivex.disposables.Disposable;

import static com.kingyon.elevator.photopicker.MimeType.getVideoDuration;
import static com.zhaoss.weixinrecorded.activity.CutTimeActivity.dateToStamp;

/**
 * Created by zhaoshuang on 17/2/21.
 * 视频编辑界面
 */

public class EditVideoActivity extends BaseActivity {

    private int[] drawableBg = new int[]{R.drawable.color1, R.drawable.color2, R.drawable.color3, R.drawable.color4, R.drawable.color5};
    private int[] colors = new int[]{R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5};
    private List<Integer> integers = new ArrayList<>();
    public static EditVideoActivity editVideoActivity =null;

    private LinearLayout ll_color,ll_bottom;
    private RelativeLayout rl_tuya,rl_jq;
    private LinearLayout ll_thumbnail;
    private ThumbnailView thumbnailView;
    private boolean parsingFrame;//正在解析缩略图
    private RelativeLayout rl_close;
    private RelativeLayout rl_title;
    private RelativeLayout rl_bottom;
    private TextView tv_hint_delete;
    private RelativeLayout rl_expression;
    private RelativeLayout rl_touch_view;
    private RelativeLayout rl_edit_text;

    private TuyaView tv_video;
    private EditText et_tag;
    private TextView tv_tag;
    private LinearLayout ll_progress;
    private SeekBar sb_speed;
    private TextView tv_speed;
    private RelativeLayout rl_cut_size;
    private RelativeLayout rl_cut_time;
    private RelativeLayout rl_back;
    private RelativeLayout rl_speed;
    private TextView tv_finish_video;
    private TextView tv_finish;
    private TextView tv_close,tv_time;
    private RelativeLayout rl_pen;
    private RelativeLayout rl_icon;
    private RelativeLayout rl_text;

    private int currentColorPosition;
    private InputMethodManager manager;
    private String path;
    private int fromType;
    private int dp100;
    private float videoSpeed = 1;
    private MediaInfo mMediaInfo;
    private MyVideoEditor myVideoEditor = new MyVideoEditor();
    private TextView editorTextView;
    private int windowWidth;
    private int windowHeight;
    private int executeCount;//总编译次数
    private float executeProgress;//编译进度
    private MediaPlayer mMediaPlayer;


    private List<FilterModel> mVideoEffects = new ArrayList<>(); //视频滤镜效果
    private MagicFilterType[] mMagicFilterTypes;
    private List<String> strlist = new ArrayList<>();
    private ValueAnimator mEffectAnimator;
    HorizontalScrollView mHsvEffect;
    LinearLayout mLlEffectContainer;
    private SurfaceTexture mSurfaceTexture;
    private Mp4Composer mMp4Composer;
    GlVideoView mSurfaceView;

    private String frameDir;
    private Disposable frameSubscribe;
    private float INITNEW = 0;
    int jisuna = 0;
    private int voideTime;
    private int startTime;
    private int endTime;
    private TextView tvStratText,tvEndText,tvCenterText;
    private ImageView img_qx,img_wc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_video);
        editVideoActivity = this;
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        LanSoEditor.initSDK(this, null);
        LibyuvUtil.loadLibrary();
        dp100 = (int) getResources().getDimension(R.dimen.dp100);
        windowWidth = Utils.getWindowWidth(mContext);
        windowHeight = Utils.getWindowHeight(mContext);
        frameDir = LanSongFileUtil.getCreateFileDir(String.valueOf(System.currentTimeMillis()));
        initUI();
        initData();
        initVideoSize();
    }

    private void initUI() {
        mSurfaceView  = findViewById(R.id.glsurfaceview);

        ll_thumbnail = findViewById(R.id.ll_thumbnail);
        thumbnailView = findViewById(R.id.thumbnailView);
        tvEndText = findViewById(R.id.tv_end_text);
        tvStratText = findViewById(R.id.tv_start_text);
        tvCenterText = findViewById(R.id.tv_center_text);
        img_qx = findViewById(R.id.img_qx);
        img_wc = findViewById(R.id.img_wc);

        rl_pen = findViewById(R.id.rl_pen);
        tv_time = findViewById(R.id.tv_time);
        rl_icon = findViewById(R.id.rl_icon);
        rl_text = findViewById(R.id.rl_text);
        ll_color = findViewById(R.id.ll_color);
        ll_bottom = findViewById(R.id.ll_bottom);
        rl_jq = findViewById(R.id.rl_jq);
        tv_video = findViewById(R.id.tv_video);
        rl_expression = findViewById(R.id.rl_expression);
        rl_touch_view = findViewById(R.id.rl_touch_view);

        tv_close = findViewById(R.id.tv_close);
        tv_finish = findViewById(R.id.tv_finish);
        rl_edit_text = findViewById(R.id.rl_edit_text);
        et_tag = findViewById(R.id.et_tag);
        tv_tag = findViewById(R.id.tv_tag);
        tv_finish_video = findViewById(R.id.tv_finish_video);
        rl_tuya = findViewById(R.id.rl_tuya);
        rl_close = findViewById(R.id.rl_close);
        rl_title = findViewById(R.id.rl_title);
        rl_bottom = findViewById(R.id.rl_bottom);
        tv_hint_delete = findViewById(R.id.tv_hint_delete);
        rl_speed = findViewById(R.id.rl_speed);
        ll_progress = findViewById(R.id.ll_progress);
        sb_speed = findViewById(R.id.sb_speed);
        tv_speed = findViewById(R.id.tv_speed);
        rl_cut_size = findViewById(R.id.rl_cut_size);
        rl_cut_time = findViewById(R.id.rl_cut_time);
        rl_back = findViewById(R.id.rl_back);

        mLlEffectContainer = findViewById(R.id.ll_effect_container);
        mHsvEffect = findViewById(R.id.hsv_effect);

        mSurfaceView.init(new IVideoSurface() {
            @Override
            public void onCreated(SurfaceTexture surfaceTexture) {
                mSurfaceTexture = surfaceTexture;
                initMediaPlay(surfaceTexture,path);
            }
        });
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_color.setVisibility(View.GONE);
                rl_expression.setVisibility(View.GONE);
                rl_edit_text.setVisibility(View.GONE);
                ll_progress.setVisibility(View.GONE);
                mHsvEffect.setVisibility(View.GONE);
            }
        });


        rl_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePenState(!(ll_color.getVisibility() == View.VISIBLE));
                changeIconState(false);
                changeTextState(false);
                changeSpeedState(false);
                changeFiler(false);
            }
        });

        rl_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIconState(!(rl_expression.getVisibility() == View.VISIBLE));
                changePenState(false);
                changeTextState(false);
                changeSpeedState(false);
                changeFiler(false);
            }
        });

        rl_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextState(!(rl_edit_text.getVisibility() == View.VISIBLE));
                changePenState(false);
                changeIconState(false);
                changeSpeedState(false);
                changeFiler(false);
            }
        });

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_video.backPath();
            }
        });

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextState(!(rl_edit_text.getVisibility() == View.VISIBLE));
            }
        });

        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextState(!(rl_edit_text.getVisibility() == View.VISIBLE));
                if (et_tag.getText().length() > 0) {
                    addTextToWindow();
                }
            }
        });

        tv_finish_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishVideo();
            }
        });

        rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rl_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextState(false);
                changePenState(false);
                changeIconState(false);
                changeSpeedState(!(ll_progress.getVisibility() == View.VISIBLE));
                changeFiler(false);
            }
        });

        rl_cut_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mMediaPlayer != null) {
//                    mMediaPlayer.release();
//                }
//                Intent intent = new Intent(mContext, CutSizeActivity.class);
//                intent.putExtra(RecordedActivity.INTENT_PATH, path);
//                startActivityForResult(intent, 1);
                changeFiler(!(mHsvEffect.getVisibility() == View.VISIBLE));
                changePenState(false);
                changeTextState(false);
                changeSpeedState(false);
                changeIconState(false);

            }
        });

        rl_cut_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mMediaPlayer != null) {
//                    mMediaPlayer.release();
//                }
//                Intent intent = new Intent(mContext, CutTimeActivity.class);
//                intent.putExtra(RecordedActivity.INTENT_PATH, path);
//                startActivityForResult(intent, 2);
                changeIconState(false);
                changePenState(false);
                changeTextState(false);
                changeSpeedState(false);
                changeFiler(false);
                rl_title.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.GONE);
                rl_jq.setVisibility(View.VISIBLE);

            }
        });

        img_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_title.setVisibility(View.VISIBLE);
                ll_bottom.setVisibility(View.VISIBLE);
                rl_jq.setVisibility(View.INVISIBLE);
//                mMediaPlayer.seekTo(0);
            }
        });

        img_wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_title.setVisibility(View.VISIBLE);
                ll_bottom.setVisibility(View.VISIBLE);
                rl_jq.setVisibility(View.INVISIBLE);
            }
        });


        thumbnailView.setOnScrollBorderListener(new ThumbnailView.OnScrollBorderListener() {
            @Override
            public void OnScrollBorder(final float start, final float end) {
                Log.e("TAG",start+"========"+end);
                jisuna = 960 / voideTime;
                INITNEW = end;
                final float shuju = ((end/(jisuna))-(start/(jisuna)));
                changeTime();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tvStratText.setText(TimeUtils.secondToTime((long) (start / jisuna)) + "");
                            tvEndText.setText(TimeUtils.secondToTime((long) (end/(jisuna)))+"");
                            tvCenterText.setText(TimeUtils.secondToTime(((Integer.parseInt(dateToStamp(TimeUtils.secondToTime((long) (end / jisuna))))
                                    -Integer.parseInt(dateToStamp(TimeUtils.secondToTime((long) (start / jisuna)))))/1000)) + "");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            @Override
            public void onScrollStateChange() {
                changeVideoPlay();
            }
        });

        initColors();
        initExpression();
        initSpeed();

        et_tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                tv_tag.setText(s.toString());
            }
        });

    }

    private void changeVideoPlay(){
        if(mMediaPlayer != null) {
            mMediaPlayer.seekTo(startTime);
        }
    }

    /**
     * 更改选择的裁剪区间的时间
     */
    private void changeTime(){

        float left = thumbnailView.getLeftInterval();
        float pro1 = left/ll_thumbnail.getWidth();

        startTime = (int) (mMediaInfo.vDuration*1000*pro1);

        float right = thumbnailView.getRightInterval();
        float pro2 = right/ll_thumbnail.getWidth();
        endTime = (int) (mMediaInfo.vDuration*1000*pro2);
    }



    private void changeFiler(boolean flag) {
        if (flag) {
            mHsvEffect.setVisibility(View.VISIBLE);
        } else {
            mHsvEffect.setVisibility(View.GONE);
        }

    }

    private void initData() {

        Intent intent = getIntent();
        path = intent.getStringExtra(RecordedActivity.INTENT_PATH);
        fromType = intent.getIntExtra(RecordedActivity.INTENT_FROMTYPE,0);
        voideTime = (int) (getVideoDuration(path)/1000);
        tvEndText.setText(TimeUtils.secondToTime(voideTime));
        tvCenterText.setText(TimeUtils.secondToTime(voideTime));
        LogUtils.e(path);
        tv_time.setText("视频时长"+TimeUtils.secondToTime((getVideoDuration(path)/1000)));
        //当进行涂鸦操作时, 隐藏标题栏和底部工具栏
        tv_video.setOnTouchListener(new TuyaView.OnTouchListener() {
            @Override
            public void onDown() {
                changeMode(false);
            }

            @Override
            public void onUp() {
                changeMode(true);
            }
        });

        myVideoEditor.setOnProgessListener(new onVideoEditorProgressListener() {
            @Override
            public void onProgress(VideoEditor v, int percent) {

                float stepPro = 100f/executeCount;
                int temp = (int) (percent/100f*stepPro);
                if (editorTextView!=null) {
                    editorTextView.setText("视频编辑中" + (int) (executeProgress + temp) + "%");
                }

                if(percent==100){
                    executeProgress += stepPro;
                }
            }
        });

            integers.add(R.mipmap.expression1);integers.add(R.mipmap.expression2);
            integers.add(R.mipmap.expression3);integers.add(R.mipmap.expression4);
            integers.add(R.mipmap.expression5);integers.add(R.mipmap.expression6);
            integers.add(R.mipmap.expression7);integers.add(R.mipmap.expression8);
            integers.add(R.mipmap.expression9);integers.add(R.mipmap.expression10);
            integers.add(R.mipmap.expression11);integers.add(R.mipmap.expression12);
            integers.add(R.mipmap.expression13);integers.add(R.mipmap.expression14);
            integers.add(R.mipmap.s1);integers.add(R.mipmap.s2);integers.add(R.mipmap.s3);
            integers.add(R.mipmap.s4);integers.add(R.mipmap.s5);integers.add(R.mipmap.s6);
            integers.add(R.mipmap.s11);integers.add(R.mipmap.s7);integers.add(R.mipmap.s8);
            integers.add(R.mipmap.s9);integers.add(R.mipmap.s10);

            strlist.add("原图");strlist.add("胶片");strlist.add("怀旧");
            strlist.add("黑白");strlist.add("色温");strlist.add("重叠");
            strlist.add("模糊");strlist.add("噪点");strlist.add("对比度");
            strlist.add("伽马线");strlist.add("色度");strlist.add("交叉");
            strlist.add("灰度");strlist.add("染料");
            //滤镜效果集合
            mMagicFilterTypes = new MagicFilterType[]{
                    MagicFilterType.NONE, MagicFilterType.INVERT,
                    MagicFilterType.SEPIA, MagicFilterType.BLACKANDWHITE,
                    MagicFilterType.TEMPERATURE, MagicFilterType.OVERLAY,
                    MagicFilterType.BARRELBLUR, MagicFilterType.POSTERIZE,
                    MagicFilterType.CONTRAST, MagicFilterType.GAMMA,
                    MagicFilterType.HUE, MagicFilterType.CROSSPROCESS,
                    MagicFilterType.GRAYSCALE, MagicFilterType.CGACOLORSPACE,
            };

            for (int i = 0; i < strlist.size(); i++) {
                FilterModel model = new FilterModel();
                model.setName(strlist.get(i));
                mVideoEffects.add(model);
            }
            addEffectView();

        }

    private void addEffectView() {

        mLlEffectContainer.removeAllViews();
        for (int i = 0; i < mVideoEffects.size(); i++) {
            View itemView = LayoutInflater.from(this)
                    .inflate(R.layout.item_video_effect, mLlEffectContainer, false);
            TextView tv = itemView.findViewById(R.id.tv);
            ImageView iv = itemView.findViewById(R.id.iv);
            FilterModel model = mVideoEffects.get(i);
            int thumbId = MagicFilterFactory.filterType2Thumb(mMagicFilterTypes[i]);
            iv.setImageResource(thumbId);
            tv.setText(model.getName());
            final int index = i;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < mLlEffectContainer.getChildCount(); j++) {
                        View tempItemView = mLlEffectContainer.getChildAt(j);
                        TextView tempTv = tempItemView.findViewById(R.id.tv);
                        FilterModel tempModel = mVideoEffects.get(j);
                        if (j == index) {
                            //选中的滤镜效果
                            if (!tempModel.isChecked()) {
                                openEffectAnimation(tempTv, tempModel, true);
                            }
                            ConfigUtils.getInstance().setMagicFilterType(mMagicFilterTypes[j]);
                            mSurfaceView.setFilter(MagicFilterFactory.getFilter());
                        } else {
                            //未选中的滤镜效果
                            if (tempModel.isChecked()) {
                                openEffectAnimation(tempTv, tempModel, false);
                            }
                        }
                    }
                }
            });
            mLlEffectContainer.addView(itemView);
        }

    }
    private void openEffectAnimation(final TextView tv, FilterModel model, boolean isExpand) {
        model.setChecked(isExpand);
//        int startValue = UIUtils.dp2Px(30);
//        int endValue = UIUtils.dp2Px(100);
//        if (!isExpand) {
//            startValue = UIUtils.dp2Px(100);
//            endValue = UIUtils.dp2Px(30);
//        }
        mEffectAnimator = ValueAnimator.ofInt(100, 60);
        mEffectAnimator.setDuration(300);
        mEffectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, value, Gravity.BOTTOM);
                tv.setLayoutParams(params);
                tv.requestLayout();
            }
        });
        mEffectAnimator.start();
    }
    private void initMediaPlay(SurfaceTexture surface,String path){
        Log.e("TAG","====111"+path);
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            mMediaPlayer.setSurface(new Surface(surface));
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });

            mMediaPlayer.prepareAsync();
        }catch (Exception e){
            Log.e("TAG","===="+e.toString());
            e.printStackTrace();
        }
        initVideoSize();
        initThumbs();
    }

    /**
     * 初始化MediaPlayer
     */

    private void initVideoSize(){

        mMediaInfo = new MediaInfo(path);
        mMediaInfo.prepare();

        float ra = mMediaInfo.getWidth() * 1f / mMediaInfo.getHeight();
        ViewGroup.LayoutParams layoutParams = mSurfaceView.getLayoutParams();
        layoutParams.width = windowWidth;
        layoutParams.height = (int) (layoutParams.width / ra);
        mSurfaceView.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams layoutParams1 = rl_tuya.getLayoutParams();
        layoutParams1.width = layoutParams.width;
        layoutParams1.height = layoutParams.height;
        rl_tuya.setLayoutParams(layoutParams1);
    }

    /**
     * 初始化缩略图
     */
    private void initThumbs(){

        int frameCount = 10;
        final float interval = frameCount/mMediaInfo.vDuration;//提取帧的间隔
        LogUtils.e(interval);
        int thumbnailWidth = ll_thumbnail.getWidth()/frameCount;
        for (int x=0; x<frameCount; x++){
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(thumbnailWidth, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setBackgroundColor(Color.parseColor("#666666"));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ll_thumbnail.addView(imageView);
        }

        RxJavaUtil.run(new RxJavaUtil.OnRxAndroidListener<String>() {
            @Override
            public String doInBackground() throws Throwable {
                parsingFrame = true;

                float ratio = mMediaInfo.getWidth()*1f/mMediaInfo.getHeight();
                boolean succ = myVideoEditor.executeExtractFrame(path, interval, 100, (int) (100/ratio),  frameDir+"/frame_%05d.jpeg");
                if(succ){
                    return frameDir;
                }else {
                    return "";
                }
            }
            @Override
            public void onFinish(String result) {
                parsingFrame = false;
            }
            @Override
            public void onError(Throwable e) {
                parsingFrame = false;

            }
        });

        frameSubscribe = RxJavaUtil.loop(300, new RxJavaUtil.OnRxLoopListener() {
            @Override
            public Boolean takeWhile() throws Exception {
                return true;
            }
            @Override
            public void onExecute() {
                File[] files = new File(frameDir).listFiles();
                if(files != null){
                    for (int x = 0; x < files.length; x++) {
                        String framePath = files[x].getAbsolutePath();
                        if (x < ll_thumbnail.getChildCount()) {
                            ImageView imageView = (ImageView) ll_thumbnail.getChildAt(x);
                            if(imageView.getTag() == null){
                                imageView.setTag(framePath);
//                                imageView.setImageURI(Uri.fromFile(new File(framePath)));
                                imageView.setImageBitmap(BitmapFactory.decodeFile(framePath));
                                LogUtils.e("================="+framePath);
                            }
                        }else{
                            frameSubscribe.dispose();
                        }
                    }
                }
            }
            @Override
            public void onFinish() {

            }
            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void initSpeed() {

        sb_speed.setMax(200);
        sb_speed.setProgress(100);
        sb_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 50) {
                    progress = 50;
                    sb_speed.setProgress(50);
                }
                videoSpeed = progress / 100f;
                tv_speed.setText(videoSpeed + "");
                if (mMediaPlayer!=null) {
                    mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(videoSpeed));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void finishVideo() {

        final boolean isPen = tv_video.getPathSum() != 0;
        final boolean isImage = rl_touch_view.getChildCount() != 0;
        final boolean isSpeed = videoSpeed != 1;

        if(isPen || isImage){
            executeCount++;
        }
        if(isSpeed){
            executeCount++;
        }

        mMediaPlayer.stop();
        editorTextView = showProgressDialog();
        RxJavaUtil.run(new RxJavaUtil.OnRxAndroidListener<String>() {
            @Override
            public String doInBackground() throws Throwable {
                String videoPath = path;
                if(isPen || isImage){
                    videoPath = mergeImage(path);
                }
                if (isSpeed) {
                    videoPath = myVideoEditor.executeAdjustVideoSpeed2(path, videoSpeed);
                }
                if (thumbnailView.getLeftInterval()!=0||thumbnailView.getRightInterval()!=960){
                    float startS = Utils.formatFloat(startTime/1000f);
                    float durationS = Utils.formatFloat((endTime-startTime)/1000f);
                    LogUtils.e(startS,durationS,path);
                    videoPath =  myVideoEditor.executeCutVideoExact(path, startS, durationS);
                }
                return videoPath;
            }
            @Override
            public void onFinish(String result) {

                if (!TextUtils.isEmpty(result)) {
                    Log.e("TAG","-====="+result);

//                    EventBus.getDefault().post(new EventBusObjectEntity(EventBusConstants.VideoCropSuccessResult, result));
//                    finish();
                    startMediaCodec(result);
                } else {
                    Toast.makeText(getApplicationContext(), "视频编辑失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(Throwable e) {
                closeProgressDialog();
                Toast.makeText(getApplicationContext(), "视频编辑失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //更改界面模式
    private void changeMode(boolean flag) {
        if (flag) {
            rl_title.setVisibility(View.VISIBLE);
            rl_bottom.setVisibility(View.VISIBLE);
        } else {
            rl_title.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化表情
     */
    private void initExpression() {

        int dp80 = (int) getResources().getDimension(R.dimen.dp80);
        int dp10 = (int) getResources().getDimension(R.dimen.dp10);
        GridView gridView = new GridView(this);
        gridView.setPadding(dp10, dp10, dp10, dp10);
        gridView.setNumColumns(4);
        gridView.setVerticalSpacing(15);
        gridView.setHorizontalSpacing(15);
        MyiconAdapter myiconAdapter = new MyiconAdapter(this,integers);
        gridView.setAdapter(myiconAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rl_expression.setVisibility(View.GONE);
//                iv_icon.setImageResource(R.mipmap.icon);
                addExpressionToWindow(integers.get(position));
            }
        });
        rl_expression.addView(gridView);
    }

    /**
     * 添加表情到界面上
     */
    private void addExpressionToWindow(int result) {
        TouchView touchView = new TouchView(this);
        touchView.setBackgroundResource(result);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dp100, dp100);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        touchView.setLayoutParams(layoutParams);

        touchView.setLimitsX(0, windowWidth);
        touchView.setLimitsY(0, windowHeight - dp100 / 2);
        touchView.setOnLimitsListener(new TouchView.OnLimitsListener() {
            @Override
            public void OnOutLimits(float x, float y) {
                tv_hint_delete.setTextColor(Color.RED);
            }

            @Override
            public void OnInnerLimits(float x, float y) {
                tv_hint_delete.setTextColor(Color.WHITE);
            }
        });
        touchView.setOnTouchListener(new TouchView.OnTouchListener() {
            @Override
            public void onDown(TouchView view, MotionEvent event) {
                tv_hint_delete.setVisibility(View.VISIBLE);
                Log.e("TAG",event.toString());
                changeMode(false);
            }

            @Override
            public void onMove(TouchView view, MotionEvent event) {

            }

            @Override
            public void onUp(TouchView view, MotionEvent event) {
                tv_hint_delete.setVisibility(View.GONE);
                changeMode(true);
                if (view.isOutLimits()) {
                    rl_touch_view.removeView(view);
                }
            }
        });

        rl_touch_view.addView(touchView);
    }

    /**
     * 添加文字到界面上
     */
    private void addTextToWindow() {
        TouchView touchView = new TouchView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tv_tag.getWidth(), tv_tag.getHeight());
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        touchView.setLayoutParams(layoutParams);
        Bitmap bitmap = Bitmap.createBitmap(tv_tag.getWidth(), tv_tag.getHeight(), Bitmap.Config.ARGB_8888);
        tv_tag.draw(new Canvas(bitmap));
//        touchView.setImageBitmap(bitmap);
        touchView.setBackground(new BitmapDrawable(bitmap));
//        touchView.setBackgroundResource(R.mipmap.color_click);
        touchView.setPadding(10,10,10,10);
        touchView.setLimitsX(0, windowWidth);
        touchView.setLimitsY(0, windowHeight - dp100 / 2);
        touchView.setOnLimitsListener(new TouchView.OnLimitsListener() {
            @Override
            public void OnOutLimits(float x, float y) {
                tv_hint_delete.setTextColor(Color.RED);
            }

            @Override
            public void OnInnerLimits(float x, float y) {
                tv_hint_delete.setTextColor(Color.WHITE);
            }
        });
        touchView.setOnTouchListener(new TouchView.OnTouchListener() {
            @Override
            public void onDown(TouchView view, MotionEvent event) {
                tv_hint_delete.setVisibility(View.VISIBLE);
                changeMode(false);
            }

            @Override
            public void onMove(TouchView view, MotionEvent event) {

            }

            @Override
            public void onUp(TouchView view, MotionEvent event) {
                tv_hint_delete.setVisibility(View.GONE);
                changeMode(true);
                if (view.isOutLimits()) {
                    rl_touch_view.removeView(view);
                }
            }
        });

        rl_touch_view.addView(touchView);

        et_tag.setText("");
        tv_tag.setText("");
    }

    /**
     * 初始化底部颜色选择器
     */
    private void initColors() {

        int dp20 = (int) getResources().getDimension(R.dimen.dp20);
        int dp25 = (int) getResources().getDimension(R.dimen.dp25);

        for (int x = 0; x < drawableBg.length; x++) {
            RelativeLayout relativeLayout = new RelativeLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            relativeLayout.setLayoutParams(layoutParams);

            View view = new View(this);
            view.setBackgroundDrawable(getResources().getDrawable(drawableBg[x]));
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(dp20, dp20);
            layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
            view.setLayoutParams(layoutParams1);
            relativeLayout.addView(view);

            final View view2 = new View(this);
            view2.setBackgroundResource(R.mipmap.color_click);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(dp25, dp25);
            layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
            view2.setLayoutParams(layoutParams2);
            if (x != 0) {
                view2.setVisibility(View.GONE);
            }
            relativeLayout.addView(view2);

            final int position = x;
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentColorPosition != position) {
                        view2.setVisibility(View.VISIBLE);
                        ViewGroup parent = (ViewGroup) v.getParent();
                        ViewGroup childView = (ViewGroup) parent.getChildAt(currentColorPosition);
                        childView.getChildAt(1).setVisibility(View.GONE);
                        tv_video.setNewPaintColor(getResources().getColor(colors[position]));
                        currentColorPosition = position;
                    }
                }
            });

            ll_color.addView(relativeLayout, x);
        }
    }

    boolean isFirstShowEditText;

    /**
     * 弹出键盘
     */
    public void popupEditText() {

        isFirstShowEditText = true;
        et_tag.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isFirstShowEditText) {
                    isFirstShowEditText = false;
                    et_tag.setFocusable(true);
                    et_tag.setFocusableInTouchMode(true);
                    et_tag.requestFocus();
                    isFirstShowEditText = !manager.showSoftInput(et_tag, 0);
                }
            }
        });
    }

    /**
     * 执行文字编辑区域动画
     */
    private void startAnim(float start, float end, AnimatorListenerAdapter listenerAdapter) {

        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(200);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                rl_edit_text.setY(value);
            }
        });
        if (listenerAdapter != null) va.addListener(listenerAdapter);
        va.start();
    }

    /**
     * 更改画笔状态的界面
     */
    private void changePenState(boolean flag) {

        if (flag) {
            tv_video.setDrawMode(flag);
            tv_video.setNewPaintColor(getResources().getColor(colors[currentColorPosition]));
//            iv_pen.setImageResource(R.mipmap.pen_click);
            ll_color.setVisibility(View.VISIBLE);
        } else {
            tv_video.setDrawMode(flag);
            ll_color.setVisibility(View.GONE);
//            iv_pen.setImageResource(R.mipmap.pen);
        }
    }

    /**
     * 更改表情状态的界面
     */
    private void changeIconState(boolean flag) {

        if (flag) {
//            iv_icon.setImageResource(R.mipmap.icon_click);
            rl_expression.setVisibility(View.VISIBLE);
        } else {
            rl_expression.setVisibility(View.GONE);
//            iv_icon.setImageResource(R.mipmap.icon);
        }
    }

    /**
     * 更改文字输入状态的界面
     */
    private void changeTextState(boolean flag) {

        if (flag) {
            rl_edit_text.setY(windowHeight);
            rl_edit_text.setVisibility(View.VISIBLE);
            startAnim(rl_edit_text.getY(), 0, null);
            popupEditText();
        } else {
            manager.hideSoftInputFromWindow(et_tag.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            startAnim(0, windowHeight, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rl_edit_text.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 更改视频加速
     */
    private void changeSpeedState(boolean flag) {

        if (flag) {
            ll_progress.setVisibility(View.VISIBLE);
//            iv_speed.setImageResource(R.mipmap.speed_click);
        } else {
            ll_progress.setVisibility(View.GONE);
//            iv_speed.setImageResource(R.mipmap.speed);
        }
    }

    /**
     * 合成图片到视频里
     */
    private String mergeImage(String path)throws IOException {

        //得到涂鸦view的bitmap图片
        Bitmap bitmap = Bitmap.createBitmap(rl_tuya.getWidth(), rl_tuya.getHeight(), Bitmap.Config.ARGB_8888);
        rl_tuya.draw(new Canvas(bitmap));
        //这步是根据视频尺寸来调整图片宽高,和视频保持一致
        Matrix matrix = new Matrix();
        matrix.postScale(mMediaInfo.getWidth() * 1f / bitmap.getWidth(), mMediaInfo.getHeight() * 1f / bitmap.getHeight());
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        String imagePath =  LanSongFileUtil.DEFAULT_DIR+System.currentTimeMillis()+".png";
        FileOutputStream fos = new FileOutputStream(new File(imagePath));
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        return myVideoEditor.executeOverLayVideoFrame(path, imagePath, 0, 0);
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        if(rl_edit_text.getVisibility() == View.VISIBLE){
            changeTextState(false);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mMediaPlayer != null){
            mMediaPlayer.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG","返回mMediaPlayer");
        if(mMediaPlayer != null){
            Log.e("TAG","返回");
            initMediaPlay(mSurfaceTexture,path);
//            mSurfaceView.setVideoPath(path);
            initVideoSize();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("TAG","onActivityResult 返回");
        if (resultCode == RESULT_OK) {
            path = data.getStringExtra(RecordedActivity.INTENT_PATH);
            tv_time.setText("视频时长"+TimeUtils.secondToTime((getVideoDuration(path)/1000)));
        }
    }

    /**
     * 视频添加滤镜效果
     */
    private void startMediaCodec(String srcPath) {
        final String outputPath = Utils.getTrimmedVideoPath(this, "small_video/trimmedVideo",
                "filterVideo_");
            mMp4Composer = new Mp4Composer(srcPath, outputPath)
                // .rotation(Rotation.ROTATION_270)
                .size(768, 1220)
                .videoBitrate(2200000)
                .fillMode(FillMode.PRESERVE_ASPECT_FIT)
                .filter(MagicFilterFactory.getFilter())
                .mute(false)
                .flipHorizontal(false)
                .flipVertical(false)
                .listener(new Mp4Composer.Listener() {
                    @Override
                    public void onProgress(final double progress) {
                        Log.d("TAG", "filterVideo---onProgress: " + (int) (progress * 100));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editorTextView.setText("视频渲染中"+(int) (progress * 100)+"%");
                            }
                        });
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("TAG", "filterVideo---onCompleted"+outputPath);
                        closeProgressDialog();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            MyActivityUtils.goPreviewVideoActivity(EditVideoActivity.this, PreviewVideoActivity.class, outputPath, getVideoDuration(outputPath),fromType);
//                                if (fromType== Constants.FROM_TYPE_TO_SELECT_MEDIA.MYADSELECT){
//                                    EventBus.getDefault().post(new EventBusObjectEntity(EventBusConstants.VideoCropSuccessResult, outputPath));
//                                    finish();
//                                }else {
//                                    MyActivityUtils.goConfirmOrderActivity(EditVideoActivity.this,
//                                            Constants.FROM_TYPE.MEDIADATA, outputPath, Constants.Materia_Type.VIDEO);
//                                    finish();
//                                }
                            }
                        });
                    }

                    @Override
                    public void onCanceled() {
//                        NormalProgressDialog.stopLoading();
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Log.e("TAG", "filterVideo---onFailed()");
//                        NormalProgressDialog.stopLoading();
                        Toast.makeText(EditVideoActivity.this, "视频处理失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

}
