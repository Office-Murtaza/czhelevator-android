package com.czh.myversiontwo;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;


/**
 * @Created By Admin  on 2020/4/21
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);
    }
}
