package com.kingyon.elevator.mvpbase;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;


/**
 * Created By SongPeng  on 2019/11/18
 * Email : 1531603384@qq.com
 * 基础mvp框架中的basepresenter
 */
public class BasePresenter<V extends BaseView> implements IPresenter {
   public Context mContext;

    public BasePresenter(Context mContext){
        this.mContext = mContext;
    }

    protected V mView;

    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param view view
     */
    public void attachView(V view) {
        this.mView = view;
    }

    /**
     * 解除绑定view，一般在onDestroy中调用
     */

    public void detachView() {
        this.mView = null;
    }

    /**
     * View是否绑定
     *
     * @return
     */
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {

    }


    public V getView() {
        return mView;
    }
}
