package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.kingyon.elevator.R;
import com.leo.afbaselibrary.utils.ScreenUtil;

/**
 * Created by GongLi on 2018/8/27.
 * Email：lc824767150@163.com
 */

public class CustomProgressView extends View {
    private float width;
    private float height;

    private Paint paint;
    private Paint shaderPaint;

    private String text;
    private int textSize = ScreenUtil.sp2px(14);
    private int textColor;
    private float percent;
    private int startColor;
    private int endColor;
    private int backgroundColor;

    private RectF backgroundRect;

    private int[] progressColors;
    private float[] progressPositions;
    private RectF progressRect;

    private int baselineText;

    private float radius = ScreenUtil.dp2px(2);

    public CustomProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            shaderPaint = new Paint();
            shaderPaint.setAntiAlias(true);
            shaderPaint.setDither(true);
        }
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomProgressView);
            text = typedArray.getString(R.styleable.CustomProgressView_text);
            textColor = typedArray.getColor(R.styleable.CustomProgressView_textColor, textColor);
            textSize = typedArray.getDimensionPixelSize(R.styleable.CustomProgressView_textSize, textSize);
            percent = typedArray.getFloat(R.styleable.CustomProgressView_percent, percent);
            startColor = typedArray.getColor(R.styleable.CustomProgressView_startColor, startColor);
            endColor = typedArray.getColor(R.styleable.CustomProgressView_endColor, endColor);
            backgroundColor = typedArray.getColor(R.styleable.CustomProgressView_backgroundColor, backgroundColor);
            radius = typedArray.getDimensionPixelSize(R.styleable.CustomProgressView_radius, (int) radius);
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width == 0) {
            width = getWidth() - getPaddingLeft() - getPaddingRight();
            height = getHeight() - getPaddingTop() - getPaddingBottom();
            if (radius < 0) {
                radius = 0;
            }
            if (radius > height / 2) {
                radius = height / 2;
            }
        }

        canvas.drawColor(0x00FFFFFF);

        if (backgroundRect == null) {
            backgroundRect = new RectF(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + width, getPaddingTop() + height);
        } else {
            backgroundRect.left = getPaddingLeft();
            backgroundRect.top = getPaddingTop();
            backgroundRect.right = getPaddingLeft() + width;
            backgroundRect.bottom = getPaddingTop() + height;
        }
        paint.setColor(0xFFFFFFFF);
        canvas.drawRoundRect(backgroundRect, radius, radius, paint);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(backgroundRect, radius, radius, paint);


        if (progressColors == null) {
            progressColors = new int[]{startColor, endColor};
        }
        if (progressPositions == null) {
            progressPositions = new float[]{0, 1};
        }
        LinearGradient progressShader = new LinearGradient(
                getPaddingLeft(), 0,
                getPaddingLeft() + width * percent, 0,
                progressColors,
                progressPositions,
                Shader.TileMode.CLAMP);
        if (progressRect == null) {
            progressRect = new RectF(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + width * percent, getPaddingTop() + height);
        } else {
            progressRect.right = getPaddingLeft() + width * percent;
        }
        shaderPaint.setShader(progressShader);
        canvas.drawRoundRect(progressRect, radius, radius, shaderPaint);

        paint.setTextSize(textSize);
        paint.setColor(textColor);
        float textWidth = paint.measureText(text == null ? "" : text);
        if (baselineText == 0) {
            Paint.FontMetricsInt fontMetricsText = paint.getFontMetricsInt();
            baselineText = (int) ((getPaddingTop() + height + getPaddingTop() - fontMetricsText.bottom - fontMetricsText.top) / 2);
        }
        canvas.drawText(text == null ? "" : text, getPaddingLeft() + (width - textWidth) / 2, baselineText, paint);
    }

    public void setColor(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setPercent(float percent) {
        if (Float.isNaN(percent)) {
            percent = 1;
        }
        if (Float.isInfinite(percent)) {
            percent = 1;
        }
        this.percent = percent < 0 ? 0 : (percent > 1 ? 1 : percent);
        invalidate();
    }

    public void setParams(float percent, String text) {
        if (Float.isNaN(percent)) {
            percent = 1;
        }
        if (Float.isInfinite(percent)) {
            percent = 1;
        }
        this.percent = percent < 0 ? 0 : (percent > 1 ? 1 : percent);
        this.text = text;
        invalidate();
    }

    public String getText() {
        return text;
    }

    public float getPercent() {
        return percent;
    }
}
