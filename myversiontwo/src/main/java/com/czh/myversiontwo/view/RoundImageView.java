package com.czh.myversiontwo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * @ClassName: RoundImageView
 * @Description: java类作用描述
 * @Author: Mr Chen
 * @CreateDate: 2020/3/7 19:07
 */
@SuppressLint("AppCompatCustomView")
public class RoundImageView extends ImageView {

    private int radius = 20;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), radius, radius, Path.Direction.CW);
        canvas.clipPath(path);//设置可显示的区域，canvas四个角会被剪裁掉
        super.onDraw(canvas);
    }
}

