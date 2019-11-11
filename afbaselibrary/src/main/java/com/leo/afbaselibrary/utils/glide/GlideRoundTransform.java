package com.leo.afbaselibrary.utils.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.leo.afbaselibrary.utils.ScreenUtil;

import java.security.MessageDigest;

import javax.xml.transform.Source;

/**
 * Created by GongLi on 2018/6/11.
 * Email：lc824767150@163.com
 */

public class GlideRoundTransform extends BitmapTransformation {
    private float radius = 0f;
    private String url;

    public GlideRoundTransform(String url) {
        this(url, 4);
    }

    public GlideRoundTransform(String url, int dp) {
        super();
        this.url = url;
        radius = ScreenUtil.dp2px(dp);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        try {
            messageDigest.update(String.format("%s__%s", url, radius).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
