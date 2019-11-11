package com.kingyon.elevator.application;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.nets.Net;
import com.liulishuo.filedownloader.FileDownloader;
import com.mob.MobSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.litepal.LitePalApplication;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by GongLi on 2018/8/23.
 * Email：lc824767150@163.com
 */

public class App extends LitePalApplication {
    private static App sInstance;
    private static RefWatcher refWatcher;

    public static RefWatcher getRefWatcher() {
        return App.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        init();
        initLogger();
        initUMeng();
        initJPush();
        initLeakCanary();
        initDownLoader();
        initShareSdk();
    }

    private void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return isDebug();
            }
        });
    }

    public boolean isDebug() {
        return getApplicationInfo() != null && (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private void init() {
        Net.getInstance().setToken(DataSharedPreferences.getToken());
    }

    private void initDownLoader() {
        FileDownloader.setupOnApplicationOnCreate(this);
    }

    private void initUMeng() {
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.openActivityDurationTrack(false);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(isDebug());
        UMConfigure.setEncryptEnabled(true);
    }

    private void initShareSdk() {
        MobSDK.init(this);
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        DataSharedPreferences.setPushRegisterId(JPushInterface.getRegistrationID(this));
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);
    }

    /**
     * 获取自定义根目录
     */
    public static App getInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static Context getContext() {
        return sInstance.getApplicationContext();
    }

    private File getExternalFilesDirEx(Context context, String type) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File ef = context.getExternalFilesDir(type);
            if (ef != null && ef.isDirectory()) {
                return ef;
            }
        }
        return new File(Environment.getExternalStorageDirectory(), type);
    }
}
