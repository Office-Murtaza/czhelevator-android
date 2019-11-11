package com.kingyon.elevator.others;

import android.content.DialogInterface;

/**
 * Created by GongLi on 2018/9/20.
 * Email：lc824767150@163.com
 */

public abstract class OnClickWithObjectListener<T> implements DialogInterface.OnClickListener {

    private T entity;

    private OnClickWithObjectListener() {
        entity = null;
    }

    public OnClickWithObjectListener(T entity) {
        this.entity = entity;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        onClick(dialog, which, entity);
    }

    public abstract void onClick(DialogInterface dialog, int which, T entity);
}
