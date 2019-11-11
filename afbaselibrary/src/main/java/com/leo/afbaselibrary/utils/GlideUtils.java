package com.leo.afbaselibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.leo.afbaselibrary.R;
import com.leo.afbaselibrary.utils.glide.GlideApp;
import com.leo.afbaselibrary.utils.glide.GlideBlurformation;
import com.leo.afbaselibrary.utils.glide.GlideRoundTransform;
import com.leo.afbaselibrary.utils.glide.VideoFrameBitmapTransformation;

import java.io.File;
import java.io.InputStream;

import okhttp3.OkHttpClient;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * Created by arvin on 2016/5/24
 */
public class GlideUtils {
    public static void loadDrawable(Context context, String url, final BitmapReadyCallBack callBack) {
        GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable.img_loading)
                .dontAnimate()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (callBack != null) {
                            callBack.onExceptoin(e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int w = resource.getIntrinsicWidth();
                        int h = resource.getIntrinsicHeight();
//                        Bitmap.Config config =
//                                resource.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                                        : Bitmap.Config.RGB_565;
//                        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
//                        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
//                        Canvas canvas = new Canvas(bitmap);
//                        resource.setBounds(0, 0, w, h);
//                        resource.draw(canvas);
                        if (callBack != null) {
                            callBack.onBitmapReady(resource, w, h);
                        }
                    }
                });
    }

    public static void loadDrawable(Context context, File file, final BitmapReadyCallBack callBack) {
        GlideApp.with(context)
                .load(file)
                .placeholder(R.drawable.img_loading)
                .dontAnimate()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (callBack != null) {
                            callBack.onExceptoin(e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int w = resource.getIntrinsicWidth();
                        int h = resource.getIntrinsicHeight();
//                        Bitmap.Config config =
//                                resource.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                                        : Bitmap.Config.RGB_565;
//                        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
//                        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
//                        Canvas canvas = new Canvas(bitmap);
//                        resource.setBounds(0, 0, w, h);
//                        resource.draw(canvas);
                        if (callBack != null) {
                            callBack.onBitmapReady(resource, w, h);
                        }
                    }
                });
    }


    /**
     * 加载普通图片（http://或者file://）
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
//        GlideApp.with(context)
//                .load(url)
//                .placeholder(R.drawable.img_loading)
//                .error(R.drawable.img_loading)
//                .into(imageView);
        loadImage(context, url, imageView, R.drawable.img_loading, R.drawable.img_loading);
    }

    public static void loadImage(Context context, String url, ImageView imageView, int holderRes, int errorRes) {
        GlideApp.with(context)
                .load(url)
                .placeholder(holderRes)
                .error(errorRes)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
    }

    /**
     * 加载普通图片（http://或者file://）
     */
    public static void loadAgateImage(Context context, String url, boolean isVideo, ImageView imageView) {
        if (isVideo) {
            if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
                loadVideoFrame(context, url, imageView);
            } else {
                loadLocalFrame(context, url, imageView);
            }
        } else {
//            loadImage(context, url, imageView);
            if (!TextUtils.isEmpty(url) && url.startsWith("http") && isVideo(url)) {
                loadImage(context, String.format("%s?vframe/jpg/offset/%s", url, 1), imageView);
            } else {
                loadImage(context, url, imageView);
            }
        }
    }

    /**
     * 加载普通图片（http://或者file://）
     */
    public static void loadAgateDrawable(Context context, String url, boolean isVideo, BitmapReadyCallBack callBack) {
        if (isVideo) {
            if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
                loadVideoFrameDrawable(context, url, callBack);
            } else {
                loadLocalFrameDrawable(context, url, callBack);
            }
        } else {
//            loadDrawable(context, url, callBack);
            if (!TextUtils.isEmpty(url) && url.startsWith("http") && isVideo(url)) {
                loadDrawable(context, String.format("%s?vframe/jpg/offset/%s", url, 1), callBack);
            } else {
                loadDrawable(context, url, callBack);
            }
        }
    }

    /**
     * 加载普通图片（http://或者file://）
     */
    public static void loadRoundImage(Context context, String url, ImageView imageView, int dp) {
        GlideApp.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new GlideRoundTransform(url, dp)))
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_loading)
                .into(imageView);
    }

    /**
     * 加载普通图片（http://或者file://）高斯模糊
     */
    public static void loadBlurImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new GlideBlurformation(context)))
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_loading)
                .into(imageView);
    }

    /**
     * 加载普通图片 File
     */
    public static void loadImage(Context context, File file, ImageView imageView) {
        GlideApp.with(context)
                .load(file)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_loading)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
    }

    /**
     * 加载为圆形图片（一般为头像加载）
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context).load(url)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_loading)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .circleCrop()
                .into(imageView);
    }

    /**
     * 加载为圆形图片（一般为头像加载）
     */
    public static void loadAvatarImage(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable.img_default_avatar)
                .error(R.drawable.img_default_avatar)
                .circleCrop()
                .into(imageView);
    }

    /**
     * 加载为圆形图片（一般为头像加载）
     */
    public static void loadAvatarImageTransparent(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable.bg_transparent_avatar)
                .error(R.drawable.bg_transparent_avatar)
                .circleCrop()
                .into(imageView);
    }

    /**
     * 加载本地图片（资源文件）
     */
    public static void loadLocalImage(Context context, int resId, ImageView imageView) {
        GlideApp.with(context)
                .load(resId)
                .into(imageView);
    }

    /**
     * 加载本地图片（资源文件）
     */
    public static void loadLocalCircleImage(Context context, int resId, ImageView imageView) {
        GlideApp.with(context)
                .load(resId)
                .circleCrop()
                .into(imageView);
    }

    /**
     * @param okHttpClient 使用Https时的，Net中的okHttpClient
     */
    public static void registerHttps(Context context, OkHttpClient okHttpClient) {
        GlideApp.get(context)
                .getRegistry()
                .replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }


//    /**
//     * 加载普通图片（http://或者file://）
//     */
//    public static void loadImage(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.img_loading).error(R.drawable.img_loading).into(imageView);
//    }
//
//    /**
//     * 加载为圆形图片（一般为头像加载）
//     */
//    public static void loadCircleImage(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).placeholder(R.drawable.img_default_avatar).error(R.drawable.img_default_avatar).
//                transform(new GlideCircleTransform(context)).into(imageView);
//    }
//
//    /**
//     * 加载本地图片（资源文件）
//     */
//    public static void loadLocalImage(Context context, int resId, ImageView imageView) {
//        Glide.with(context).load(resId).into(imageView);
//    }
//
//    /**
//     * 加载本地图片（资源文件）
//     */
//    public static void loadLocalCircleImage(Context context, int resId, ImageView imageView) {
//        Glide.with(context).load(resId).transform(new GlideCircleTransform(context)).into(imageView);
//    }
//
//    /**
//     * @param okHttpClient 使用Https时的，Net中的okHttpClient
//     */
//    public static void registerHttps(Context context, OkHttpClient okHttpClient) {
//        Glide.get(context).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
//    }

    public interface BitmapReadyCallBack {
        void onExceptoin(Exception e);

        void onBitmapReady(Drawable drawable, int width, int height);
    }

    public static void loadVideoFrameDrawable(final Context context, String url, long timeMillis, final BitmapReadyCallBack callBack) {
        loadDrawable(context, String.format("%s?vframe/jpg/offset/%s", url, timeMillis), callBack);
    }

    public static void loadVideoFrameDrawable(Context context, String url, BitmapReadyCallBack callBack) {
        loadVideoFrameDrawable(context, url, 1, callBack);
    }

    public static void loadVideoFrame(final Context context, String url, long timeMillis, ImageView imageView) {
        loadImage(context, String.format("%s?vframe/jpg/offset/%s", url, timeMillis), imageView);
    }

    public static void loadVideoFrame(Context context, String url, ImageView imageView) {
        loadVideoFrame(context, url, 1, imageView);
    }

    public static void loadLocalFrameDrawable(final Context context, String url, long timeMillis, final BitmapReadyCallBack callBack) {
        GlideApp.with(context)
                .load(url)
                .apply(RequestOptions.frameOf(timeMillis)
                        .set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
                        .transform(new VideoFrameBitmapTransformation(url, timeMillis)))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_loading)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (callBack != null) {
                            callBack.onExceptoin(e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int w = resource.getIntrinsicWidth();
                        int h = resource.getIntrinsicHeight();
                        if (callBack != null) {
                            callBack.onBitmapReady(resource, w, h);
                        }
                    }
                });
    }

    public static void loadLocalFrameDrawable(Context context, String url, BitmapReadyCallBack callBack) {
        loadLocalFrameDrawable(context, url, 1, callBack);
    }

    public static void loadLocalFrame(final Context context, String url, long timeMillis, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .apply(RequestOptions.frameOf(timeMillis)
                        .set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
                        .transform(new VideoFrameBitmapTransformation(url, timeMillis)))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_loading)
                .dontAnimate()
                .into(imageView);
    }

    public static void loadLocalFrame(Context context, String url, ImageView imageView) {
        loadLocalFrame(context, url, 1, imageView);
    }


    final static String[] videoSuffix = new String[]{".avi", ".wmv", ".mpeg", ".mp4", ".mov", ".moov", ".mkv", ".flv", ".f4v", ".m4v", ".rmvb", ".rm", ".3gp", ".dat", ".ts", ".mts", ".vob"};

    public static boolean isVideo(String imgLink) {
        boolean result = false;
        if (!TextUtils.isEmpty(imgLink) && imgLink.contains(".")) {
            String suffix = imgLink.substring(imgLink.lastIndexOf("."));
            if (!TextUtils.isEmpty(suffix)) {
                for (int i = 0; i < videoSuffix.length; i++) {
                    if (TextUtils.equals(videoSuffix[i], suffix)) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }
}
