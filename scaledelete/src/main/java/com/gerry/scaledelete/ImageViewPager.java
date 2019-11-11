package com.gerry.scaledelete;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by GongLi on 2017/11/26.
 * Email：lc824767150@163.com
 */

public class ImageViewPager extends ViewPager {
    private float downy;
    private float downx;
    private double mTouchSlop = 100;

    public ImageViewPager(Context context) {
        super(context);
    }

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downx = event.getX();
            downy = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float endY = event.getY();
            float endX = event.getX();
            float distanceX = Math.abs(endX - downx);
            float distanceY = Math.abs(endY - downy);
            // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
            if (Math.sqrt(distanceX * distanceX + distanceY * (distanceY)) < mTouchSlop) {
                Log.d("aaaaaaaaaa", "aaaaaaaaa");
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
