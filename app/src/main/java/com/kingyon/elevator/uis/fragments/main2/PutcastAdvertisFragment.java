package com.kingyon.elevator.uis.fragments.main2;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.entities.CityFacilityInfoEntiy;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.ConentOdjerEntity;
import com.kingyon.elevator.entities.entities.RecommendHouseEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.others.AddCellToPlanPresenter;
import com.kingyon.elevator.uis.activities.MainActivity;
import com.kingyon.elevator.uis.activities.homepage.CityActivity;
import com.kingyon.elevator.uis.activities.homepage.SearchActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.RecommendHouseAdapter;
import com.kingyon.elevator.uis.fragments.main.PlanNewFragment;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.LocationUtils;
import com.kingyon.elevator.utils.MyToastUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.kingyon.elevator.utils.utilstwo.CityUtils;
import com.kingyon.elevator.utils.utilstwo.LocationEnabledUtils;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.utils.EasyPermissions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ADVERTIS_STYLE;
import static com.kingyon.elevator.utils.utilstwo.LocationEnabledUtils.isLocationEnabled;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * @Created By Admin  on 2020/5/13
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 广告投放
 */
public class PutcastAdvertisFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.ll_select_place)
    LinearLayout llSelectPlace;
    @BindView(R.id.tv_type_name)
    TextView tvTypeName;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.tv_terminal)
    TextView tvTerminal;
    @BindView(R.id.tv_claim)
    TextView tvClaim;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_frequency)
    TextView tvFrequency;
    @BindView(R.id.tv_way)
    TextView tvWay;
    @BindView(R.id.tv_ad_time)
    TextView tvAdTime;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.tv_choose_point)
    TextView tvChoosePoint;
    @BindView(R.id.ll_js)
    LinearLayout llJs;
    @BindView(R.id.ll_tj)
    LinearLayout llTj;
    @BindView(R.id.rv_recommended)
    RecyclerView rvRecommended;
    Unbinder unbinder;
    @BindView(R.id.fl_title)
    LinearLayout flTitle;
    @BindView(R.id.tv_city_number)
    TextView tvCityNumber;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.ll_view_schedule)
    LinearLayout llViewSchedule;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    private AddCellToPlanPresenter addCellToPlanPresenter;
    private LocationEntity locationEntity;

    private List<RecommendHouseEntiy> list = new ArrayList<>();
    RecommendHouseAdapter recommendHouseAdapter;
    private String latitude,longitude;
    private  int cityId;
    private int page = 1;
    private String provinceCode, cityCode,  countyCode;
    String[] positioning = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    @Override
    public int getContentViewId() {
        return R.layout.fragment_putcast_advrttis;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setHeadViewPadding(getActivity(), flTitle);
        addCellToPlanPresenter = new AddCellToPlanPresenter((BaseActivity) getActivity());
        LocationEntity entity = AppContent.getInstance().getLocation();
        if (entity!=null) {
            latitude = String.valueOf(entity.getLatitude());
            longitude = String.valueOf(entity.getLongitude());
            tvPlace.setText(entity.getCity() + "");
            cityCode = String.valueOf(520100);
            cityId = 520100;
        }
        initRefresh();
//        CityUtils.getCityCode(getActivity(), entity.getCity(), new CityUtils.CityCode() {
//            @Override
//            public void cityCode(int cityCode1) {
//
//            }
//        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            initRefresh();
        }
    }
    private void initRefresh() {
        list.clear();
        httpAdcertis(provinceCode, cityCode, countyCode);
        httpRecommendHouse(page, latitude, longitude, cityId, "", "", "", "");

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                list.clear();
                page=1;
                httpAdcertis(provinceCode, cityCode, countyCode);
                httpRecommendHouse(page, latitude, longitude, cityId, "", "", "", "");
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                httpAdcertis(provinceCode, cityCode, countyCode);
                httpRecommendHouse(page, latitude, longitude, cityId, "", "", "", "");
            }
        });
    }

    private void httpRecommendHouse(int page, String latitude, String longitude, int cityId,
                                    String areaId, String pointType, String keyWord, String distance) {
        if (latitude==null) {
            LocationEntity entity = AppContent.getInstance().getLocation();
            if (entity!=null) {
                latitude = String.valueOf(entity.getLatitude());
                longitude = String.valueOf(entity.getLongitude());
            }
        }
        LogUtils.e(page,latitude,longitude,cityId,areaId,pointType,keyWord,distance);
        NetService.getInstance().setRecommendHouse(page, latitude, longitude, cityId, areaId, pointType, keyWord, distance)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<RecommendHouseEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode());
                        if (ex.getCode()==-102){
                            if (page>1) {
                                ToastUtils.showShort("已经没有更多了");
                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                            }else {
                                rvRecommended.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                            }
                        }else {
                            rvRecommended.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<RecommendHouseEntiy> conentEntity) {
                        hideProgress();
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        if (conentEntity.getContent().size()==0&&page==1){
                            rvRecommended.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                        }else {
                            rvRecommended.setVisibility(View.VISIBLE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            dataAdd(conentEntity);
                        }
                    }
                });


    }

    private void dataAdd(ConentEntity<RecommendHouseEntiy> conentEntity) {
        for (int i = 0; i < conentEntity.getContent().size(); i++) {
            RecommendHouseEntiy queryRecommendEntity = new RecommendHouseEntiy();
            queryRecommendEntity = conentEntity.getContent().get(i);
            list.add(queryRecommendEntity);
        }
        if (rvRecommended == null || page == 1) {
            rvRecommended.setNestedScrollingEnabled(false);
            rvRecommended.setFocusable(false);
            recommendHouseAdapter = new RecommendHouseAdapter((BaseActivity) getActivity(),"2");
            recommendHouseAdapter.addData(list);
            rvRecommended.setAdapter(recommendHouseAdapter);
            rvRecommended.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
             recommendHouseAdapter.setOnItemClickListener(new RecommendHouseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    if (addCellToPlanPresenter != null) {

                        addCellToPlanPresenter.showHomeOagePicker(1, null);
                    }
                }
            });

        } else {
            recommendHouseAdapter.addData(list);
            recommendHouseAdapter.notifyDataSetChanged();
        }
    }

    private void httpAdcertis(String provinceCode, String cityCode, String countyCode) {
        LogUtils.e(provinceCode,cityCode,countyCode);
        NetService.getInstance().setCityFacilty(provinceCode, cityCode, countyCode)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentOdjerEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                    }
                    @Override
                    public void onNext(ConentOdjerEntity conentOdjerEntity) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        CityFacilityInfoEntiy cityFacility = conentOdjerEntity.getPageContent();
                        tvTerminal.setText(cityFacility.terminal);
                        tvClaim.setText(cityFacility.askfor);
                        tvDuration.setText(cityFacility.throwDuration);
                        tvFrequency.setText(cityFacility.exposure);
                        tvWay.setText(cityFacility.way);
                        tvAdTime.setText(cityFacility.adDuration);
                        String str = "低至   ¥ <font color=\"#FF3049\"><big>%s</big></font> 元/台/天";
                        tvCost.setText(Html.fromHtml(String.format(str, cityFacility.price)));
                        tvCityNumber.setText(String.format("当前城市屏幕数量：%s台", cityFacility.facilityNum));
                        LogUtils.e(conentOdjerEntity.getPageContent().toString(), conentOdjerEntity.toString());
                    }
                });

    }


    @Override
    protected void dealLeackCanary() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_select_place, R.id.ll_type, R.id.tv_terminal, R.id.tv_claim,
            R.id.tv_duration, R.id.tv_frequency, R.id.tv_way, R.id.tv_ad_time,
            R.id.tv_cost, R.id.tv_choose_point, R.id.ll_js, R.id.ll_tj,
            R.id.rv_recommended, R.id.ll_view_schedule,R.id.rl_error,R.id.rl_ll_style})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select_place:
                startActivityForResult(CityActivity.class, 8001);
                break;
            case R.id.ll_type:
                break;
            case R.id.tv_terminal:
                break;
            case R.id.tv_claim:
                break;
            case R.id.tv_duration:
                break;
            case R.id.tv_frequency:
                break;
            case R.id.tv_way:
                break;
            case R.id.tv_ad_time:
                break;
            case R.id.tv_cost:
                break;
            case R.id.tv_choose_point:
                Bundle bundle = getSearchBundle();
                bundle.putBoolean(CommonUtil.KEY_VALUE_3, true);
                bundle.putParcelable(CommonUtil.KEY_VALUE_4, locationEntity);
                startActivity(SearchActivity.class, bundle);
                break;
            case R.id.ll_js:
                break;
            case R.id.ll_tj:
                break;
            case R.id.ll_view_schedule:
                if (isToken(getActivity())) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("type","ad");
                    startActivity(PlanNewFragment.class,bundle1);
                }else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.rl_error:
                if (smartRefreshLayout!=null){
                    smartRefreshLayout.autoRefresh(100);
                }else {
                    httpAdcertis(provinceCode, cityCode, countyCode);
                    httpRecommendHouse(1, latitude, longitude, cityId, "", "", "", "");
                }

                break;
            case R.id.rl_ll_style:
                /*查看样式*/
                ActivityUtils.setActivity(ACTIVITY_ADVERTIS_STYLE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e(requestCode, requestCode, data);

        if (BaseActivity.RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case 8001:
                    LocationEntity choosed = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    this.locationEntity  = choosed;
                    LogUtils.e(choosed.getLatitude(), choosed.getLongitude(),
                            choosed.getCity(), choosed.describeContents(),
                            choosed.getCityCode(), choosed.getName());
                    latitude = String.valueOf(choosed.getLatitude());
                    longitude = String.valueOf(choosed.getLongitude());
                    if (choosed != null) {
                        CityUtils.getCityCode(getActivity(), choosed.getCity(), new CityUtils.CityCode() {
                            @Override
                            public void cityCode(int cityCode1) {
                                LogUtils.e();
                                cityCode = String.valueOf(cityCode1) ;
                                cityId = cityCode1;
                                httpAdcertis(provinceCode, cityCode, countyCode);
                                page = 1;
                                list.clear();
                                httpRecommendHouse(page, latitude, longitude, cityId, "", "", "", "");
                            }
                        });
                        tvPlace.setText(choosed.getCity() + "");
                    }
                    break;
            }
        }
    }

    private Bundle getSearchBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonUtil.KEY_VALUE_1, tvPlace.getText().toString());
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
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        LogUtils.d("权限允许onPermissionsGranted：", GsonUtils.toJson(perms));
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                "当前应用缺少必要权限,请手动开启定位权限。\n请点击\"设置\"-\"权限\"-打开所需权限。",
                R.string.setting, R.string.cancel, null, perms)) {
        } else {
            MyToastUtils.showShort("缺少必要权限，无法定位，请重新授权");
            EasyPermissions.requestPermissions(this, "选取点位需要以下权限",
                    1000 + new Random().nextInt(100), positioning);
        }
    }

    @Override
    public void onPermissionsAllGranted() {
        if (isLocationEnabled(getActivity())){
            Bundle bundle = getSearchBundle();
            bundle.putBoolean(CommonUtil.KEY_VALUE_3, true);
            bundle.putParcelable(CommonUtil.KEY_VALUE_4, locationEntity);
            startActivity(SearchActivity.class, bundle);
        }else {
            LocationEnabledUtils.toOpenGPS(getActivity());
        }
    }

}
