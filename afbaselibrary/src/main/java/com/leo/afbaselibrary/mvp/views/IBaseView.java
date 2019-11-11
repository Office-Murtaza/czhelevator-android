package com.leo.afbaselibrary.mvp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * created by arvin on 16/10/24 14:48
 * email：1035407623@qq.com
 * 所有View模块的父类
 */
public interface IBaseView extends View.OnClickListener{
    int getContentViewId();

    void init(Bundle savedInstanceState);

    <T extends View> T getView(int id);

    void showToast(String message);

    void startActivity(Class clazz);

    void startActivity(Class clazz, Bundle bundle);

    void startActivityForResult(Class clazz, int requestCode);

    void startActivityForResult(Class clazz, int requestCode, Bundle bundle);

}
