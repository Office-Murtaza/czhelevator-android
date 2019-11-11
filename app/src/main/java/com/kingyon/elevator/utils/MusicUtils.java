package com.kingyon.elevator.utils;

import android.media.MediaPlayer;
import android.os.Environment;
import android.text.TextUtils;

import com.kingyon.elevator.application.App;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Created by GongLi on 2018/12/10.
 * Email：lc824767150@163.com
 */

public class MusicUtils {
    private static MusicUtils musicUtils;
    private MediaPlayer mediaPlayer;
    private String musicUrl;
    private String cacheUrl;

    private MusicUtils(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                musicUrl = cacheUrl;
                mp.start();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Logger.e(String.format("播放出错 code:%s extra:%s", what, extra));
                return true;
            }
        });
    }

    public static MusicUtils getInstance() {
        if (musicUtils == null) {
            musicUtils = new MusicUtils(new MediaPlayer());
        }
        return musicUtils;
    }

    public void play(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (TextUtils.equals(url, musicUrl)) {
            mediaPlayer.start();
        } else {
            prepare(url);
        }
    }

    public void pause() {
        mediaPlayer.pause();
//        musicUrl = null;
//        cacheUrl = null;
//        mediaPlayer.reset();
    }

    private void prepare(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getPlayUri(url));
            cacheUrl = url;
            mediaPlayer.prepareAsync();
//            musicUrl = url;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying(String url) {
        return mediaPlayer.isPlaying() && TextUtils.equals(musicUrl, url);
    }

    private String getPlayUri(String url) {
        String result;
        File file = getResourceDownloadFile(url);
        if (file.exists()) {
            result = file.getAbsolutePath();
        } else {
            result = url;
            FileDownloader.getImpl().create(url).setPath(file.getAbsolutePath()).start();
        }
        return result;
    }

    public String getResourceDownloadPath(String url) {
        return getResourceDownloadPath() + File.separator + "ad_bg_voice" + File.separator + getFileName(url);
    }

    public File getResourceDownloadFile(String url) {
        return new File(getResourceDownloadPath(url));
    }

    public File getResourceDownloadPath() {
        return App.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    }

    private String getFileName(String url) {
        String result;
        if (!TextUtils.isEmpty(url)) {
            int index1 = url.lastIndexOf("/") + 1;
//            int index2 = url.lastIndexOf(".");
            if (index1 >= 0) {
                result = url.substring(index1, url.length());
            } else {
                result = "";
            }
        } else {
            result = "";
        }
        return result;
    }

    public void clear() {
        mediaPlayer.pause();
        musicUrl = null;
        cacheUrl = null;
        mediaPlayer.reset();
    }
}

