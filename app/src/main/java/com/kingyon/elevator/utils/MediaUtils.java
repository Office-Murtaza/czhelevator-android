package com.kingyon.elevator.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.orhanobut.logger.Logger;

import java.util.HashMap;

/**
 * Created by GongLi on 2018/11/1.
 * Email：lc824767150@163.com
 */

public class MediaUtils {

    private static MediaUtils mediaUtils;

    private MediaUtils() {
    }

    public static MediaUtils getInstance() {
        if (mediaUtils == null) {
            mediaUtils = new MediaUtils();
        }
        return mediaUtils;
    }

    public long getVideoDuring(String mUri) {
        long duration = 0;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        if (mUri != null) {
            try {
                if (mUri.startsWith("http")) {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                    mmr.setDataSource(mUri, headers);
                } else {
                    mmr.setDataSource(mUri);
                }
                duration = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            } catch (Exception ex) {
                Logger.w("MediaUtils getVideoDuring(String mUri) 出错");
            } finally {
                mmr.release();
            }
        } else {
            Logger.w("MediaUtils getVideoSize(String mUri, int[] size) mUri == null");
        }
        return duration;
    }

    public int getVideoWidth(String mUri) {
        int width = 0;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        if (mUri != null) {
            try {
                if (mUri.startsWith("http")) {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                    mmr.setDataSource(mUri, headers);
                } else {
                    mmr.setDataSource(mUri);
                }
                width = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            } catch (Exception ex) {
                Logger.w("MediaUtils getVideoWidth(String mUri) 出错");
            } finally {
                mmr.release();
            }
        } else {
            Logger.w("MediaUtils getVideoSize(String mUri, int[] size) mUri == null");
        }
        return width;
    }

    public int getVideoHeight(String mUri) {
        int height = 0;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        if (mUri != null) {
            try {
                if (mUri.startsWith("http")) {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                    mmr.setDataSource(mUri, headers);
                } else {
                    mmr.setDataSource(mUri);
                }
                height = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            } catch (Exception ex) {
                Logger.w("MediaUtils getVideoHeight(String mUri) 出错");
            } finally {
                mmr.release();
            }
        } else {
            Logger.w("MediaUtils getVideoSize(String mUri, int[] size) mUri == null");
        }
        return height;
    }

    public int[] getVideoSize(String mUri, int[] size) {
        if (size == null || size.length != 2) {
            size = new int[2];
        }
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        if (mUri != null) {
            try {
                if (mUri.startsWith("http")) {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                    mmr.setDataSource(mUri, headers);
                } else {
                    mmr.setDataSource(mUri);
                }
                size[0] = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                size[1] = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            } catch (Exception ex) {
                Logger.w("MediaUtils getVideoSize(String mUri, int[] size) 出错");
            } finally {
                mmr.release();
            }
        } else {
            Logger.w("MediaUtils getVideoSize(String mUri, int[] size) mUri == null");
        }
        return size;
    }

    public float getVideoRatio(String mUri) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        float ratio = 0;
        if (mUri != null) {
            try {
                if (mUri.startsWith("http")) {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                    mmr.setDataSource(mUri, headers);
                } else {
                    mmr.setDataSource(mUri);
                }
                float width = 0;
                float height = 0;
//                float width = Float.parseFloat(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//                float height = Float.parseFloat(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                Bitmap frameBitmap = mmr.getFrameAtTime();
                if (frameBitmap != null) {
                    width = frameBitmap.getWidth();
                    height = frameBitmap.getHeight();
                    frameBitmap.recycle();
                }
                ratio = width / height;
                if (Float.isNaN(ratio) || Float.isInfinite(ratio)) {
                    ratio = 0;
                    Logger.w("MediaUtils getVideoSize(String mUri, int[] size) 出错");
                }
            } catch (Exception ex) {
                Logger.w("MediaUtils getVideoSize(String mUri, int[] size) 出错");
            } finally {
                mmr.release();
            }
        } else {
            Logger.w("MediaUtils getVideoSize(String mUri, int[] size) mUri == null");
        }
        return ratio;
    }


}
