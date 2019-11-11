package com.kingyon.elevator.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.App;
import com.leo.afbaselibrary.utils.ScreenUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Created by GongLi on 2017/12/6.
 * Email：lc824767150@163.com
 */

public class ViewBitmapUtils {
    public static Bitmap createBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        view.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        return bitmap;
    }

    public static Bitmap createBitmapOnHide(View v, int shareSize) {
        int w = v.getWidth();
        int h = v.getHeight() - shareSize;
        if (h < 0) {
            h = 0;
        }
        v.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        v.draw(c);
        v.setLayerType(View.LAYER_TYPE_NONE, null);
        return bmp;
    }

    public static byte[] createBitmapOnHideToBytes(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
//        c.drawColor(0x00ffffff);
        v.layout(0, 0, w, h);
        v.draw(c);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        bmp.recycle();
        return bytes;
    }

    public static Bitmap createBitmapWithWaterMark(View v, Bitmap watermark, int color) {
        int watermarkMargin = ScreenUtil.dp2px(12);
        int w = v.getWidth();
        int h = v.getHeight();
        int bW = watermark.getWidth();
        int bH = watermark.getHeight();
        int realW = w > bW ? w : bW;
        int realH = h + bH + watermarkMargin;
        Bitmap bmp = Bitmap.createBitmap(realW, realH, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
        c.drawColor(color);
        v.layout((realW - w) / 2, 0, (realW + w) / 2, h);
        v.draw(c);
        c.drawBitmap(watermark, (realW - bW) / 2, h, null);
        return bmp;
    }

//    public static byte[] bitmapAddHaloToBytes(Bitmap src) {
//        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
//        Paint p = new Paint();
//        p.setColor(0x42000000);
//        canvas.drawBitmap(src.extractAlpha(), 0, 0, p);
//        StateListDrawable sld = new StateListDrawable();
//        sld.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(bitmap));
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }

//    public static Bitmap createBitmapWithWaterMark(Context context, View v, int watermarkId, int leftShadowId, int topShadoeId, int rightShadowId, int bottomShadowId) {
//        //获取水印和阴影bitmap
//        Resources resources = context.getResources();
//        Bitmap watermark = BitmapFactory.decodeResource(resources, watermarkId);
//        Bitmap leftShadow = BitmapFactory.decodeResource(resources, leftShadowId);
//        Bitmap topShadow = BitmapFactory.decodeResource(resources, topShadoeId);
//        Bitmap rightShadow = BitmapFactory.decodeResource(resources, rightShadowId);
//        Bitmap bottomShadow = BitmapFactory.decodeResource(resources, bottomShadowId);
//
//
//        //生成宽高空bitmap
//        int watermarkMargin = ScreenUtil.dp2px(8);
//
//        int srcW = v.getWidth();
//        int srcH = v.getHeight();
//
//        int watermarkW = watermark.getWidth();
//        int watermarkH = watermark.getHeight();
//
//        int leftW = leftShadow.getWidth();
//        int leftH = leftShadow.getHeight();
//
//        int topW = topShadow.getWidth();
//        int topH = topShadow.getHeight();
//
//        int rightW = rightShadow.getWidth();
//        int rightH = rightShadow.getHeight();
//
//        int bottomW = bottomShadow.getWidth();
//        int bottomH = bottomShadow.getHeight();
//
//        int imageW = srcW > watermarkW ? srcW : watermarkW;
//        int imageH = srcH + watermarkH + watermarkMargin;
//
//        int realW = imageW + leftW + rightW;
//        int realH = imageH + topH + bottomH;
//
//        Bitmap bmp = Bitmap.createBitmap(realW, realH, Bitmap.Config.RGB_565);
//
//
//        //画view
//        Canvas c = new Canvas(bmp);
//        c.drawColor(Color.WHITE);
//        v.layout((imageW - srcW) / 2 + leftW, topH, (realW + srcW) / 2 + leftW, topH + srcH);
//        v.draw(c);
//        //画水印
//        c.drawBitmap(watermark, (realW - watermarkW) / 2 + leftW, topH + srcH + watermarkMargin / 2, null);
//        //画左阴影
//        c.drawBitmap(leftShadow, 0, 0, null);
//        //画上阴影
//        c.drawBitmap(topShadow, 0, topH, null);
//        //画右阴影
//        c.drawBitmap(rightShadow, leftW + imageW, 0, null);
//        //画下阴影
//        c.drawBitmap(bottomShadow, 0, topH + imageH, null);
//
//        return bmp;
//    }

    public static String getPath(Context context) {
        return String.format("%s%s%s%s%s"
                , Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                , File.separator
                , context.getString(R.string.app_name)
                , File.separator
                , "template");
    }

    public static File saveBitmap(Context context, Bitmap bitmap, String path, long objectId) {
        return saveBitmap(context, bitmap, path, getUploadFileName(objectId));
    }

    public static File saveBitmap(Context context, Bitmap bitmap, long objectId) {
        return saveBitmap(context, bitmap, getPath(context), objectId);
    }

    public static File saveBitmap(Context context, Bitmap bitmap, String path, String imageName) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File imageFile;
        FileOutputStream out = null;
        try {
            imageFile = new File(file, imageName);
            if (imageFile.exists()) {
                imageFile.delete();
            }
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(imageFile);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                uri = FileProvider.getUriForFile(context, AFUtil.getAppProcessName(context) + ".provider", imageFile);
//            } else {
//                uri = Uri.fromFile(imageFile);
//            }
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (IOException e) {
            e.printStackTrace();
            imageFile = null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    imageFile = null;
                }
            }
        }
//        File result;
//        try {
//            result = Luban.with(context).load(imageFile).get();
//            Logger.i(String.format("compress %s MB", result.length() / 1024 / 1024));
//        } catch (IOException e) {
//            e.printStackTrace();
//            result = imageFile;
//        }
        return imageFile;
    }

    private static String getUploadFileName(long objectId) {
        //return UUID.randomUUID() + "-" + System.currentTimeMillis() + ".png";
//        return System.currentTimeMillis() + ".jpg";
        return String.format("template_%s_%s.jpg", CommonUtil.getShareDigits(objectId), String.valueOf(UUID.randomUUID()));
    }

    public static Bitmap dealCrowdfundingPiicBitmap(Bitmap bitmapTop, Bitmap bitmapBottom) {
        int screenWidth = ScreenUtil.getScreenWidth(App.getContext());
        int topWidth = bitmapTop.getWidth();
        int topHeight = (int) (bitmapTop.getHeight() * (float) screenWidth / topWidth);
        int bottomWidth = bitmapBottom.getWidth();
        int bottomHeight = (int) (bitmapBottom.getHeight() * (float) screenWidth / bottomWidth);

        //创建画布
        Bitmap bitmap = Bitmap.createBitmap(screenWidth, topHeight + bottomHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        //画布背景
        canvas.drawColor(0xFFFFFFFF);

        //画上半部分
        Rect rectTop = new Rect(0, 0, screenWidth, topHeight);
        canvas.drawBitmap(bitmapTop, null, rectTop, null);

        //画下半部分
        Rect rectBottom = new Rect(0, topHeight, screenWidth, topHeight + bottomHeight);
        canvas.drawBitmap(bitmapBottom, null, rectBottom, null);

        return bitmap;
    }

    public static Bitmap createBitmapPiiic(Context context, Bitmap src, int width, int height, String titleText, String othersText, String link) {

        int padding = ScreenUtil.dp2px(16);

        int screenWidth = ScreenUtil.getScreenWidth(App.getContext());
        int coverWidth = screenWidth;
        int coverHeight = (int) (coverWidth * height / (float) width);
        int totalWidth = screenWidth;
        int qrCodeSize = 2 * ScreenUtil.sp2px(14) + ScreenUtil.dp2px(20) + ScreenUtil.sp2px(12);
        int totalHeight = coverHeight + padding + ScreenUtil.dp2px(20) + qrCodeSize + ScreenUtil.dp2px(4);

        //创建画布
        Bitmap bitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //Bitmap bitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.RGB_565);
        //Canvas canvas = new Canvas(bmp);

        //画布背景
        canvas.drawColor(0xFFFFFFFF);

        //绘制封面图
        Rect rectSrc = new Rect(0, 0, coverWidth, coverHeight);
        canvas.drawBitmap(src, null, rectSrc, null);

        //绘制二维码
        String shareLink = String.valueOf(link);
        Bitmap qrBitmap = createQrCode(context, shareLink, qrCodeSize, qrCodeSize);
        if (qrBitmap != null) {
            Rect rect = new Rect(totalWidth - padding - qrCodeSize, coverHeight + ScreenUtil.dp2px(20), totalWidth - padding, coverHeight + ScreenUtil.dp2px(20) + qrCodeSize);
            canvas.drawBitmap(qrBitmap, null, rect, null);
        }

        //绘制标题
        int textAllowWidth = coverWidth - qrCodeSize - 3 * padding;
        String title = String.valueOf(titleText);
        if (!TextUtils.isEmpty(title)) {
            TextPaint titlePaint = new TextPaint();
            titlePaint.setAntiAlias(true);
            titlePaint.setColor(0xFF333333);
            titlePaint.setStrokeWidth(ScreenUtil.dp2px(0.6f));
            titlePaint.setStyle(Paint.Style.FILL);
            titlePaint.setTextSize(ScreenUtil.sp2px(14));
            int maxLength = calculateMaxlength(titlePaint, title, 2, textAllowWidth);
            if (maxLength > 1 && title.length() > maxLength) {
                title = String.format("%s...", title.substring(0, maxLength));
            }
            StaticLayout titltStatic = new StaticLayout(title, titlePaint, textAllowWidth, Layout.Alignment.ALIGN_NORMAL, 1.1f, 0.0f, false);
            canvas.save();
            canvas.translate(padding, coverHeight + ScreenUtil.dp2px(20));
            titltStatic.draw(canvas);
            canvas.restore();
        }

        //绘制其它文本
        String others = othersText;
        if (!TextUtils.isEmpty(others)) {
            TextPaint otherPaint = new TextPaint();
            otherPaint.setAntiAlias(true);
            otherPaint.setColor(0xFF333333);
            otherPaint.setStrokeWidth(ScreenUtil.dp2px(0.6f));
            otherPaint.setStyle(Paint.Style.FILL);
            otherPaint.setTextSize(ScreenUtil.sp2px(12));
            int maxLength = calculateMaxlength(otherPaint, others, 1, textAllowWidth);
            if (maxLength > 1 && others.length() > maxLength) {
                others = String.format("%s...", others.substring(0, maxLength));
            }
            StaticLayout otherStatic = new StaticLayout(others, otherPaint, textAllowWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            canvas.save();
            canvas.translate(padding, coverHeight + ScreenUtil.dp2px(20) + 2 * ScreenUtil.sp2px(14) + ScreenUtil.dp2px(20));
            otherStatic.draw(canvas);
            canvas.restore();
        }
        return bitmap;
    }

    public static Bitmap createBitmapCover(Context context, Drawable drawable, int width, int height, String titleText, String othersText, String link) {

        int padding = ScreenUtil.dp2px(16);

        int screenWidth = ScreenUtil.getScreenWidth(App.getContext());
        int validWidth = screenWidth - 2 * padding;
        int coverWidth = validWidth;
        int coverHeight = (int) (validWidth * height / (float) width);
        int totalWidth = screenWidth;
        int qrCodeSize = 2 * ScreenUtil.sp2px(14) + ScreenUtil.dp2px(20) + ScreenUtil.sp2px(12);
        int totalHeight = coverHeight + 2 * padding + ScreenUtil.dp2px(20) + qrCodeSize + ScreenUtil.dp2px(4);


        //创建画布
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.RGB_565
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(totalWidth, totalHeight, config);
        Canvas canvas = new Canvas(bitmap);
        //Bitmap bitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.RGB_565);
        //Canvas canvas = new Canvas(bmp);

        //画布背景
        canvas.drawColor(0xFFFFFFFF);

        //绘制封面图
        drawable.setBounds(padding, padding, padding + coverWidth, padding + coverHeight);
        drawable.draw(canvas);

        //绘制二维码
        String shareLink = String.valueOf(link);
        Bitmap qrBitmap = createQrCode(context, shareLink, qrCodeSize, qrCodeSize);
        if (qrBitmap != null) {
            Rect rect = new Rect(totalWidth - padding - qrCodeSize, padding + coverHeight + ScreenUtil.dp2px(20), totalWidth - padding, padding + coverHeight + ScreenUtil.dp2px(20) + qrCodeSize);
            canvas.drawBitmap(qrBitmap, null, rect, null);
        }

        //绘制标题
        int textAllowWidth = coverWidth - qrCodeSize - padding;
        String title = String.valueOf(titleText);
        if (!TextUtils.isEmpty(title)) {
            TextPaint titlePaint = new TextPaint();
            titlePaint.setAntiAlias(true);
            titlePaint.setColor(0xFF333333);
            titlePaint.setStrokeWidth(ScreenUtil.dp2px(0.6f));
            titlePaint.setStyle(Paint.Style.FILL);
            titlePaint.setTextSize(ScreenUtil.sp2px(14));
            int maxLength = calculateMaxlength(titlePaint, title, 2, textAllowWidth);
            if (maxLength > 1 && title.length() > maxLength) {
                title = String.format("%s...", title.substring(0, maxLength - 1));
            }
            StaticLayout titltStatic = new StaticLayout(title, titlePaint, textAllowWidth, Layout.Alignment.ALIGN_NORMAL, 1.1f, 0.0f, false);
            canvas.save();
            canvas.translate(padding, padding + coverHeight + ScreenUtil.dp2px(20));
            titltStatic.draw(canvas);
            canvas.restore();
        }

        //绘制其它文本
        String others = othersText;
        if (!TextUtils.isEmpty(others)) {
            TextPaint otherPaint = new TextPaint();
            otherPaint.setAntiAlias(true);
            otherPaint.setColor(0xFF333333);
            otherPaint.setStrokeWidth(ScreenUtil.dp2px(0.6f));
            otherPaint.setStyle(Paint.Style.FILL);
            otherPaint.setTextSize(ScreenUtil.sp2px(12));
            int maxLength = calculateMaxlength(otherPaint, others, 1, textAllowWidth);
            if (maxLength > 1 && others.length() > maxLength) {
                others = String.format("%s...", others.substring(0, maxLength - 1));
            }
            StaticLayout otherStatic = new StaticLayout(others, otherPaint, textAllowWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            canvas.save();
            canvas.translate(padding, padding + coverHeight + ScreenUtil.dp2px(20) + 2 * ScreenUtil.sp2px(14) + ScreenUtil.dp2px(20));
            otherStatic.draw(canvas);
            canvas.restore();
        }
        return bitmap;
    }

    private static int calculateMaxlength(TextPaint paint, String text, int lines, int textAllowWidth) {
        String copyText = String.valueOf(text);
        int result = 0;
        int length = TextUtils.isEmpty(copyText) ? 0 : copyText.length();
        if (length > 0) {
            for (int i = 0; i < lines; i++) {
                int lineResult = calculateLineMaxLength(paint, copyText, textAllowWidth);
                if (lineResult > 0 && lineResult < copyText.length()) {
                    copyText = copyText.substring(lineResult - 1, copyText.length());
                } else {
                    copyText = "";
                }
                result += lineResult;
            }
        }
        return result;
    }

    private static int calculateLineMaxLength(TextPaint paint, String text, int textAllowWidth) {
        String copyText = String.valueOf(text);
        int result = 0;
        int length = TextUtils.isEmpty(copyText) ? 0 : copyText.length();
        if (length > 0) {
            for (int i = 1; i <= length; i++) {
                float width = paint.measureText(copyText.substring(0, i));
                if (width > textAllowWidth) {
                    break;
                }
                result = i;
            }
        }
        return result;
    }

    private static Bitmap createQrCode(Context context, String shareLink, int width, int height) {
        Bitmap result = null;
        if (!TextUtils.isEmpty(shareLink)) {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                hints.put(EncodeHintType.MARGIN, 1);
                BitMatrix matrix = multiFormatWriter.encode(shareLink, BarcodeFormat.QR_CODE, width, height);

                matrix = deleteWhite(matrix);//删除白边
                width = matrix.getWidth();
                height = matrix.getHeight();
                int[] pixels = new int[width * height];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        if (matrix.get(x, y)) {
                            pixels[y * width + x] = Color.BLACK;
                        } else {
                            pixels[y * width + x] = Color.WHITE;
                        }
                    }
                }
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

                result = addLogo(bitmap, BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                if (result == null) {
                    result = bitmap;
                }

//                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                bitmap = barcodeEncoder.createBitmap(result);
            } catch (WriterException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException iae) {
                return null;
            }
        }
        return result;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.RGB_565);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }
}
