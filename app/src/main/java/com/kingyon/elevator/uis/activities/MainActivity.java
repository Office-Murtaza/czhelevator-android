package com.kingyon.elevator.uis.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.EventBusConstants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.AdNoticeWindowEntity;
import com.kingyon.elevator.entities.EventBusObjectEntity;
import com.kingyon.elevator.entities.LatLonCache;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.OrderFailedNumberEntity;
import com.kingyon.elevator.entities.PushMessageEntity;
import com.kingyon.elevator.entities.ReceivedPushEntity;
import com.kingyon.elevator.entities.RegisterIdEntity;
import com.kingyon.elevator.entities.TabEntity;
import com.kingyon.elevator.entities.ToPlanTab;
import com.kingyon.elevator.entities.UnreadNumberEntity;
import com.kingyon.elevator.entities.VersionEntity;
import com.kingyon.elevator.interfaces.PrivacyTipsListener;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.user.MessageCenterActivity;
import com.kingyon.elevator.uis.fragments.main.HomepageFragment;
import com.kingyon.elevator.uis.fragments.main.MessageFragment;
import com.kingyon.elevator.uis.fragments.main.OrderFragment;
import com.kingyon.elevator.uis.fragments.main.PlanNewFragment;
import com.kingyon.elevator.uis.fragments.main.UserFragment;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.LocationUtils;
import com.kingyon.elevator.utils.OCRUtil;
import com.kingyon.elevator.utils.PublicFuncation;
import com.kingyon.elevator.utils.RichTextUtil;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.kingyon.elevator.utils.animationutils.AnimatorPath;
import com.kingyon.elevator.utils.animationutils.PathEvaluator;
import com.kingyon.elevator.utils.animationutils.PathPoint;
import com.leo.afbaselibrary.nets.entities.DataEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.utils.download.DownloadApkUtil;
import com.leo.afbaselibrary.widgets.TabStripView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity implements TabStripView.OnTabSelectedListener, AMapLocationListener {
    @BindView(R.id.tabBar)
    TabStripView tabBar;
    @BindView(R.id.iv_add_plan_animation_view)
    ImageView iv_add_plan_animation_view;

    private String currentTag;
    private BaseFragment currentFragment;
    private long logTime;
    private AnimatorPath path;//声明动画集合
    private Boolean isFirstInit = true;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this, false);
        EventBus.getDefault().register(this);
        currentTag = "首页";
        checkLocation();
        initTab(savedInstanceState);
        initPushId();
        dealOpenActivity(getIntent().getParcelableExtra("pushEntity"));
        checkVersion();
        // 请选择您的初始化方式
        OCRUtil.getInstance().initAccessToken(this);
        tabBar.postDelayed(() -> loadUserPrivacy(), 400);
    }


    private void initTab(Bundle savedInstanceState) {
        //设置tab栏
        tabBar.addTab(HomepageFragment.class, new TabStripView.TabParam(R.color.colorPrimary
                , R.drawable.ic_homepage_nor, R.drawable.ic_homepage_sec, "首页"));
        tabBar.addTab(PlanNewFragment.class, new TabStripView.TabParam(R.color.colorPrimary
                , R.drawable.ic_plan_nor, R.drawable.ic_plan_sec, "计划"));
        tabBar.addTab(MessageFragment.class, new TabStripView.TabParam(R.color.colorPrimary
                , R.drawable.ic_order_nor, R.drawable.ic_order_sec, "消息"));
        tabBar.addTab(UserFragment.class, new TabStripView.TabParam(R.color.colorPrimary
                , R.drawable.ic_mine_nor, R.drawable.ic_mine_sec, "我的"));
        tabBar.onRestoreInstanceState(savedInstanceState);
        tabBar.setTabSelectListener(this);
        tabBar.post(() -> {
            tabBar.createFragmentByTag(MessageFragment.newInstance(), "消息");
            tabBar.createFragmentByTag(PlanNewFragment.newInstance(), "计划");
        });
    }

    private void dealOpenActivity(PushMessageEntity messageEntity) {
        if (messageEntity == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonUtil.KEY_VALUE_1, messageEntity);
        startActivity(MessageCenterActivity.class, bundle);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        dealOpenActivity((PushMessageEntity) intent.getParcelableExtra("pushEntity"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentFragment == null) {
            tabBar.getCurrentFragment(currentTag);
        }
        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        tabBar.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
//        requestUnreadNumber();
//        requestAdPubFailNumber();
        super.onResume();
        if (currentFragment == null) {
            currentFragment = tabBar.getCurrentFragment(currentTag);
        }
        if (currentFragment != null) {
            currentFragment.setUserVisibleHint(true);
        }
        if (PublicFuncation.isIntervalSixMin()) {
            loadWindowAd();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (currentFragment == null) {
            currentFragment = tabBar.getCurrentFragment(currentTag);
        }
    }

    private void checkVersion() {
        NetService.getInstance().getLatestVersion(this)
                .compose(MainActivity.this.<VersionEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<VersionEntity>() {
                    @Override
                    public void onResultError(ApiException ex) {
                        Logger.e(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(final VersionEntity versionEntity) {
                        if (versionEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        DownloadApkUtil.getInstance(MainActivity.this)
                                .isDownloadNewVersion(MainActivity.this, versionEntity, false);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeTab(TabEntity tabEntity) {
        if (tabEntity.isExit()) {
            finish();
        } else {
            if (tabBar != null) {
                tabBar.setCurrentSelectedTab(tabEntity.getPos());
                if (tabEntity.getPos() == 2 && tabEntity.getOrderType() != null) {
                    String tagByPos = tabBar.getTagByPos(2);
                    if (tabBar.isExist(tagByPos)) {
                        BaseFragment curFragment = tabBar.getCurrentFragment(tagByPos);
                        if (curFragment != null && curFragment instanceof OrderFragment) {
                            ((OrderFragment) curFragment).onStatusModify(tabEntity.getOrderType());
                        }
                    }
                }
                if (tabEntity.getPos() == 1 && !TextUtils.isEmpty(tabEntity.getPlanType())) {
                    String tagByPos = tabBar.getTagByPos(1);
                    if (tabBar.isExist(tagByPos)) {
                        BaseFragment curFragment = tabBar.findFragmentByTag(tagByPos);
                        if (curFragment != null && curFragment instanceof PlanNewFragment) {
                            ((PlanNewFragment) curFragment).onTypeModify(tabEntity.getPlanType());
                        }
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeTab(ToPlanTab toPlanTab) {
        ActivityUtil.finishAllNotMain();
        onChangeTab(new TabEntity(1, toPlanTab.getType()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHomeAddSuccess(EventBusObjectEntity eventBusObjectEntity) {
        if (eventBusObjectEntity.getEventCode() == EventBusConstants.AddHomeCellToPlanSuccess) {
            ActivityUtil.finishAllNotMain();
            startAddPlanAnimation();
            if (!TextUtils.isEmpty((String) eventBusObjectEntity.getData())) {
                String tagByPos = tabBar.getTagByPos(1);
                if (tabBar.isExist(tagByPos)) {
                    BaseFragment curFragment = tabBar.findFragmentByTag(tagByPos);
                    if (curFragment != null && curFragment instanceof PlanNewFragment) {
                        ((PlanNewFragment) curFragment).onTypeModify((String) eventBusObjectEntity.getData());
                    }
                }
            }
        } else if (eventBusObjectEntity.getEventCode() == EventBusConstants.ReflashPlanCount) {
            int allCount = RuntimeUtils.infomationPlanCount + RuntimeUtils.diyPlanCount + RuntimeUtils.businessPlanCount;
            if (allCount > 0) {
                tabBar.showRedDot(1);
            } else {
                tabBar.hideRedDot(1);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterId(RegisterIdEntity registerIdEntity) {
        initPushId();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedPush(ReceivedPushEntity entity) {
        //requestUnreadNumber();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        DialogUtils.getInstance().hidePlanSelectDateDialog();
        super.onDestroy();
    }

    @Override
    public void onTabSelected(TabStripView.ViewHolder holder) {
        currentTag = holder.tag;
        currentFragment = tabBar.getCurrentFragment(currentTag);
    }

    @Override
    public void onBackPressed() {
        if (tabBar != null && tabBar.getCurrentSelectedTab() != 0) {
            tabBar.setCurrentSelectedTab(0);
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - logTime < 2000) {
            finish();
        } else {
            showToast("再次点击退出应用");
            logTime = currentTime;
        }
    }

    /**
     * 执行加入购物车的动画
     */
    public void startAddPlanAnimation() {
        if (RuntimeUtils.clickPositionAnimation != null) {
            if (RuntimeUtils.animationImagePath != null) {
                GlideUtils.loadImage(this, RuntimeUtils.animationImagePath, iv_add_plan_animation_view);
            }
            iv_add_plan_animation_view.setX(20);
            iv_add_plan_animation_view.setY(RuntimeUtils.clickPositionAnimation[1] - 150);
            iv_add_plan_animation_view.setVisibility(View.VISIBLE);
            path = new AnimatorPath();
            path.moveTo(DensityUtil.dip2px(16), RuntimeUtils.clickPositionAnimation[1] - 150);
            path.secondBesselCurveTo(ScreenUtils.getScreenWidth() / 2, RuntimeUtils.clickPositionAnimation[1] - 50, ScreenUtils.getScreenWidth() / 4 + 20, ScreenUtils.getScreenHeight() - 80);
            ObjectAnimator anim = ObjectAnimator.ofObject(this, "addPlan", new PathEvaluator(), path.getPoints().toArray());
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(1000);
            anim.start();
            iv_add_plan_animation_view.animate().scaleX(0f).scaleY(0f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    iv_add_plan_animation_view.setVisibility(View.GONE);
                    iv_add_plan_animation_view.setScaleX(1f);
                    iv_add_plan_animation_view.setScaleY(1f);
                    tabBar.showRedDot(1);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
            RuntimeUtils.clickPositionAnimation = null;
        }
    }

    public void setAddPlan(PathPoint newLoc) {
        iv_add_plan_animation_view.setTranslationX(newLoc.mX);
        iv_add_plan_animation_view.setTranslationY(newLoc.mY);
    }

    private void initPushId() {
        if (TextUtils.isEmpty(Net.getInstance().getToken())) {
            return;
        }
        String pushId = DataSharedPreferences.getJPushId();
        if (TextUtils.isEmpty(pushId)) {
            pushId = JPushInterface.getRegistrationID(this);
            DataSharedPreferences.setPushRegisterId(pushId);
        }
        if (TextUtils.isEmpty(pushId)) {
            return;
        }
        NetService.getInstance().bindPushId(DataSharedPreferences.getJPushId())
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        Logger.d("JPush", "[JPush] 绑定 Registration Id 失败");
                    }

                    @Override
                    public void onNext(String s) {
                        Logger.d("JPush", "[JPush] 绑定 Registration Id 成功");
                    }
                });
    }

    public void checkLocation() {
        String[] locationPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        LocationUtils.getInstance(this).register(this, this);
        checkPermission(new CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                tabBar.post(new Runnable() {
                    @Override
                    public void run() {
                        LocationUtils.getInstance(MainActivity.this).startLocation();
                    }
                });
            }
        }, "需要允许定位相关权限用于获取地区资讯", locationPermission);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                LocationEntity entity = new LocationEntity();
                entity.setLongitude(aMapLocation.getLongitude());
                entity.setLatitude(aMapLocation.getLatitude());
                entity.setName(String.format("%s%s", FormatUtils.getInstance().getCityName(aMapLocation.getCity()), aMapLocation.getAoiName()));
                entity.setCity(aMapLocation.getCity());
                boolean firstLocation = AppContent.getInstance().getLocation() == null;
                DataSharedPreferences.saveLatLon(new LatLonCache(entity.getLatitude(), entity.getLongitude()));
                AppContent.getInstance().setLocation(entity);
                LocationUtils.getInstance(MainActivity.this).stopLocation();
                LocationUtils.getInstance(MainActivity.this).unregister(MainActivity.this);
                updateHomepageLocation(firstLocation);
                EventBus.getDefault().post(entity);
//                if (App.getInstance().isDebug()) {
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("longitude：").append(aMapLocation.getLongitude()).append("\n");
//                    stringBuilder.append("latitude：").append(aMapLocation.getLatitude()).append("\n");
//                    stringBuilder.append("address：").append(aMapLocation.getAddress()).append("\n");
//                    stringBuilder.append("city：").append(aMapLocation.getCity()).append("\n");
//                    stringBuilder.append("district：").append(aMapLocation.getDistrict()).append("\n");
//                    stringBuilder.append("street：").append(aMapLocation.getStreet()).append("\n");
//                    stringBuilder.append("cityCode：").append(aMapLocation.getCityCode()).append("\n");
//                    stringBuilder.append("aoiName：").append(aMapLocation.getAoiName());
//                    stringBuilder.append("poiName：").append(aMapLocation.getPoiName());
//                    Logger.i(String.format("定位信息：\n%s", stringBuilder));
//                }
            } else {
                Logger.e(String.format("AmapError：\n\tlocation Error\n\t\tErrCode:%s\n\t\terrInfo:%s", aMapLocation.getErrorCode(), aMapLocation.getErrorInfo()));
            }
        } else {
            Logger.e("AmapError：\n\tlocation Error\n\t\tErrCode:null\n\t\terrInfo:null");
        }
    }

    private void updateHomepageLocation(boolean firstLocation) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof HomepageFragment) {
                    ((HomepageFragment) fragment).onLocationResult(firstLocation);
                }
            }
        }
    }

    /**
     * 获取系统消息的未读个数
     */
    private void requestUnreadNumber() {
        if (TextUtils.isEmpty(Net.getInstance().getToken())) {
            EventBus.getDefault().post(new UnreadNumberEntity());
            tabBar.setTabUnread(3, 0);
        } else {
            NetService.getInstance().unreadCount()
                    .compose(this.<UnreadNumberEntity>bindLifeCycle())
                    .subscribe(new CustomApiCallback<UnreadNumberEntity>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                            EventBus.getDefault().post(new UnreadNumberEntity());
                            tabBar.setTabUnread(3, 0);
                        }

                        @Override
                        public void onNext(UnreadNumberEntity unreadNumberEntity) {
                            tabBar.setTabUnread(3, unreadNumberEntity.getTotalUnread());
                            EventBus.getDefault().post(unreadNumberEntity);
                        }
                    });
        }
    }

    private void requestAdPubFailNumber() {
        if (TextUtils.isEmpty(Net.getInstance().getToken())) {
            tabBar.setTabUnread(2, 0);
        } else {
            NetService.getInstance().orderPublishFailed()
                    .compose(this.<OrderFailedNumberEntity>bindLifeCycle())
                    .subscribe(new CustomApiCallback<OrderFailedNumberEntity>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                            tabBar.setPublishFailed(2, 0);
                        }

                        @Override
                        public void onNext(OrderFailedNumberEntity orderFailedNumberEntity) {
                            tabBar.setPublishFailed(2, orderFailedNumberEntity.getNumber());
                        }
                    });
        }
    }


    /**
     * 首页弹出用户协议框，YYB审核需要做首次启动弹出处理
     */
    private void loadUserPrivacy() {
        Boolean isShow = DataSharedPreferences.getBoolean(DataSharedPreferences.IS_SHOW_ALREADY_PRIVACY_DIALOG, false);
        if (!isShow) {
            NetService.getInstance().richText(Constants.AgreementType.USER_RULE.getValue())
                    .compose(this.<DataEntity<String>>bindLifeCycle())
                    .subscribe(new CustomApiCallback<DataEntity<String>>() {
                        @Override
                        protected void onResultError(ApiException ex) {

                        }

                        @Override
                        public void onNext(DataEntity<String> dataEntity) {
                            DialogUtils.getInstance().showUserPrivacyTipsDialog(MainActivity.this, dataEntity.getData(), new PrivacyTipsListener() {
                                @Override
                                public void onNoAgree() {

                                }

                                @Override
                                public void onAgree() {

                                }
                            });
                        }
                    });
        }
    }


    /**
     * 加载弹窗通知
     */
    private void loadWindowAd() {
        NetService.getInstance().getTipsList("HOME", 2)
                .subscribe(new CustomApiCallback<List<AdNoticeWindowEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.d("弹窗广告加载失败：" + GsonUtils.toJson(ex));
                    }

                    @Override
                    public void onNext(List<AdNoticeWindowEntity> adNoticeWindowEntities) {
                        if (adNoticeWindowEntities != null && adNoticeWindowEntities.size() > 0) {
                            LogUtils.d("弹窗广告数据：" + GsonUtils.toJson(adNoticeWindowEntities));
                            AdNoticeWindowEntity adNoticeWindowEntity = PublicFuncation.getLastAdItem(adNoticeWindowEntities);
                            if (adNoticeWindowEntity != null) {
                                if (adNoticeWindowEntity.getShowType() == 2) {
                                    //展示弹窗广告
                                    DialogUtils.getInstance().showMainWindowNoticeDialog(MainActivity.this, adNoticeWindowEntity);
                                }
                            }
                        }
                    }
                });
    }


    public void showMessageUnreadCount(int count) {
        tabBar.setTabUnread(2, count);
    }
}