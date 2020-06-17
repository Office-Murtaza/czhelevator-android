package com.kingyon.elevator.videocrop;


import android.app.Activity;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created By SongPeng  on 2019/11/22
 * Email : 1531603384@qq.com
 */
public class VideoClipUtils {

    private static final String TAG = VideoClipUtils.class.getSimpleName();

    /**
     * 裁剪视频
     *
     * @param srcPath     需要裁剪的原视频路径
     * @param outPath     裁剪后的视频输出路径
     * @param startTimeMs 裁剪的起始时间
     * @param endTimeMs   裁剪的结束时间
     */
    public static void clip(Activity context, String srcPath, String outPath, double startTimeMs, double endTimeMs, VideoCropListener videoCropListener) {
        LogUtils.d("srcPath="+srcPath+"\noutPath="+outPath+"\nstartTimeMs="+startTimeMs+"\nendTimeMs="+endTimeMs);
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(outPath)) {
            ToastUtils.showShort("视频路径为空");
            return;
        }
        if (!(new File(srcPath).exists())) {
            ToastUtils.showShort("视频文件不存在");
            return;
        }
        if (startTimeMs >= endTimeMs) {
            ToastUtils.showShort("视频起始时间大于结束时间");
            return;
        }
        Looper looper = Looper.getMainLooper();
//        context.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startCrop(looper, srcPath, outPath, startTimeMs, endTimeMs, videoCropListener);

                } catch (IOException e) {
                    Looper.prepare();
                    videoCropListener.cropError();
                    Looper.loop();
                }
            }
        }).start();


    }


    private static void startCrop(Looper looper, String srcPath, String outPath, double startTimeMs, double endTimeMs, VideoCropListener videoCropListener) throws IOException, IllegalArgumentException {
        Movie movie = MovieCreator.build(srcPath);
        List<Track> tracks = movie.getTracks();
        //移除旧的track
        movie.setTracks(new LinkedList<Track>());
        //处理的时间以秒为单位
        double startTime = startTimeMs / 1000;
        double endTime = endTimeMs / 1000;
        Log.d(TAG, "--->>>>startTimeMs = " + startTimeMs + "\n endTimeMs = " + endTimeMs + "\n tracks.size = " + tracks.size());
        //计算剪切时间，视频的采样间隔大，以视频为准
        LogUtils.d(startTime+"==="+endTime);
        for (Track track : tracks) {
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                startTime = correctTimeToSyncSample(track, startTime, false);
                endTime = correctTimeToSyncSample(track, endTime, true);
                LogUtils.d("--->>>>startTime = " + startTime + "\n endTime = " + endTime+"\nstartTime="+startTime+"\nendTime="+endTime);
                if (track.getHandler().equals("vide")) {
                    break;
                }
            }
        }
        Log.d(TAG, "--->>>>startTime = " + startTime + "\n endTime = " + endTime);
        long currentSample;
        double currentTime;
        double lastTime;
        long startSample1;
        long endSample1;
        long delta;

        for (Track track : tracks) {
            currentSample = 0;
            currentTime = 0;
            lastTime = -1;
            startSample1 = -1;
            endSample1 = -1;

            //根据起始时间和截止时间获取起始sample和截止sample的位置
            for (int i = 0; i < track.getSampleDurations().length; i++) {
                delta = track.getSampleDurations()[i];
                if (currentTime > lastTime && currentTime <= startTime) {
                    startSample1 = currentSample;
                }
                if (currentTime > lastTime && currentTime <= endTime) {
                    endSample1 = currentSample;
                }
                lastTime = currentTime;
                currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
                currentSample++;
            }
            Log.d(TAG, "track.getHandler() = " + track.getHandler() + "\n startSample1 = " + startSample1 + "\n endSample1 = " + endSample1);
            if (startSample1 <= 0 && endSample1 <= 0) {
                throw new RuntimeException("clip failed !!");
            }
            movie.addTrack(new CroppedTrack(track, startSample1, endSample1));// 添加截取的track
        }

        //合成视频mp4
        Container out = new DefaultMp4Builder().build(movie);
        FileOutputStream fos = new FileOutputStream(outPath);
        FileChannel fco = fos.getChannel();
        out.writeContainer(fco);
        fco.close();
        fos.close();
        Looper.prepare();
        videoCropListener.cropSuccess();
        Looper.loop();
    }


    /**
     * 换算剪切时间
     *
     * @param track
     * @param cutHere
     * @param next
     * @return
     */
    private static double correctTimeToSyncSample(Track track, double cutHere,
                                                  boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];
            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                // samples always start with 1 but we start with zero therefore
                // +1
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(),
                        currentSample + 1)] = currentTime;
            }
            currentTime += (double) delta
                    / (double) track.getTrackMetaData().getTimescale();
            currentSample++;
        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }


    public static String muxVideoAudio(String videoFilePath, String audioFilePath, String outputFile) {
        try {
            MediaExtractor videoExtractor = new MediaExtractor();
            videoExtractor.setDataSource(videoFilePath);
            MediaExtractor audioExtractor = new MediaExtractor();
            audioExtractor.setDataSource(audioFilePath);
            MediaMuxer muxer = new MediaMuxer(outputFile, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            videoExtractor.selectTrack(0);
            MediaFormat videoFormat = videoExtractor.getTrackFormat(0);
            int videoTrack = muxer.addTrack(videoFormat);
            audioExtractor.selectTrack(0);
            MediaFormat audioFormat = audioExtractor.getTrackFormat(0);
            int audioTrack = muxer.addTrack(audioFormat);
            LogUtils.d(TAG, "Video Format " + videoFormat.toString());
            LogUtils.d(TAG, "Audio Format " + audioFormat.toString());
            boolean sawEOS = false;
            int frameCount = 0;
            int offset = 100;
            int sampleSize = 256 * 1024;
            ByteBuffer videoBuf = ByteBuffer.allocate(sampleSize);
            ByteBuffer audioBuf = ByteBuffer.allocate(sampleSize);
            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();
            videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
            audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
            muxer.start();
            while (!sawEOS) {
                videoBufferInfo.offset = offset;
                videoBufferInfo.size = videoExtractor.readSampleData(videoBuf, offset);
                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
                    sawEOS = true;
                    videoBufferInfo.size = 0;
                } else {
                    videoBufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
                    //noinspection WrongConstant
                    videoBufferInfo.flags = videoExtractor.getSampleFlags();
                    muxer.writeSampleData(videoTrack, videoBuf, videoBufferInfo);
                    videoExtractor.advance();
                    frameCount++;
                }
            }

            boolean sawEOS2 = false;
            int frameCount2 = 0;
            while (!sawEOS2) {
                frameCount2++;
                audioBufferInfo.offset = offset;
                audioBufferInfo.size = audioExtractor.readSampleData(audioBuf, offset);
                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
                    sawEOS2 = true;
                    audioBufferInfo.size = 0;
                } else {
                    audioBufferInfo.presentationTimeUs = audioExtractor.getSampleTime();
                    //noinspection WrongConstant
                    audioBufferInfo.flags = audioExtractor.getSampleFlags();
                    muxer.writeSampleData(audioTrack, audioBuf, audioBufferInfo);
                    audioExtractor.advance();
                }
            }
            muxer.stop();
            muxer.release();
            LogUtils.d(TAG,"Output: "+outputFile);

            return outputFile;
        } catch (IOException e) {
            LogUtils.d(TAG, "Mixer Error 1 " + e.getMessage());
        } catch (Exception e) {
            LogUtils.d(TAG, "Mixer Error 2 " + e.getMessage());
        }
        return "";
    }
}
