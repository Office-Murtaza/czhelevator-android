package com.leo.afbaselibrary.uis.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.leo.afbaselibrary.R;
import com.leo.afbaselibrary.mvp.presenters.BasePresenter;
import com.leo.afbaselibrary.mvp.views.IBaseView;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.leo.afbaselibrary.utils.BarUtils;
import com.leo.afbaselibrary.utils.EasyPermissions;
import com.leo.afbaselibrary.utils.QuickClickUtils;
import com.leo.afbaselibrary.utils.RxCheckLifeCycleTransformer;
import com.leo.afbaselibrary.utils.statusbar.Eyes;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;
import rx.subjects.BehaviorSubject;

/**
 * created by arvin on 16/10/24 14:55
 * email：1035407623@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView, EasyPermissions.PermissionCallbacks{

    private static final String TAG = "BaseActivity";
    private BasePresenter<IBaseView> mPresenter;
    protected BehaviorSubject<RxCheckLifeCycleTransformer.LifeCycleEvent> eventBehaviorSubject = BehaviorSubject.create();
    /** 触摸时按下的点 **/
    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();
    /**startActivity
     * 权限回调接口
     */
    private CheckPermListener mListener;
    protected static final int RC_PERM = 123;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusbarLightMode();
        setContentView(getContentViewId());
        mPresenter = new BasePresenter<IBaseView>(this);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        init(savedInstanceState);
        Log.v(TAG, this.getClass().getSimpleName());
        eventBehaviorSubject.onNext(RxCheckLifeCycleTransformer.LifeCycleEvent.CREATE);
    }

    protected void statusbarLightMode() {
        Eyes.setStatusBarLightMode(this, Color.WHITE);
        BarUtils.setStatusBarLightMode(this, true);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
    }

    public void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public final <T extends View> T getView(int id) {
        return mPresenter.getView(id);
    }

    @Override
    public void showToast(String message) {
        mPresenter.showToast(message);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void startActivity(Class clazz) {
        if (QuickClickUtils.isFastClick()) {
            mPresenter.startActivity(clazz, null);
        }
    }

    @Override
    public void startActivity(Class clazz, Bundle bundle) {
        if (QuickClickUtils.isFastClick()) {
            mPresenter.startActivity(clazz, bundle);
        }
    }

    @Override
    public void startActivityForResult(Class clazz, int requestCode) {
        if (QuickClickUtils.isFastClick()) {
            mPresenter.startActivityForResult(clazz, requestCode, null);
        }
    }

    @Override
    public void startActivityForResult(Class clazz, int requestCode, Bundle bundle) {
        if (QuickClickUtils.isFastClick()) {
            mPresenter.startActivityForResult(clazz, requestCode, bundle);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
//        overridePendingTransition(0, R.anim.af_right_out);
//        overridePendingTransition(R.anim.fade_in, R.anim.af_right_out);
    }

    public void checkPermission(CheckPermListener listener, String resString, String... mPerms) {
        mListener = listener;
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            if (mListener != null)
                mListener.agreeAllPermission();
        } else {
            EasyPermissions.requestPermissions(this, resString,
                    RC_PERM, mPerms);
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
            backFromSetting();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // 只同意了部分权限

    }

    @Override
    public void onPermissionsAllGranted() {
        if (mListener != null)
            mListener.agreeAllPermission();//同意了全部权限的回调
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                "当前应用缺少必要权限。\n请点击\"设置\"-\"权限\"-打开所需权限。",
                R.string.setting, R.string.cancel, null, perms);
    }

    protected void backFromSetting() {
    }

    public void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(BaseActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
        }
        progressDialog.setMessage(message != null ? message : "");
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public abstract int getContentViewId();

    @Override
    public abstract void init(Bundle savedInstanceState);

    public interface CheckPermListener {
        //权限通过后的回调方法
        void agreeAllPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBehaviorSubject.onNext(RxCheckLifeCycleTransformer.LifeCycleEvent.START);
    }


    /**
     * 把Observable的生命周期与Activity绑定
     *
     * @param <D>
     * @return
     */
    public <D> RxCheckLifeCycleTransformer<D> bindLifeCycle() {
        return new RxCheckLifeCycleTransformer<D>(eventBehaviorSubject);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mobResume();
    }

    private void mobResume() {
        MobclickAgent.onResume(this);
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        if (fragments == null || fragments.size() < 1) {
//            MobclickAgent.onPageStart(getClass().getSimpleName());
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mobPause();
    }

    private void mobPause() {
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        if (fragments == null || fragments.size() < 1) {
//            MobclickAgent.onPageEnd(getClass().getSimpleName());
//        }
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.removeActivity(this);
        eventBehaviorSubject.onNext(RxCheckLifeCycleTransformer.LifeCycleEvent.DESTROY);
    }

    /**
     * 重写getResources，防止因为系统字体大小变更引起布局变化
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        try {
            Configuration newConfig = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            if (resources != null && newConfig.fontScale != 1) {
                newConfig.fontScale = 1;
                if (Build.VERSION.SDK_INT >= 17) {
                    Context configurationContext = createConfigurationContext(newConfig);
                    resources = configurationContext.getResources();
                    displayMetrics.scaledDensity = displayMetrics.density * newConfig.fontScale;
                } else {
                    resources.updateConfiguration(newConfig, displayMetrics);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resources;
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        curP.x = event.getX();
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                downP.x = event.getX();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (curP.x- downP.x > 500) {
//                    Log.i("TEST", "move-=-=-=--=-");
//                    finish();
//
//                }
//
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//
//            default:
//                break;
//        }
//
//        return true;
//    }

}
