package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by gongli on 2017/3/29 12:32
 * email: lc824767150@163.com
 * 宽度保持与高度一致的ImageView
 */

public class RatioImageViewByHeight extends ImageView {
    public RatioImageViewByHeight(Context context) {
        super(context);
    }

    public RatioImageViewByHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageViewByHeight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}