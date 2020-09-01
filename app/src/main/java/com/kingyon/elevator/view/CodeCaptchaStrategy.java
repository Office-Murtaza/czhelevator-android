package com.kingyon.elevator.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

import com.luozm.captcha.CaptchaStrategy;
import com.luozm.captcha.PositionInfo;
import com.luozm.captcha.Utils;

import java.util.Random;

/**
 * @Created By Admin  on 2020/8/31
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class CodeCaptchaStrategy extends CaptchaStrategy {

    public CodeCaptchaStrategy(Context ctx) {
        super(ctx);
    }

    @Override
    public Path getBlockShape(int blockSize) {
        Path mSuccessPath;
        mSuccessPath = new Path();
        mSuccessPath.moveTo(0, 0);
        mSuccessPath.rLineTo(blockSize, 0);
        mSuccessPath.rLineTo(blockSize , blockSize);
        mSuccessPath.rLineTo(-blockSize, 0);
        mSuccessPath.close();
        return mSuccessPath;
    }

    @Override
    public PositionInfo getBlockPostionInfo(int width, int height, int blockSize) {

        Random random = new Random();
        int edge = Utils.dp2px(getContext(), 50);
        int left = random.nextInt(width - edge);
        //Avoid robot frequently and quickly click the start point to access the captcha.
        if (left < edge) {
            left = edge;
        }
        int top = random.nextInt(height - edge);
        if (top < 0) {
            top = 0;
        }
        return new PositionInfo(left, top);


    }

    @Override
    public Paint getBlockShadowPaint() {
        Paint shadowPaint = new Paint();
        shadowPaint.setColor(Color.parseColor("#000000"));
        shadowPaint.setAlpha(165);
        shadowPaint.setAntiAlias(true);
        return shadowPaint;
    }

    @Override
    public Paint getBlockBitmapPaint() {
        Paint paint = new Paint();
        return paint;

    }

    @Override
    public void decoreateSwipeBlockBitmap(Canvas canvas, Path shape) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(50);
        paint.setPathEffect(new DashPathEffect(new float[]{20,20},10));
        Path path = new Path(shape);
        canvas.drawPath(path,paint);
    }
}
