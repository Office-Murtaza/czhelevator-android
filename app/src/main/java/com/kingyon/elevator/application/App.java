package com.kingyon.elevator.application;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.utils.DensityUtil;
import com.liulishuo.filedownloader.FileDownloader;
import com.mob.MobSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
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

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white,R.color.black);//全局设置主题颜色
                return new ClassicsHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
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
        DensityUtil.init(this);
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