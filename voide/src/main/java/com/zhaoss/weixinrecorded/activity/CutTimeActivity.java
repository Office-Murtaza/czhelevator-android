package com.zhaoss.weixinrecorded.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lansosdk.videoeditor.LanSongFileUtil;
import com.lansosdk.videoeditor.MediaInfo;
import com.lansosdk.videoeditor.VideoEditor;
import com.lansosdk.videoeditor.onVideoEditorProgressListener;
import com.zhaoss.weixinrecorded.R;
import com.zhaoss.weixinrecorded.util.MyVideoEditor;
import com.zhaoss.weixinrecorded.util.RxJavaUtil;
import com.zhaoss.weixinrecorded.util.TimeUtils;
import com.zhaoss.weixinrecorded.util.Utils;
import com.zhaoss.weixinrecorded.view.ThumbnailView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhaoshuang on 2017/9/30.
 */

public class CutTimeActivity extends BaseActivity{

    private TextureView textureView;
    private RelativeLayout rl_close;
    private TextView tv_finish_video;
    private LinearLayout ll_thumbnail;
    private ThumbnailView thumbnailView;
    private float INITNEW = 0;
    private int NEW = 0;
    int jisuna = 0;

    private String path;
    private int startTime;
    private int endTime;
    private MediaInfo mMediaInfo;
    private TextView editorTextView;
    private TextView tvStratText,tvEndText,tvCenterText;
    private MediaPlayer mMediaPlayer;
    private MyVideoEditor myVideoEditor = new MyVideoEditor();
    private boolean parsingFrame;//正在解析缩略图
    private String frameDir;
    private Disposable frameSubscribe;
    private int voideTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cut_time);

        frameDir = LanSongFileUtil.getCreateFileDir(String.valueOf(System.currentTimeMillis()));

        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        voideTime = (getVideoDuration(path)/1000);
        mMediaInfo = new MediaInfo(path);
        mMediaInfo.prepare();

        initUI();
    }

    private void initUI() {

        rl_close = findViewById(R.id.rl_close);
        tv_finish_video = findViewById(R.id.tv_finish_video);
        textureView = findViewById(R.id.textureView);
        ll_thumbnail = findViewById(R.id.ll_thumbnail);
        thumbnailView = findViewById(R.id.thumbnailView);
        tvEndText = findViewById(R.id.tv_end_text);
        tvStratText = findViewById(R.id.tv_start_text);
        tvCenterText = findViewById(R.id.tv_center_text);
        rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_finish_video.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Log.e("TAG==",thumbnailView.getTransitionName()
                        +"\ngetLeftInterval==="+thumbnailView.getLeftInterval()+"\ngetRightInterval==="+thumbnailView.getRightInterval());
                if (thumbnailView.getLeftInterval()==0&&INITNEW==0){
                    finish();
                }else {
                    cutVideo();
                }

            }
        });

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                initMediaPlay(surface);
            }
            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }
            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }
            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
        tvEndText.setText(TimeUtils.secondToTime(voideTime));
        tvCenterText.setText(TimeUtils.secondToTime(voideTime));
//        Log.e("TAG",thumbnailView.getRightInterval()+"====");
        //监听裁剪器滑动
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

        myVideoEditor.setOnProgessListener(new onVideoEditorProgressListener() {
            @Override
            public void onProgress(VideoEditor v, int percent) {
                if(!parsingFrame){
                    editorTextView.setText("视频编辑中"+percent+"%");
                }
            }
        });
    }

    private void initMediaPlay(SurfaceTexture surface){

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(path);

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
            e.printStackTrace();
        }

        initVideoSize();
        initThumbs();
    }

    /**
     * 初始化视频播放器
     */
    private void initVideoSize(){

        float ra = mMediaInfo.getWidth()*1f/mMediaInfo.getHeight();

        ViewGroup.LayoutParams layoutParams = textureView.getLayoutParams();
        layoutParams.width = Utils.getWindowWidth(mContext);
        layoutParams.height = (int) (layoutParams.width/ra);
        textureView.setLayoutParams(layoutParams);

        //最小剪切时间1秒
        int pxWidth = (int) (1f/mMediaInfo.vDuration*thumbnailView.getWidth());
        thumbnailView.setMinInterval(pxWidth);
    }

    private void cutVideo() {

        editorTextView = showProgressDialog();

        RxJavaUtil.run(new RxJavaUtil.OnRxAndroidListener<String>() {
            @Override
            public String doInBackground() throws Throwable {
                mMediaPlayer.stop();
                frameSubscribe.dispose();
                myVideoEditor.cancel();
                while (parsingFrame) {
                    Thread.sleep(50);
                }

                float startS = Utils.formatFloat(startTime/1000f);
                float durationS = Utils.formatFloat((endTime-startTime)/1000f);
                return myVideoEditor.executeCutVideoExact(path, startS, durationS);
            }
            @Override
            public void onFinish(String result) {
                closeProgressDialog();
                if(!TextUtils.isEmpty(result)){
                    Intent intent = new Intent();
                    intent.putExtra(RecordedActivity.INTENT_PATH, result);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
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

    private void changeVideoPlay(){
        if(mMediaPlayer != null) {
            mMediaPlayer.seekTo(startTime);
        }
    }

    /**
     * 初始化缩略图
     */
    private void initThumbs(){

        int frameCount = 10;
        final float interval = frameCount/mMediaInfo.vDuration;//提取帧的间隔

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
                }else{
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
                                imageView.setImageBitmap(BitmapFactory.decodeFile(framePath));
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

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(mMediaPlayer!=null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }
private MediaMetadataRetriever media;
    //获取视频总时长
    private int getVideoDuration(String path){
        media = new MediaMetadataRetriever();
        media.setDataSource(path);
        String duration = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); //
        return Integer.parseInt(duration);
    }


    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
}
