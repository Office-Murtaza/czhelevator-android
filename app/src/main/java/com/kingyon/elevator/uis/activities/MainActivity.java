package com.kingyon.elevator.uis.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.AdNoticeWindowEntity;
import com.kingyon.elevator.entities.LatLonCache;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.OrderFailedNumberEntity;
import com.kingyon.elevator.entities.PushMessageEntity;
import com.kingyon.elevator.entities.ReceivedPushEntity;
import com.kingyon.elevator.entities.RegisterIdEntity;
import com.kingyon.elevator.entities.TabEntity;
import com.kingyon.elevator.entities.ToPlanTab;
import com.kingyon.elevator.entities.UnreadNumberEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.entities.VersionEntity;
import com.kingyon.elevator.entities.entities.FingerprintEntiy;
import com.kingyon.elevator.interfaces.PrivacyTipsListener;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.user.MessageCenterActivity;
import com.kingyon.elevator.uis.fragments.main.HomepageFragment;
import com.kingyon.elevator.uis.fragments.main.OrderFragment;
import com.kingyon.elevator.uis.fragments.main2.FoundFragment;
import com.kingyon.elevator.uis.fragments.main2.MessageFragmentg;
import com.kingyon.elevator.uis.fragments.main2.PersonalFragment;
import com.kingyon.elevator.uis.fragments.main2.PutcastAdvertisFragment;
import com.kingyon.elevator.uis.fragments.main2.SquareFragmnet;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.LocationUtils;
import com.kingyon.elevator.utils.OCRUtil;
import com.kingyon.elevator.utils.PublicFuncation;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.kingyon.elevator.utils.animationutils.AnimatorPath;
import com.kingyon.elevator.utils.animationutils.PathEvaluator;
import com.kingyon.elevator.utils.animationutils.PathPoint;
import com.kingyon.elevator.utils.utilstwo.AdUtils;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.leo.afbaselibrary.nets.entities.DataEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.download.DownloadApkUtil;
import com.leo.afbaselibrary.widgets.TabStripView;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

import static com.czh.myversiontwo.utils.Constance.MAIN_ACTIVITY;
import static com.kingyon.elevator.utils.utilstwo.ConentUtils.totalNum;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

@Route(path = MAIN_ACTIVITY)
public class MainActivity extends BaseActivity implements TabStripView.OnTabSelectedListener, AMapLocationListener {
    @BindView(R.id.tabBar)
    TabStripView tabBar;
    @BindView(R.id.iv_add_plan_animation_view)
    ImageView iv_add_plan_animation_view;
    @Autowired
    int intdex1;
    private String currentTag;
    private BaseFragment currentFragment;
    private long logTime;
    private AnimatorPath path;//声明动画集合
    private Boolean isFirstInit = true;
    public static MainActivity mainActivity;
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            tabBar.setTabUnread(3, totalNum);
            handler.postDelayed(this, 500);
        }
    };
    Boolean isShow = DataSharedPreferences.getBoolean(DataSharedPreferences.IS_SHOW_ALREADY_PRIVACY_DIALOG, false);

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mainActivity = this;
        ARouter.getInstance().inject(this);
        StatusBarUtil.setTransparent(this, false);
        EventBus.getDefault().register(this);
        currentTag = "首页";
        checkLocation();
        initTab(savedInstanceState);
        initPushId();
        dealOpenActivity(getIntent().getParcelableExtra("pushEntity"));
        if (isShow) {
            checkVersion();
        }
        // 请选择您的初始化方式
        OCRUtil.getInstance().initAccessToken(this);
        tabBar.postDelayed(() -> loadUserPrivacy(), 400);
        ConentUtils.httpHomeData(1);
        handler.postDelayed(runnable, 500);
        httpPersonal();
        tabBar.setDefaultSelectedTab(intdex1);
    }


    private void initTab(Bundle savedInstanceState) {
        //设置tab栏
        tabBar.addTab(FoundFragment.class, new TabStripView.TabParam(R.color.colorPrimary
                , R.drawable.ic_nav_find_off, R.drawable.ic_nav_find_on, "发现"));
        tabBar.addTab(SquareFragmnet.class, new TabStripView.TabParam(R.color.colorPrimary
                , R.drawable.ic_nav_square_off, R.drawable.ic_nav_square_on, "广场"));

        tabBar.addTab(PutcastAdvertisFragment.class, new TabStripView.TabParam(R.color.colorPrimary
                , R.drawable.ic_nav_release_on, R.drawable.ic_nav_release_on, ""));

        tabBar.addTab(MessageFragmentg.class, new TabStripView.TabParam(R.color.colorPrimary
                , R.drawable.ic_nav_message_off, R.drawable.ic_nav_message_on, "消息"));

        tabBar.addTab(PersonalFragment.class, new TabStripView.TabParam(R.color.colorPrimary
                , R.drawable.ic_nav_private_off, R.drawable.ic_nav_private_on, "个人"));
        tabBar.onRestoreInstanceState(savedInstanceState);
        tabBar.setTabSelectListener(this);
        tabBar.post(() -> {
            tabBar.createFragmentByTag(new MessageFragmentg(), "消息");
            tabBar.createFragmentByTag(new SquareFragmnet(), "计划");
        });

    }


    private void httpPersonal() {
        NetService.getInstance().userProfile()
                .compose(this.<UserEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<UserEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e("未登录");
                    }

                    @Override
                    public void onNext(UserEntity userEntity) {
                        if (userEntity != null) {
                            DataSharedPreferences.saveUserBean(userEntity);
                        }
                    }
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
        MobclickAgent.onResume(this);
        if (currentFragment == null) {
            currentFragment = tabBar.getCurrentFragment(currentTag);
        }
        if (currentFragment != null) {
            currentFragment.setUserVisibleHint(true);
        }
        initPushId();
        initZhiwen();
        httpPersonal();
    }
    private void initZhiwen() {
        /*初始化指纹*/
        List<FingerprintEntiy> listzw = DataSupport.findAll(FingerprintEntiy.class);
        LogUtils.e(listzw.toString());
        if (listzw.size()<=0){
            DataSharedPreferences.saveBoolean(DataSharedPreferences.IS_OPEN_FINGER, false);
        }else {
            for (int i = 0; i < listzw.size(); i++) {
                if (listzw.get(i).getUserId().equals(DataSharedPreferences.getCreatateAccount()) && listzw.get(i).getIsFin().equals("2")) {
                    LogUtils.e("*****************");
                    DataSharedPreferences.saveBoolean(DataSharedPreferences.IS_OPEN_FINGER, true);
                } else if (listzw.get(i).getUserId().equals(DataSharedPreferences.getCreatateAccount()) && listzw.get(i).getIsFin().equals("1")) {
                    DataSharedPreferences.saveBoolean(DataSharedPreferences.IS_OPEN_FINGER, false);
                }
            }
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
//                        BaseFragment curFragment = tabBar.findFragmentByTag(tagByPos);
//                        if (curFragment != null && curFragment instanceof SquareFragmnet) {
//                            ((SquareFragmnet) curFragment).onTypeModify(tabEntity.getPlanType());
//                        }
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
//                    if (curFragment != null && curFragment instanceof SquareFragmnet) {
//                        ((SquareFragmnet) curFragment).onTypeModify((String) eventBusObjectEntity.getData());
//                    }
                }
            }
        } else if (eventBusObjectEntity.getEventCode() == EventBusConstants.ReflashPlanCount) {
            int allCount = RuntimeUtils.infomationPlanCount + RuntimeUtils.diyPlanCount + RuntimeUtils.businessPlanCount;
            if (allCount > 0) {
//                tabBar.showRedDot(1);
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
            LogUtils.e("=====================");
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
//                    tabBar.showRedDot(1);
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
        setAlias();
        if (TextUtils.isEmpty(Net.getInstance().getToken())) {
            return;
        }
        String pushId = DataSharedPreferences.getJPushId();
        LogUtils.e(pushId);
        if (TextUtils.isEmpty(pushId)) {
            pushId = JPushInterface.getRegistrationID(this);
            LogUtils.e(pushId);
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


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                JPushInterface.setAlias(getApplicationContext(), 1, DataSharedPreferences.getCreatateAccount());
            }
        }
    };
    private JPushMessageReceiver jPushMessageReceiver = new JPushMessageReceiver() {
        @Override
        public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
            super.onAliasOperatorResult(context, jPushMessage);
            if (jPushMessage.getErrorCode() == 6002) {//超时处理
                Message obtain = Message.obtain();
                obtain.what = 100;
                mHandler.sendMessageDelayed(obtain, 1000 * 60);//60秒后重新验证
            } else {
                Log.e("onAliasOperatorResult: ", jPushMessage.getErrorCode() + "");
            }
        }
    };

    private void setAlias() {
        String alias = DataSharedPreferences.getCreatateAccount();//别名
        LogUtils.e(alias);
        if (!alias.isEmpty()) {
            JPushInterface.setAlias(getApplicationContext(), 1, alias);//设置别名或标签
        }

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
                LogUtils.e(aMapLocation.getLongitude(), aMapLocation.getLatitude(), aMapLocation.getCity(),
                        FormatUtils.getInstance().getCityName(aMapLocation.getCity()), aMapLocation.getAoiName());

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
        if (!isShow) {
            NetService.getInstance().richText(Constants.AgreementType.PRIVACY_POLICY.getValue())
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
                                    finish();
                                }

                                @Override
                                public void onAgree() {

                                }
                            });
                        }
                    });
        }
    }




    public void showMessageUnreadCount(int count) {
        tabBar.setTabUnread(2, count);
    }



}