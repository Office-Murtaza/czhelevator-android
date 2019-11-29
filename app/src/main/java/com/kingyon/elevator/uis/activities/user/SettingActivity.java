package com.kingyon.elevator.uis.activities.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.VersionEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.AgreementActivity;
import com.kingyon.elevator.uis.activities.password.LoginActivity;
import com.kingyon.elevator.utils.GlideCacheUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.leo.afbaselibrary.utils.download.DownloadApkUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class SettingActivity extends BaseSwipeBackActivity {
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.ll_cache)
    LinearLayout llCache;
    @BindView(R.id.tv_feed_bak)
    TextView tvFeedBak;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.ll_user_privacy)
    LinearLayout ll_user_privacy;
    @BindView(R.id.security_setting)
    LinearLayout security_setting;




    @Override
    protected String getTitleText() {
        return "设置";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initSize();
        tvVersion.setText(String.format("V%s", AFUtil.getVersion(this)));
        requestUpdate(false);
    }

    @OnClick({R.id.ll_cache, R.id.tv_feed_bak, R.id.ll_version, R.id.tv_logout,R.id.ll_user_privacy,R.id.security_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_cache:
                clearCache();
                break;
            case R.id.tv_feed_bak:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.ll_version:
                requestUpdate(true);
                break;
            case R.id.tv_logout:
                if (TextUtils.isEmpty(Net.getInstance().getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    showExitDialog();
                }
                break;
            case R.id.ll_user_privacy:
                AgreementActivity.start(this, "用户隐私政策", Constants.AgreementType.USER_RULE.getValue());
                break;
            case R.id.security_setting:

                break;
        }
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要退出登录吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void logout() {
        showProgressDialog(getString(R.string.wait));
        tvLogout.setEnabled(false);
        NetService.getInstance().logout()
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        DataSharedPreferences.clearLoginInfo();
                        tvLogout.setEnabled(true);
                        hideProgress();
                        ActivityUtil.finishAllNotMain();
                        MobclickAgent.onProfileSignOff();
//                        startActivity(MainActivity.class);
                    }

                    @Override
                    public void onNext(String s) {
                        DataSharedPreferences.clearLoginInfo();
                        tvLogout.setEnabled(true);
                        hideProgress();
                        ActivityUtil.finishAllNotMain();
                        MobclickAgent.onProfileSignOff();
//                        startActivity(MainActivity.class);
                    }
                });
    }

    @Override
    protected void onResume() {
        tvLogout.setText(TextUtils.isEmpty(Net.getInstance().getToken()) ? "登录" : "退出登录");
        super.onResume();
    }

    private void requestUpdate(final boolean update) {
        if (update) {
            showProgressDialog(getString(R.string.wait));
        }
        NetService.getInstance().getLatestVersion(this)
                .compose(this.<VersionEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<VersionEntity>() {
                    @Override
                    public void onResultError(ApiException ex) {
                        if (update) {
                            showToast(ex.getDisplayMessage());
                        }
                        hideProgress();
                    }

                    @Override
                    public void onNext(VersionEntity versionEntity) {
                        hideProgress();
                        if (update) {
                            if (versionEntity == null) {
                                ToastUtils.toast(SettingActivity.this, "已是最新版本");
                                return;
                            }
                            DownloadApkUtil.getInstance(SettingActivity.this).isDownloadNewVersion(SettingActivity.this, versionEntity);
                        } else {
                            if (versionEntity == null) {
                                return;
                            }
                            if (versionEntity.getVersionCode() > AFUtil.getVersionCode(SettingActivity.this)) {
                                tvVersion.setSelected(true);
                            } else {
                                tvVersion.setSelected(false);
                            }
                        }
                    }
                });
    }

    private void clearCache() {
        llCache.setEnabled(false);
        showProgressDialog("正在清除缓存");
        Glide.get(SettingActivity.this).clearMemory();
        Observable.just("").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                Glide.get(SettingActivity.this).clearDiskCache();
                GlideCacheUtil.getInstance().deleteFolderFile(getCacheDir().getAbsolutePath(), false);
                if (getExternalCacheDir() != null) {
                    GlideCacheUtil.getInstance().deleteFolderFile(getExternalCacheDir().getAbsolutePath(), false);
                }
                return s;
            }
        }).delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        showProgressDialog("清除缓存成功");
                        return s;
                    }
                }).delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                    }

                    @Override
                    public void onNext(String s) {
                        llCache.setEnabled(true);
                        initSize();
                        hideProgress();
                    }
                });
    }

    private void initSize() {
        tvCache.setText(GlideCacheUtil.getFormatSize(getCacheSize()));
    }

    private long getCacheSize() {
        GlideCacheUtil glideCacheUtil = GlideCacheUtil.getInstance();
        return glideCacheUtil.getFolderSize(getCacheDir())
                + glideCacheUtil.getFolderSize(getExternalCacheDir());
    }
}
