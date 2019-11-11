package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.orhanobut.logger.Logger;

/**
 * Created by GongLi on 2018/6/11.
 * Email：lc824767150@163.com
 */

public class BlankRecyclerView extends RecyclerView {

    private GestureDetectorCompat gestureDetector;
    private BlankListener listener;

    public BlankRecyclerView(Context context) {
        super(context);
    }

    public BlankRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlankRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBlankListener(BlankListener listener) {
        this.listener = listener;
        this.gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (gestureDetector != null) {
            if (gestureDetector.onTouchEvent(e)) {
                View childView = findChildViewUnder(e.getX(), e.getY());
                if (childView == null) {
                    listener.onBlankClick(this);
                    return true;
                }
            }
        } else {
            Logger.e("没有设置点击事件");
        }
        return super.onTouchEvent(e);
    }

    public interface BlankListener {
        void onBlankClick(BlankRecyclerView recyclerView);
    }
}
