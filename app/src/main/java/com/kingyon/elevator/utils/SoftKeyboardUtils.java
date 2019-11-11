package com.kingyon.elevator.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * Created by GongLi on 2018/7/6.
 * Email：lc824767150@163.com
 */

public class SoftKeyboardUtils {
    private View decorView;
    private View contentView;
    private Context mContext;

    private OnKeyboardChangeListener onKeyboardChangeListener;

    public void setOnKeyboardChangeListener(OnKeyboardChangeListener onKeyboardChangeListener) {
        this.onKeyboardChangeListener = onKeyboardChangeListener;
    }

    public SoftKeyboardUtils(Activity act, View contentView) {
        this.decorView = act.getWindow().getDecorView();
        this.contentView = contentView;
        mContext = act;
        //only required on newer android versions. it was working on API level 19
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public void enable() {
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public void disable() {
        decorView.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
    }


    private boolean isVisiableForLast;
    private int keyboardHeight;

    //a small helper to allow showing the editText focus
    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            //计算出可见屏幕的高度
            int displayHight = rect.bottom - rect.top;
            //获得屏幕整体的高度
            int hight = decorView.getHeight();
            boolean visible = (double) displayHight / hight < 0.8;
            int statusBarHeight = 0;
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = mContext.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (visible != isVisiableForLast) {
                if (visible) {
                    //获得键盘高度
                    keyboardHeight = hight - displayHight - statusBarHeight;
                    contentView.setPadding(0, 0, 0, keyboardHeight);
                    if (onKeyboardChangeListener != null) {
                        onKeyboardChangeListener.onKeyboardChange(true);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        //reset the padding of the contentView
                        contentView.setPadding(0, 0, 0, 0);
                        if (onKeyboardChangeListener != null) {
                            onKeyboardChangeListener.onKeyboardChange(false);
                        }
                    }
                }
            }
            isVisiableForLast = visible;

//blog.csdn.net/s297165331/article/details/55049904?utm_source=copy


//            Rect r = new Rect();
//            //r will be populated with the coordinates of your view that area still visible.
//            decorView.getWindowVisibleDisplayFrame(r);
//
//            //get screen height and calculate the difference with the useable area from the r
//            int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
//            int diff = height - r.bottom;
//
//            //if it could be a keyboard add the padding to the view
//            if (diff != 0) {
//                // if the use-able screen height differs from the total screen height we assume that it shows a keyboard now
//                //check if the padding is 0 (if yes set the padding for the keyboard)
//                if (contentView.getPaddingBottom() != diff) {
//                    //set the padding of the contentView for the keyboard
//                    contentView.setPadding(0, 0, 0, diff);
//                    if (onKeyboardChangeListener != null) {
//                        onKeyboardChangeListener.onKeyboardChange(true);
//                    }
//                }
//            } else {
//                //check if the padding is != 0 (if yes reset the padding)
//                if (contentView.getPaddingBottom() != 0) {
//                    //reset the padding of the contentView
//                    contentView.setPadding(0, 0, 0, 0);
//                    if (onKeyboardChangeListener != null) {
//                        onKeyboardChangeListener.onKeyboardChange(false);
//                    }
//                }
//            }
        }
    };

    public interface OnKeyboardChangeListener {
        void onKeyboardChange(boolean show);
    }

    /**
     * Helper to hide the keyboard
     *
     * @param act
     */
    public static void hideKeyboard(Activity act) {
        if (act != null && act.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
