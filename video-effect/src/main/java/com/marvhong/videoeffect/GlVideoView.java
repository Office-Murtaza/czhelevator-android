/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.marvhong.videoeffect;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.marvhong.videoeffect.filter.base.GlFilter;
import com.marvhong.videoeffect.render.VideoGlRender;

import java.io.IOException;


public class GlVideoView extends GLSurfaceView {

    private VideoGlRender mRenderer;

    private Context mContext;
    private static final String TAG = "MiGuAdVideoView";
    private boolean isReady = false;
    private int position = 0;//续播时间
    private String url = "";
    private MediaPlayer player;
    public  float width1;
    public float height1;
    public GlVideoView(Context context) {
        super(context);
        init(context,null);
    }

    public GlVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);

        player = new MediaPlayer();

        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d(TAG, "surfaceCreated");
                isReady = true;
                player.setDisplay(getHolder());
                if (!"".equals(url) && !player.isPlaying()) {
                    try {
                        player.reset();
                        player.setDataSource(url);
                        player.prepare();
                        player.seekTo(position);
                        Log.d(TAG, "续播时间：" + position);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.d(TAG, "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                isReady = false;

                Log.d(TAG, "surfaceDestroyed");
                if (player.isPlaying()) {
                    position = player.getCurrentPosition();
                    Log.d(TAG, "当前播放时间：" + position);
                    player.stop();
                }
            }
        });

        init(context,attrs);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int hight = MeasureSpec.getSize(heightMeasureSpec);
        if (this.width1>0 ){
            width = (int) width1;
        }
        setMeasuredDimension(width, hight);
    }
    private void init(Context context, AttributeSet attrs) {
        if (!supportsOpenGLES2(context)) {
            throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        }

        mContext = context;

    }

    public void init(IVideoSurface videoSurface){
        GlFilter filter = new GlFilter();
        mRenderer = new VideoGlRender(filter, videoSurface);

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.RGBA_8888);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    /**
     * Checks if OpenGL ES 2.0 is supported on the current device.
     *
     * @param context the context
     * @return true, if successful
     */
    private boolean supportsOpenGLES2(final Context context) {
        final ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x20000;
    }


    /**
     * Set the filter to be applied on the image.
     *
     * @param filter Filter that should be applied on the image.
     */
    public void setFilter(GlFilter filter) {
        mRenderer.setFilter(filter);
    }

    /**
     * Get the current applied filter.
     *
     * @return the current filter
     */
    public GlFilter getFilter() {
        return mRenderer.getFilter();
    }

    public String getVersion(){
        return GPUVideoConst.Version;
    }

    public void setVideoPath(String url) {
        this.url = url;
        if (isReady) {
            try {
                player.reset();
                player.setDataSource(url);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCurrentPosition() {
        if (player != null) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    public void start() {
        if (player != null && !player.isPlaying()) {
            player.start();
        }
    }

    public void seekTo(int startTime) {
        if (player != null && player.isPlaying()) {
            player.seekTo(startTime);
        }
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        if (player != null) {
            player.setOnPreparedListener(listener);
        }
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        if (player != null) {
            player.setOnCompletionListener(listener);
        }
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
        if (player != null) {
            player.setOnErrorListener(listener);
        }
    }

    public void release() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
