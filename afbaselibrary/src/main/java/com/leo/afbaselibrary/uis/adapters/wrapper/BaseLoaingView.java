package com.leo.afbaselibrary.uis.adapters.wrapper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Leo on 2017/3/14.
 */

public abstract class BaseLoaingView extends View implements BaseLoading{

    private WrapperState wrapperState = WrapperState.LOADING;

    public BaseLoaingView(Context context) {
        super(context);
    }

    public BaseLoaingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLoaingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startLoading(){
        showLoaing();
    }

    public void showFaild(){
        showFailed();
    }

    public void showNoData(){
        showEmpty();
    }
}
