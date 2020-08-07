package com.kingyon.elevator.uis.activities.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.GlideCacheUtil;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ACCOUNT_BINDING;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_RE_CODE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_ABOUT;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 * 系统设置
 */

public class SettingActivity extends BaseSwipeBackActivity {
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.ll_cache)
    LinearLayout llCache;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.security_setting)
    LinearLayout security_setting;
    @BindView(R.id.pre_v_back)
    ImageView preVBack;
    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.head_root)
    RelativeLayout headRoot;
    @BindView(R.id.tv_account_binding)
    TextView tvAccountBinding;
    @BindView(R.id.default_setting)
    TextView defaultSetting;
    @BindView(R.id.tv_font)
    LinearLayout tvFont;
    @BindView(R.id.tv_about)
    TextView tvAbout;


    @Override
    protected String getTitleText() {
        return "系统设置";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initSize();

    }

    @OnClick({R.id.ll_cache, R.id.tv_logout,
            R.id.security_setting,R.id.tv_account_binding, R.id.tv_font, R.id.tv_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_cache:
                clearCache();
                break;
            case R.id.tv_logout:
                if (TextUtils.isEmpty(Net.getInstance().getToken())) {
//                    startActivity(LoginActivity.class);
                   ActivityUtils.setLoginActivity();
                } else {
                    showExitDialog();
                }
                break;
//            case R.id.ll_user_privacy:
//                AgreementActivity.start(this, "用户隐私政策", Constants.AgreementType.USER_RULE.getValue());
//                break;
            case R.id.security_setting:
                if (DataSharedPreferences.getUserBean() == null) {
                    try {
                        Context currentActivity = ActivityUtil.getCurrentActivity();
                        if (currentActivity != null) {
                            Intent intent = new Intent(currentActivity, Class.forName("com.kingyon.elevator.uis.actiivty2.login.LoginActiivty"));
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("value_1", true);
                            intent.putExtras(bundle);
                            currentActivity.startActivity(intent);//只释放这一行
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    MyActivityUtils.goFragmentContainerActivity(SettingActivity.this, FragmentConstants.SecuritySettingFragment);
                }
                break;
            case R.id.tv_account_binding:
                /*账户绑定*/
                if (isToken(this)) {
                    ActivityUtils.setActivity(ACTIVITY_ACCOUNT_BINDING);
                } else {
                    ActivityUtils.setLoginActivity();
                }

                break;
            case R.id.tv_font:
                /*字体大小*/

                break;
            case R.id.tv_about:
                /*关于*/
                ActivityUtils.setActivity(ACTIVITY_USER_ABOUT);
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
        showProgressDialog(getString(R.string.wait),true);
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
                        DataSharedPreferences.saveBoolean(DataSharedPreferences.IS_OPEN_FINGER, false);
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



    private void clearCache() {
        llCache.setEnabled(false);
        showProgressDialog("正在清除缓存",true);
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
                        showProgressDialog("清除缓存成功",true);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
