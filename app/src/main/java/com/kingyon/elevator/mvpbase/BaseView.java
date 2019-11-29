package com.kingyon.elevator.mvpbase;

import android.app.ProgressDialog;

/**
 * Created By SongPeng  on 2019/11/18
 * Email : 1531603384@qq.com
 */
public interface BaseView {

    /**
     * 显示等待框
     */
    void showLoading();

    /**
     * 显示等待框
     *
     * @param tips 提示内容
     */
    void showLoading(String tips);

    /**
     * 显示短时间吐司提示
     *
     * @param tipsContent
     */
    void showShortToast(String tipsContent);

    /**
     * 显示长时间吐司提示
     *
     * @param tipsContent
     */
    void showLongToast(String tipsContent);

    /**
     * 显示内容为空的view
     */
    void showEmptyContentView();

    /**
     * 显示内容为空的view
     */
    void showEmptyContentView(String content);

    void showErrorView();

    void showErrorView(String content);

    void showContentView();


    void showProgressView();

    void showProgressView(String content);


    void showProgressDialog(String message,Boolean  isCancel);

    void hideProgressDailog();

}
