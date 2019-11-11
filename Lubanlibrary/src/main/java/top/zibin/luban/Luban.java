package top.zibin.luban;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Luban implements Handler.Callback {
    private static final String TAG = "Luban";
    private static final String DEFAULT_DISK_CACHE_DIR = "luban_disk_cache";

    private static final int MSG_COMPRESS_SUCCESS = 0;
    private static final int MSG_COMPRESS_START = 1;
    private static final int MSG_COMPRESS_ERROR = 2;

    private File file;
    private OnCompressListener onCompressListener;

    private Handler mHandler;

    private Luban(Builder builder) {
        this.file = builder.file;
        this.onCompressListener = builder.onCompressListener;
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    /**
     * Returns a file with a cache audio name in the private cache directory.
     *
     * @param context A context.
     */
    private File getImageCacheFile(Context context) {
        if (getImageCacheDir(context) != null) {
            return new File(getImageCacheDir(context) + "/" + System.currentTimeMillis() + (int) (Math.random() * 100) + file.getName().substring(file.getName().lastIndexOf(".") - 1));
        }
        return null;
    }

    /**
     * Returns a directory with a default name in the private cache directory of the application to
     * use to store retrieved audio.
     *
     * @param context A context.
     * @see #getImageCacheDir(Context, String)
     */
    @Nullable
    private File getImageCacheDir(Context context) {
        return getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR);
    }

    /**
     * Returns a directory with the given name in the private cache directory of the application to
     * use to store retrieved media and thumbnails.
     *
     * @param context   A context.
     * @param cacheName The name of the subdirectory in which to store the cache.
     * @see #getImageCacheDir(Context)
     */
    @Nullable
    private File getImageCacheDir(Context context, String cacheName) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                // File wasn't able to create a directory, or the result exists but not a directory
                return null;
            }
            return result;
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "default disk cache dir is null");
        }
        return null;
    }

    /**
     * start asynchronous compress thread
     */
    @UiThread
    private void launch(final Context context) {
        if (file == null && onCompressListener != null) {
            onCompressListener.onError(new NullPointerException("image file cannot be null"));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_START));

                    File result = new Engine(file, getImageCacheFile(context)).compress();
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_SUCCESS, result));
                } catch (IOException e) {
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_ERROR, e));
                }
            }
        }).start();
    }

    /**
     * start compress and return the file
     */
    @WorkerThread
    private File get(final Context context) throws IOException {
        return new Engine(file, getImageCacheFile(context)).compress();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (onCompressListener == null) return false;

        switch (msg.what) {
            case MSG_COMPRESS_START:
                onCompressListener.onStart();
                break;
            case MSG_COMPRESS_SUCCESS:
                onCompressListener.onSuccess((File) msg.obj);
                break;
            case MSG_COMPRESS_ERROR:
                onCompressListener.onError((Throwable) msg.obj);
                break;
        }
        return false;
    }

    public static class Builder {
        private Context context;
        private File file;
        private OnCompressListener onCompressListener;

        Builder(Context context) {
            this.context = context;
        }

        private Luban build() {
            return new Luban(this);
        }

        public Builder load(File file) {
            this.file = file;
            return this;
        }

        public Builder putGear(int gear) {
            return this;
        }

        public Builder setCompressListener(OnCompressListener listener) {
            this.onCompressListener = listener;
            return this;
        }

        public void launch() {
            build().launch(context);
        }

        public File get() throws IOException {
            return build().get(context);
        }

        public void clear() {
            build().clear(context);
        }

        public void clearAsync(LuBanClearListener luBanClearListener) {
            build().clearAsync(context, luBanClearListener);
        }

        public long getCacheSize() {
            return build().getCacheSize(context);
        }
    }

    /**
     * 获取缓存大小
     *
     * @param context
     */
    public long getCacheSize(Context context) {
        File file = getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR);
        long size = 0;
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                size = getFolderSize(file);
            } else {
                size = file.length();
            }
        }
        return size;
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private long getFolderSize(File file) {
        long size = 0;
        File[] fileList = file.listFiles();
        for (File aFileList : fileList) {
            if (aFileList.isDirectory()) {
                size = size + getFolderSize(aFileList);
            } else {
                size = size + aFileList.length();
            }
        }
        return size;
    }

    /**
     * 清除压缩后的图片
     *
     * @param context
     */
    public boolean clear(final Context context) {
        File file = getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR);
        boolean flag = true;
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                flag = deleteFilesInDir(file);
            } else if (file.isFile()) {
                flag = file.delete();
            } else {
                return false;
            }
        } else {
            return false;
        }
        return flag;
    }


    /**
     * 清除压缩后的图片
     *
     * @param context
     * @param listener
     */
    public void clearAsync(final Context context, final LuBanClearListener listener) {
        Thread clearThread = new Thread(new Runnable() {
            @Override
            public void run() {
                File file = getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR);
                boolean flag = true;
                if (file != null && file.exists()) {
                    if (file.isDirectory()) {
                        flag = deleteFilesInDir(file);
                    } else if (file.isFile()) {
                        flag = file.delete();
                    } else {
                        flag = false;
                    }
                } else {
                    flag = false;
                }
                if (listener != null) {
                    listener.deleted(flag);
                }
            }
        });
        clearThread.start();
    }

    private boolean deleteFilesInDir(final File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return true;
    }

    private boolean deleteDir(final File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }
}