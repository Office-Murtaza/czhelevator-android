package com.zhaoss.weixinrecorded;

import android.app.Application;
import android.content.Context;

/**
 * @ClassName: App
 * @Description: java类作用描述
 * @Author: Mr Chen
 * @CreateDate: 2020/3/22 22:44
 */
public class App extends Application {
    public static Context sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = getApplicationContext();
    }
}
