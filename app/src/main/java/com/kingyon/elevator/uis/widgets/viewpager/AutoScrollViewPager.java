package com.kingyon.elevator.uis.widgets.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gongli on 2017/5/18 14:49
 * email: lc824767150@163.com
 * <p>
 * 自定义可定时自动滑动的w无限循环ViewPager
 * 同时满足手动滑动的要求（解决了自动滑动和手动滑动的冲突）
 */

public class AutoScrollViewPager extends ViewPager {

    private Timer timer;
    private TimerTask timerTask;
    private int time;
    private static final int DURATION = 800;
    private PagerAdapter baseAdapter;
//    private boolean isOne;

    public AutoScrollViewPager(Context context) {
        super(context);
        init();
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 重写setAdapter让使用时只需要向正常使用PagerAdapter
     * （不能是FragmentPagerAdapter等）那样进行就可以实现自动滚动
     *
     * @param adapter 普通的ViewPager的Adapter
     */
    @Override
    public void setAdapter(final PagerAdapter adapter) {
        this.baseAdapter = adapter;
        if (timer == null)
            timer = new Timer();
        if (time <= 0)
            time = 3000;
//        if (adapter.getCount() > 1) {
//            isOne = false;
        super.setAdapter(new PagerAdapter() {
            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return baseAdapter.getCount() > 1 ? Integer.MAX_VALUE : baseAdapter.getCount();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return baseAdapter.isViewFromObject(view, object);
            }


            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                return baseAdapter.instantiateItem(container, position % adapter.getCount());
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                baseAdapter.destroyItem(container, position % adapter.getCount(), object);
            }
        });
        setCurrentItem(baseAdapter.getCount() * 500, false);
        startAutoScroll();
//        }
//        else {
////            isOne = true;
//            super.setAdapter(adapter);
//        }
    }

    /**
     * 设置自动滚动的周期时间
     * 需要在设置adapter之前使用才有效
     *
     * @param time 间隔时间(默认为3000ms）
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * 开始自动滚动
     */
    public void startAutoScroll() {
//        if (isOne) {
//            return;
//        }
        if (baseAdapter.getCount() < 2) {
            return;
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    //这是将要通过定时器运行在子线程的任务主体
                    startNextPage();
                }
            };
            if (timer == null) {
                timer = new Timer();
            }
            timer.schedule(timerTask, time, time);
        }
    }

    /**
     * 停止自动滚动
     */
    public void stopAutoScroll() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 滑动到下一页
     */
    private void startNextPage() {
        if (baseAdapter.getCount() > 1) {
            post(new Runnable() {
                @Override
                public void run() {
                    setCurrentItem(getCurrentItem() + 1);
                }
            });
        } else {
            stopAutoScroll();
        }
    }

    private void init() {
        //用反射修改ViewPager切换时的默认滚动时间
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(this, new Scroller(getContext()) {
                @Override
                public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                    super.startScroll(startX, startY, dx, dy, DURATION);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加防止自动滚动与手动控制的冲突
     *
     * @param ev 滑动事件
     * @return 是否拦截事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoScroll();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startAutoScroll();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

//    //所以进行了重写的更改
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                stopAutoScroll();
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                startAutoTask();
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getAdapter() != null) {
            startAutoScroll();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoScroll();
    }
}
