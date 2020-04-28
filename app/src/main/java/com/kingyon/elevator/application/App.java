package com.kingyon.elevator.application;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.FileUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.mob.MobSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.litepal.LitePalApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by GongLi on 2018/8/23.
 * Email：lc824767150@163.com
 */

public class App extends LitePalApplication {
    private static App sInstance;
    private static RefWatcher refWatcher;
    public static Context sApplication;
    public static RefWatcher getRefWatcher() {
        return App.refWatcher;
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.white, R.color.black);//全局设置主题颜色
            return new ClassicsHeader(context);
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
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
        ARouter.init(this);
        sApplication = getApplicationContext();
        FileUtils.createFile("PDD");
        okgoinit();
    }

    private void okgoinit() {
        //okGo网络框架初始化和全局配置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));      //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));//使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));      //使用内存保持cookie，app退出后，cookie消失
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.put("Cache-Control", "no-cache");    //header不支持中文，不允许有特殊字符
        headers.put("Postman-Token", "<calculated when request is sent>");
        headers.put("Content-Length", "0");
        headers.put("Host", "<calculated when request is sent>");
        headers.put("User-Agent", "PostmanRuntime/7.24.1");
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Connection", "keep-alive");
        headers.put("token", DataSharedPreferences.getToken());
        //设置请求参数
        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);
    }

    private void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return isDebug();
            }
        });
        if (AppUtils.isAppDebug()) {
            LogUtils.getConfig().setLogSwitch(true);
        } else {
            LogUtils.getConfig().setLogSwitch(false);
        }
    }

    public boolean isDebug() {
        return getApplicationInfo() != null && (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private void init() {
        DensityUtil.init(this);
        LogUtils.d("token:" + DataSharedPreferences.getToken());
        Net.getInstance().setToken(DataSharedPreferences.getToken());

        String fileName = Environment.getExternalStorageDirectory() + File.separator + "PDD";
        new File(fileName).mkdirs();
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
