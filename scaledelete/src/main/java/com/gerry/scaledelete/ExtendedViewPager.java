package com.gerry.scaledelete;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;


public class ExtendedViewPager extends ViewPager {

    private OnTitleShowListener onTitleShowListener;
    private boolean used;

    public void setOnTitleShowListener(OnTitleShowListener onTitleShowListener) {
        this.onTitleShowListener = onTitleShowListener;
    }

    public ExtendedViewPager(Context context) {
        super(context);
    }

    public ExtendedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof TouchImageView) {
            //
            // canScrollHorizontally is not supported for Api < 14. To get around this issue,
            // ViewPager is extended and canScrollHorizontallyFroyo, a wrapper around
            // canScrollHorizontally supporting Api >= 8, is called.
            //
            return ((TouchImageView) v).canScrollHorizontallyFroyo(-dx);

        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }
    }

    private float downy;
    private float downx;
    private double mTouchSlop;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (onTitleShowListener != null) {
            if (mTouchSlop == 0) {
                mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                used = false;
                downx = event.getX();
                downy = event.getY();
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                float endY = event.getY();
                float endX = event.getX();
                float distanceX = Math.abs(endX - downx);
                float distanceY = Math.abs(endY - downy);
                if (Math.sqrt(distanceX * distanceX + distanceY * (distanceY)) < mTouchSlop) {
                    onTitleShow(false);
                } else {
                    onTitleShow(true);
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float endY = event.getY();
                float endX = event.getX();
                float distanceX = Math.abs(endX - downx);
                float distanceY = Math.abs(endY - downy);
                if (Math.sqrt(distanceX * distanceX + distanceY * (distanceY)) > mTouchSlop) {
                    onTitleShow(true);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void onTitleShow(boolean needHide) {
        if (onTitleShowListener != null && !used) {
            onTitleShowListener.onTitleShow(needHide);
        }
    }

    public interface OnTitleShowListener {
        void onTitleShow(boolean needHide);
    }
}
