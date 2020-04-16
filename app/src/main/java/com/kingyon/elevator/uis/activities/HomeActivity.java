package com.kingyon.elevator.uis.activities;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.LatLonCache;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.PushMessageEntity;
import com.kingyon.elevator.entities.VersionEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.homepage.SearchActivity;
import com.kingyon.elevator.uis.activities.user.MessageCenterActivity;
import com.kingyon.elevator.uis.fragments.main.HomepageFragment;
import com.kingyon.elevator.uis.fragments.main.MessageFragment;
import com.kingyon.elevator.uis.fragments.main.PlanNewFragment;
import com.kingyon.elevator.uis.fragments.main.UserFragment;
import com.kingyon.elevator.uis.fragments.main2.FoundFragment;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.LocationUtils;
import com.kingyon.elevator.utils.OCRUtil;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.download.DownloadApkUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created By Admin  on 2020/3/30
 * Email : 163235610@qq.com
 * Author:Myczh
 * Instructions:
 */
public class HomeActivity extends BaseActivity implements AMapLocationListener {

    @BindView(R.id.frame_content)
    FrameLayout frameContent;
    @BindView(R.id.img_home_found)
    ImageView imgHomeFound;
    @BindView(R.id.tv_home_found)
    TextView tvHomeFound;
    @BindView(R.id.ll_home_found)
    LinearLayout llHomeFound;
    @BindView(R.id.img_home_square)
    ImageView imgHomeSquare;
    @BindView(R.id.tv_home_square)
    TextView tvHomeSquare;
    @BindView(R.id.ll_home_square)
    LinearLayout llHomeSquare;
    @BindView(R.id.img_home_fb)
    ImageView imgHomeFb;
    @BindView(R.id.ll_home_fb)
    LinearLayout llHomeFb;
    @BindView(R.id.img_home_message)
    ImageView imgHomeMessage;
    @BindView(R.id.tv_xxs)
    TextView tvXxs;
    @BindView(R.id.tv_home_message)
    TextView tvHomeMessage;
    @BindView(R.id.ll_home_message)
    LinearLayout llHomeMessage;
    @BindView(R.id.img_home_my)
    ImageView imgHomeMy;
    @BindView(R.id.tv_home_my)
    TextView tvHomeMy;
    @BindView(R.id.ll_home_my)
    LinearLayout llHomeMy;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    private LocationEntity locationEntity;
    @Override
    public int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this, false);
//        EventBus.getDefault().register(this);
        checkLocation();
        initPushId();
        dealOpenActivity(getIntent().getParcelableExtra("pushEntity"));
        checkVersion();
        // 请选择您的初始化方式
        OCRUtil.getInstance().initAccessToken(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        viewInit();
        found();
    }

    @OnClick({R.id.ll_home_found, R.id.ll_home_square, R.id.ll_home_fb, R.id.ll_home_message, R.id.ll_home_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_home_found:
                viewInit();
                found();
                break;
            case R.id.ll_home_square:
                viewInit();
                square();
                break;
            case R.id.ll_home_fb:
                Bundle searchBundle1 = getSearchBundle();
                searchBundle1.putBoolean(CommonUtil.KEY_VALUE_3, false);
                startActivity(SearchActivity.class, searchBundle1);
                break;
            case R.id.ll_home_message:
                viewInit();
                message();
                break;
            case R.id.ll_home_my:
                viewInit();
                homeMy();
                break;
        }
    }

    private void homeMy() {
        UserFragment userFragment = new UserFragment();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.frame_content, userFragment );
        fragmentTransaction.commit();
        tvHomeMy.setTextColor( Color.parseColor( "#000000" ) );
        imgHomeMy.setImageResource( R.drawable.ic_nav_private_on );
        tvHomeFound.setSelected( false );
        imgHomeFound.setSelected( false );
        tvHomeSquare.setSelected( false );
        imgHomeSquare.setSelected( false );
        tvHomeMessage.setSelected( false );
        imgHomeMessage.setSelected( false );
        tvHomeMy.setSelected( true );
        imgHomeFb.setSelected( true );
    }

    private void message() {
        MessageFragment messageFragment = new MessageFragment();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.frame_content, messageFragment );
        fragmentTransaction.commit();
        tvHomeMessage.setTextColor( Color.parseColor( "#000000" ) );
        imgHomeMessage.setImageResource( R.drawable.ic_nav_message_on );
        tvHomeFound.setSelected( false );
        imgHomeFound.setSelected( false );
        tvHomeSquare.setSelected( false );
        imgHomeSquare.setSelected( false );
        tvHomeMessage.setSelected( true );
        imgHomeMessage.setSelected( true );
        tvHomeMy.setSelected( false );
        imgHomeFb.setSelected( false );
    }

    private void square() {
        PlanNewFragment planNewFragment = new PlanNewFragment();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.frame_content, planNewFragment );
        fragmentTransaction.commit();
        tvHomeSquare.setTextColor( Color.parseColor( "#000000" ) );
        imgHomeSquare.setImageResource( R.drawable.ic_nav_square_on );
        tvHomeFound.setSelected( false );
        imgHomeFound.setSelected( false );
        tvHomeSquare.setSelected( true );
        imgHomeSquare.setSelected( true );
        tvHomeMessage.setSelected( false );
        imgHomeMessage.setSelected( false );
        tvHomeMy.setSelected( false );
        imgHomeFb.setSelected( false );


    }
    private void found() {
        FoundFragment homepageFragment = new FoundFragment();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.frame_content, homepageFragment );
        fragmentTransaction.commit();
        tvHomeFound.setTextColor( Color.parseColor( "#000000" ) );
        imgHomeFound.setImageResource( R.drawable.ic_nav_find_on );
        tvHomeFound.setSelected( true );
        imgHomeFound.setSelected( true );
        tvHomeSquare.setSelected( false );
        imgHomeSquare.setSelected( false );
        tvHomeMessage.setSelected( false );
        imgHomeMessage.setSelected( false );
        tvHomeMy.setSelected( false );
        imgHomeFb.setSelected( false );
    }
    private void viewInit() {
        tvHomeFound.setTextColor( Color.parseColor( "#666666" ) );
        tvHomeMessage.setTextColor( Color.parseColor( "#666666" ) );
        tvHomeMy.setTextColor( Color.parseColor( "#666666" ) );
        tvHomeSquare.setTextColor( Color.parseColor( "#666666" ) );
        imgHomeFound.setImageResource( R.drawable.ic_nav_find_off);
        imgHomeSquare.setImageResource( R.drawable.ic_nav_square_off );//ic_plan_nor
        imgHomeMessage.setImageResource( R.drawable.ic_nav_message_off );//ic_order_nor
        imgHomeMy.setImageResource( R.drawable.ic_nav_private_off );

    }


    public void checkLocation() {
        String[] locationPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        LocationUtils.getInstance(this).register(this, this);
        checkPermission(new CheckPermListener() {
            @Override
            public void agreeAllPermission() {
//                tabBar.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        LocationUtils.getInstance(HomeActivity.this).startLocation();
//                    }
//                });
            }
        }, "需要允许定位相关权限用于获取地区资讯", locationPermission);
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
    private void dealOpenActivity(PushMessageEntity messageEntity) {
        if (messageEntity == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonUtil.KEY_VALUE_1, messageEntity);
        startActivity(MessageCenterActivity.class, bundle);
    }

    private void checkVersion() {
        NetService.getInstance().getLatestVersion(this)
                .compose(HomeActivity.this.<VersionEntity>bindLifeCycle())
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
                        DownloadApkUtil.getInstance(HomeActivity.this)
                                .isDownloadNewVersion(HomeActivity.this, versionEntity, false);
                    }
                });
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
                LocationUtils.getInstance(HomeActivity.this).stopLocation();
                LocationUtils.getInstance(HomeActivity.this).unregister(HomeActivity.this);
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

    @NonNull
    private Bundle getSearchBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonUtil.KEY_VALUE_1, "");
        if (locationEntity == null) {
            if (TextUtils.isEmpty(DataSharedPreferences.getLocationStr())) {
                locationEntity = AppContent.getInstance().getLocation();
            } else {
                locationEntity = DataSharedPreferences.getLocationCache();
            }
        }
        LocationEntity city = locationEntity != null ? locationEntity : DataSharedPreferences.getLocationCache();
        bundle.putParcelable(CommonUtil.KEY_VALUE_2, new AMapCityEntity(city.getCity(), TextUtils.isEmpty(city.getCityCode()) ? "" : city.getCityCode()));
        return bundle;
    }
}
