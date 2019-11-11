package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.leo.afbaselibrary.utils.ScreenUtil;

/**
 * Created by GongLi on 2018/11/2.
 * Email：lc824767150@163.com
 */

public class LeftFlingFrameLayout extends FrameLayout {

    int lastX = 0;
    int lastY = 0;
    private float downX = 0;
    private float downY = 0;

    private int FLING_MIN_DISTANCE = 0;// 移动最小距离
    private boolean leftFling;

    private OnFlingListener onFlingListener;

    public LeftFlingFrameLayout(@NonNull Context context) {
        super(context);
    }

    public LeftFlingFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LeftFlingFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LeftFlingFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (FLING_MIN_DISTANCE == 0) {
            FLING_MIN_DISTANCE = ScreenUtil.getScreenWidth(getContext()) / 8;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
         /*--------解决垂直RecyclerView嵌套水平RecyclerView横向滑动不流畅问题 start --------*/
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int dealtX = 0;
        int dealtY = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dealtX = 0;
                dealtY = 0;
                downX = ev.getRawX();
                downY = ev.getRawY();
                leftFling = false;
                // 保证子View能够接收到Action_move事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX += Math.abs(x - lastX);
                dealtY += Math.abs(y - lastY);
                float distanceX = ev.getRawX() - downX;
                float distanceY = ev.getRawY() - downY;
                // Log.i("dispatchTouchEvent", "dealtX:=" + dealtX);
                // Log.i("dispatchTouchEvent", "dealtY:=" + dealtY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (dealtX > dealtY) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                if (Math.abs(distanceX) > Math.abs(distanceY) && distanceX < -FLING_MIN_DISTANCE && !leftFling) {
                    leftFling = true;
                    onFlingListener.onLeftFling(this);
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                leftFling = false;
                break;
            case MotionEvent.ACTION_UP:
                leftFling = false;
                break;
        }
        /*--------解决垂直RecyclerView嵌套水平RecyclerView横向滑动不流畅问题 end --------*/
//        /*---解决ViewPager嵌套垂直RecyclerView嵌套水平RecyclerView横向滑动到底后不滑动ViewPager start ---*/
//        ViewParent parent = this;
//        while (!((parent = parent.getParent()) instanceof ViewPager)) ;
//        // 循环查找viewPager
//        parent.requestDisallowInterceptTouchEvent(true);
//        /*---解决ViewPager嵌套垂直RecyclerView嵌套水平RecyclerView横向滑动到底后不滑动ViewPager start ---*/
        return super.dispatchTouchEvent(ev);
    }

    public void setOnFlingListener(OnFlingListener onFlingListener) {
        this.onFlingListener = onFlingListener;
    }

    public interface OnFlingListener {
        void onLeftFling(View view);
    }
}
