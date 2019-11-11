package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by GongLi on 2018/12/28.
 * Email：lc824767150@163.com
 */

public class RatioFrameLayoutByWidth extends FrameLayout {
    public RatioFrameLayoutByWidth(@NonNull Context context) {
        super(context);
    }

    public RatioFrameLayoutByWidth(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioFrameLayoutByWidth(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
