package com.kingyon.elevator.uis.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.AdvertisionEntity;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.JumpUtils;
import com.kingyon.elevator.utils.PathUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.nets.callbacks.AbsAPICallback;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.activities.BaseHtmlActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by GongLi on 2018/4/13.
 * Email：lc824767150@163.com
 */

public class LoadingActivity extends BaseActivity {

    @BindView(R.id.img_advertision)
    ImageView imgAdvertision;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.tv_ad)
    TextView tvAd;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.root_container)
    RelativeLayout root_container;

    private boolean isFinishedByUser = false;
    private Subscription subscribe;
    private long countDownTime;

    @Override
    public int getContentViewId() {
        StatusBarUtil.setTransparent(this, false);
        return R.layout.activity_loading;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvRight.setText(String.format("Copyright @ %s,%s", TimeUtil.getYear(System.currentTimeMillis()), getString(R.string.app_name)));
        AppContent.getInstance().init(this);
        NetService.getInstance().getAdertising()
                .compose(this.<AdvertisionEntity>bindLifeCycle())
                .subscribe(new AbsAPICallback<AdvertisionEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        Logger.i(ex.getDisplayMessage());
                        onAdvertisionSuccess(DataSharedPreferences.getAdvertision());
                    }

                    @Override
                    public void onNext(AdvertisionEntity advertisionEntity) {
                        DataSharedPreferences.saveAdvertision(advertisionEntity);
                        onAdvertisionSuccess(advertisionEntity);
                    }
                });
        startCountDown();
    }

    @Override
    protected void statusbarLightMode() {

    }

    private void onAdvertisionSuccess(AdvertisionEntity advertisionEntity) {
        if (advertisionEntity != null && advertisionEntity.isUseable()) {
            File advertisionDownloadFile = PathUtils.getAdvertisionDownloadFile(advertisionEntity);
            if (advertisionDownloadFile != null && advertisionDownloadFile.exists()) {
                GlideUtils.loadDrawable(LoadingActivity.this, advertisionDownloadFile, new GlideUtils.BitmapReadyCallBack() {
                    @Override
                    public void onExceptoin(Exception e) {
                        root_container.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onBitmapReady(Drawable drawable, int width, int height) {
                        Logger.i("from download");
                        showAdertision(drawable);
                        root_container.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                GlideUtils.loadDrawable(LoadingActivity.this, advertisionEntity.getPicture(), new GlideUtils.BitmapReadyCallBack() {
                    @Override
                    public void onExceptoin(Exception e) {
                        root_container.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onBitmapReady(Drawable drawable, int width, int height) {
                        Logger.i("from Glide");
                        showAdertision(drawable);
                        root_container.setVisibility(View.VISIBLE);
                    }
                });
                downloadAdertision(advertisionEntity);
            }
            startCountDown();
        }
    }

    private void downloadAdertision(final AdvertisionEntity entity) {
        File advertisionDownloadFile = PathUtils.getAdvertisionDownloadFile(entity);
        if (advertisionDownloadFile == null) {
            return;
        }
        FileDownloader.getImpl().create(entity.getPicture())
                .setPath(advertisionDownloadFile.getAbsolutePath())
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Logger.i("from pending");
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Logger.i("from progress");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Logger.i("from completed");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Logger.i("from paused");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Logger.i("from error");
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Logger.i("from warn");
                    }
                })
                .start();
    }

    private void showAdertision(Drawable drawable) {
        imgAdvertision.setVisibility(View.VISIBLE);
        imgAdvertision.setImageDrawable(drawable);
        tvAd.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.VISIBLE);
        startCountDown();
    }

    private void startCountDown() {
        closeCountDown();
        if (!isFinishedByUser) {
            countDownTime = 5000L;
            subscribe = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                    .compose(this.<Long>bindLifeCycle())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            tvSkip.setText(String.format("%s 跳过", countDownTime / 1000));
                            if (countDownTime <= 0) {
                                closeCountDown();
                                startActivity();
                            }
                            countDownTime -= 1000;
                        }
                    });

//            Observable.just("").delay(2000, TimeUnit.MILLISECONDS)
//                    .compose(this.<String>bindLifeCycle())
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new CustomApiCallback<String>() {
//                        @Override
//                        protected void onResultError(ApiException ex) {
//
//                        }
//
//                        @Override
//                        public void onNext(String s) {
//                            startActivity();
//                        }
//                    });
        }
    }

    private void closeCountDown() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        subscribe = null;
    }

    private void startActivity() {
        if (!isFinishedByUser) {
//            if (DataSharedPreferences.getBoolean("first_lauch_app", true)) {
//                startActivity(TransitionActivity.class);
//            } else {
//            String myUserRole = AppContent.getInstance().getMyUserRole();
//            if (TextUtils.isEmpty(myUserRole) || TextUtils.isEmpty(DataSharedPreferences.getToken())) {
//                startActivity(LoginActivity.class);
//            } else {
            JumpUtils.getInstance().jumpToRoleMain(this, AppContent.getInstance().getMyUserRole());
//            }
//            }
            finish();
        }
    }

    @OnClick({R.id.img_advertision, R.id.tv_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_advertision:
                startAdvertision();
                break;
            case R.id.tv_skip:
                startActivity();
                break;
        }
    }

    private void startAdvertision() {
        AdvertisionEntity advertision = DataSharedPreferences.getAdvertision();
        if (advertision != null && !TextUtils.isEmpty(advertision.getLink()) && !isFinishedByUser) {
            isFinishedByUser = true;
            Bundle bundle = new Bundle();
            bundle.putString(BaseHtmlActivity.TITLE, "    ");
            bundle.putString(BaseHtmlActivity.URL, advertision.getLink());
            startActivity(AdvertisionActivity.class, bundle);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        isFinishedByUser = true;
        closeCountDown();
        super.onDestroy();
    }
}
