package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.leo.afbaselibrary.utils.ScreenUtil;

/**
 * Created by GongLi on 2017/11/3.
 * Email：lc824767150@163.com
 */

public class DashView extends View {

    private Paint paint;
    private Path path;
    private PathEffect effects;
    private int width;
    private int height;

    public DashView(Context context) {
        super(context);
        init();
    }

    public DashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (paint == null) {
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0xFFE5E5E5);
            effects = new DashPathEffect(new float[]{ScreenUtil.dp2px(7), ScreenUtil.dp2px(4)}, 0);
            paint.setPathEffect(effects);
            path = new Path();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0) {
            width = getWidth();
            paint.setStrokeWidth(width);
            height = getHeight();
        }
        canvas.drawColor(0xFFFFFF);
        path.reset();
        path.moveTo(0, ScreenUtil.dp2px(8));
        path.lineTo(0, height);
        canvas.drawPath(path, paint);
    }
}
