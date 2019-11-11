package com.leo.afbaselibrary.listeners;

import android.view.View;

/**
 * Created by Leo on 2015/8/6.
 */
public abstract class SingleOnclickListener<T> implements View.OnClickListener {

    private T target;

    public SingleOnclickListener(T t){
        this.target = t;
    }

    @Override
    public void onClick(View view) {
        onSingleClick(view, target);
    }

    public abstract void onSingleClick(View view,T t);

}
