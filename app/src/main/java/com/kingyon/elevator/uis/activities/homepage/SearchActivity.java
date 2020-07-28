package com.kingyon.elevator.uis.activities.homepage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.donkingliang.labels.LabelsView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.KeywordEntity;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.entities.ToPlanTab;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.PointClassicEntiy;
import com.kingyon.elevator.entities.entities.RecommendHouseEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.fragments.homepage.MapSearchFragment;
import com.kingyon.elevator.uis.fragments.homepage.TextSearchTwoFragment;
import com.kingyon.elevator.uis.fragments.main.PlanNewFragment;
import com.kingyon.elevator.uis.pops.CellTypeWindow;
import com.kingyon.elevator.uis.pops.SearchAreaWindow;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.utils.animationutils.AnimatorPath;
import com.kingyon.elevator.utils.animationutils.PathEvaluator;
import com.kingyon.elevator.utils.animationutils.PathPoint;
import com.kingyon.elevator.utils.utilstwo.AdUtils;
import com.kingyon.elevator.utils.utilstwo.JsonUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class SearchActivity extends BaseSwipeBackActivity {

    @BindView(R.id.tv_location_title)
    TextView tvLocationTitle;
    @BindView(R.id.tv_search_title)
    TextView tvSearchTitle;
    @BindView(R.id.img_clear)
    ImageView imgClear;
    @BindView(R.id.tv_map_title)
    TextView tvMapTitle;
    @BindView(R.id.ctv_city_area)
    CheckedTextView ctvCityArea;
    @BindView(R.id.ctv_cell_type)
    CheckedTextView ctvCellType;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.iv_home_logo)
    ImageView iv_home_logo;
    @BindView(R.id.iv_gouwuche)
    ImageView iv_gouwuche;
    @BindView(R.id.pre_v_back)
    ImageView preVBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.ll_city_area)
    LinearLayout llCityArea;
    @BindView(R.id.ll_cell_type)
    LinearLayout llCellType;
    @BindView(R.id.tv_mag_bottom)
    TextView tvMagBottom;
    @BindView(R.id.tv_list_title)
    TextView tvListTitle;
    @BindView(R.id.tv_list_bottom)
    TextView tvListBottom;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.tv_bumber)
    TextView tvBumber;
    @BindView(R.id.rl_plan)
    RelativeLayout rlPlan;
    @BindView(R.id.img_menu)
    ImageView imgMenu;
    @BindView(R.id.labels)
    LabelsView labels;
    @BindView(R.id.labels2)
    LabelsView labels2;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    List<PointClassicEntiy> list;
    List<PointClassicEntiy.ChildBean> childBeanList;
    List<PointClassicEntiy.ChildBean> childBeanList1 = new ArrayList<>();
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;

    private String keyWord;
    private AMapCityEntity cityEntity;
    List<NormalParamEntity> areaDatas;
    private String distance;
    private String areaIds;
    private String cellType;

    private boolean mapMode;

    private TextSearchTwoFragment textFragment;
    private MapSearchFragment mapFragment;
    private boolean notFirstIn;

    private CellTypeWindow cellTypeWindow;
    private SearchAreaWindow searchAreaWindow;
    private AnimatorPath path;//声明动画集合
    private String lastPlanType = "";

    private double latitude = 26.617327;
    private double longitude = 106.648644;
    private boolean isconent = true;
    ConentEntity<RecommendHouseEntiy> entiyConentEntity;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (AdUtils.planNumber > 0) {
                tvBumber.setText(AdUtils.planNumber + "");
                tvBumber.setVisibility(View.VISIBLE);
            }
            handler.postDelayed(this, 500);
        }
    };
    LocationEntity locationEntity;

    @Override
    public int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected String getTitleText() {
        keyWord = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        if (keyWord == null) {
            keyWord = "";
        }
        EventBus.getDefault().post(new KeywordEntity(keyWord));
        cityEntity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_2);
        locationEntity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_4);
        mapMode = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_3, false);
        if (locationEntity != null) {
            latitude = locationEntity.getLatitude();
            longitude = locationEntity.getLongitude();
            LogUtils.e(latitude, longitude);
        }
        return "搜索";
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        handler.postDelayed(runnable, 500);
//        LocationEntity entity = AppContent.getInstance().getLocation();
//        if (entity!=null) {
//            latitude = entity.getLatitude();
//            longitude = entity.getLongitude();
//        }
        /*定位请求*/
        httpRecommendHouse(0, String.valueOf(latitude), String.valueOf(longitude), 0, "", "", "", "");
        EventBus.getDefault().register(this);
        /*侧滑内容*/
        initData();
        AdUtils.httpPlannuber();


        String name = FormatUtils.getInstance().getCityName(cityEntity.getName());
        if (name != null && name.length() > 5) {
            name = String.format("%s…", name.substring(0, 4));
        }
        tvLocationTitle.setText(name);
        tvSearchTitle.setText(keyWord);
    }

    private void httpRecommendHouse(int page, String latitude, String longitude, int cityId,
                                    String areaId, String pointType, String keyWord, String distance) {
        LogUtils.e(page, latitude, longitude, cityId, areaId, pointType, keyWord, distance);
        showProgressDialog("加载中....");
        NetService.getInstance().setRecommendHouse(page, latitude, longitude, cityId, areaId, pointType, keyWord, distance)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<RecommendHouseEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        hideProgress();
                        flContent.setVisibility(View.GONE);
                        imgMenu.setVisibility(View.GONE);
                        rlError.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onNext(ConentEntity<RecommendHouseEntiy> conentEntity) {
                        LogUtils.e(conentEntity.toString(), conentEntity.getContent().toString());
                        flContent.setVisibility(View.VISIBLE);
                        rlError.setVisibility(View.GONE);
                        imgMenu.setVisibility(View.VISIBLE);
                        entiyConentEntity = conentEntity;
                        hideProgress();
                        showFragment();
                    }
                });
    }

    private void initData() {
        NetService.getInstance().setPointClassic()
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<PointClassicEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                    }

                    @Override
                    public void onNext(ConentEntity<PointClassicEntiy> conentEntity) {
                        LogUtils.e(conentEntity.getContent().toString(), conentEntity.getContent().get(0).child.toString());
                        list = new ArrayList<>();
                        PointClassicEntiy pointClassicEntiy = new PointClassicEntiy();
                        pointClassicEntiy.id = 0;
                        pointClassicEntiy.pointName = "不限";
                        pointClassicEntiy.level = "1";
                        pointClassicEntiy.parent = 1;
                        pointClassicEntiy.child = childBeanList1;
                        list.add(pointClassicEntiy);
                        for (int i = 0; i < conentEntity.getContent().size(); i++) {
                            list.add(conentEntity.getContent().get(i));
                        }
                        labels.setLabels(list, new LabelsView.LabelTextProvider<PointClassicEntiy>() {
                            @Override
                            public CharSequence getLabelText(TextView label, int position, PointClassicEntiy data) {
                                //根据data和position返回label需要显示的数据。
                                return data.pointName;
                            }
                        });
                        labels.setSelects(0);
                        labels.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
                            @Override
                            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                                childBeanList = new ArrayList<>();
                                if (list.get(position).child != null) {
                                    for (int c = 0; c < list.get(position).child.size(); c++) {
                                        childBeanList.add(list.get(position).child.get(c));
                                    }
                                    labels2.setLabels(childBeanList, new LabelsView.LabelTextProvider<PointClassicEntiy.ChildBean>() {
                                        @Override
                                        public CharSequence getLabelText(TextView label, int position, PointClassicEntiy.ChildBean data) {
                                            //根据data和position返回label需要显示的数据。
                                            return data.pointName;
                                        }
                                    });
                                    labels2.setSelects(0);
                                }
                            }
                        });

                    }
                });

    }

    private void showFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (isconent) {
            LogUtils.e(latitude, longitude);
            fragmentTransaction.add(R.id.fl_content, new MapSearchFragment().newInstance(cityEntity, entiyConentEntity, latitude, longitude));
        } else {
            fragmentTransaction.add(R.id.fl_content, new TextSearchTwoFragment().newInstance(entiyConentEntity));
        }
        fragmentTransaction.commit();
    }

    private void updateFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (textFragment != null) {
            fragmentTransaction.hide(textFragment);
            if (!mapMode) {
                fragmentTransaction.show(textFragment);
                tvListTitle.setTextColor(Color.parseColor("#000000"));
                tvMapTitle.setTextColor(Color.parseColor("#666666"));
                tvMagBottom.setVisibility(View.GONE);
                tvListBottom.setVisibility(View.VISIBLE);
            }
        }
        if (mapFragment != null) {
            fragmentTransaction.hide(mapFragment);
            if (mapMode) {
                fragmentTransaction.show(mapFragment);
                tvMagBottom.setVisibility(View.VISIBLE);
                tvListBottom.setVisibility(View.GONE);
                tvListTitle.setTextColor(Color.parseColor("#666666"));
                tvMapTitle.setTextColor(Color.parseColor("#000000"));
            }
        }
        fragmentTransaction.commit();
    }

    public void onSearchResult(List<CellItemEntity> cellEntities) {
        if (cellEntities != null) {
            updateMapFragment(cellEntities);
        }
        hideProgress();
    }

    private void updateMapFragment(List<CellItemEntity> cellEntities) {
        if (mapFragment != null) {
            mapFragment.onParamsChange(cellEntities, cityEntity);
        }
    }

    @Override
    protected void onResume() {
        if (!notFirstIn) {
            notFirstIn = true;
            updateFragment();
            if (mapMode) {
                showProgressDialog(getString(R.string.wait));
            }
        }
        super.onResume();
    }

    @OnClick({R.id.ll_city_area, R.id.ll_cell_type, R.id.tv_location_title, R.id.tv_search_title, R.id.img_clear, R.id.tv_map_title
            , R.id.iv_gouwuche, R.id.tv_list_title, R.id.rl_plan, R.id.img_menu, R.id.tv_reset, R.id.tv_confirm,R.id.rl_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_city_area:
                showSearchAreaWindow();
                break;
            case R.id.ll_cell_type:
                showCellTypeWindow();
                break;
            case R.id.tv_location_title:
                startActivityForResult(CityActivity.class, 8001);
                break;
            case R.id.tv_search_title:
                Bundle bundle = new Bundle();
                bundle.putString(CommonUtil.KEY_VALUE_1, tvSearchTitle.getText().toString());
                bundle.putParcelable(CommonUtil.KEY_VALUE_2, new AMapCityEntity(cityEntity.getName(), cityEntity.getAdcode()));
                bundle.putBoolean(CommonUtil.KEY_VALUE_3, true);
                startActivityForResult(SearchHistoryActivity.class, 8002, bundle);
                break;
            case R.id.img_clear:
                tvSearchTitle.setText("");
                keyWord = "";
                updateDatas();
                break;
            case R.id.tv_map_title:
                isconent = true;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fl_content, new MapSearchFragment().newInstance(cityEntity, entiyConentEntity, latitude, longitude));
                fragmentTransaction.commit();
                tvMagBottom.setVisibility(View.VISIBLE);
                tvListBottom.setVisibility(View.GONE);
                tvListTitle.setTextColor(Color.parseColor("#90666666"));
                tvMapTitle.setTextColor(Color.parseColor("#000000"));

                break;
            case R.id.tv_list_title:
                isconent = false;
                tvListTitle.setTextColor(Color.parseColor("#000000"));
                tvMapTitle.setTextColor(Color.parseColor("#90666666"));
                tvMagBottom.setVisibility(View.GONE);
                tvListBottom.setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.add(R.id.fl_content, new TextSearchTwoFragment().newInstance(entiyConentEntity));
                fragmentTransaction1.commit();
                break;
            case R.id.iv_gouwuche:
                EventBus.getDefault().post(new ToPlanTab(lastPlanType));
                break;
            case R.id.rl_plan:
                if (isToken(this)) {
                    startActivity(PlanNewFragment.class);
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.img_menu:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.tv_reset:
                labels.clearAllSelect();
                labels2.clearAllSelect();
                break;
            case R.id.tv_confirm:
                drawerLayout.closeDrawer(Gravity.LEFT);
                PointClassicEntiy pointClassicEntiy = new PointClassicEntiy();
                try {
                    JSONArray jsonArray = new JSONArray(JsonUtils.beanToJson(labels.getSelectLabelDatas()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    pointClassicEntiy.pointName = jsonObject.optString("pointName");
                    pointClassicEntiy.id = jsonObject.optInt("id");
                    pointClassicEntiy.level = jsonObject.optString("level");
                    pointClassicEntiy.parent = jsonObject.optInt("parent");
                    JSONArray jsonArray1 = new JSONArray(JsonUtils.beanToJson(labels2.getSelectLabelDatas()));
                    LogUtils.e(jsonArray1.length(), jsonArray1);
                    List<PointClassicEntiy.ChildBean> list = new ArrayList<>();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        PointClassicEntiy.ChildBean childBean = new PointClassicEntiy.ChildBean();
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        LogUtils.e(jsonObject1.optString("pointName"));
                        childBean.pointName = jsonObject1.optString("pointName");
                        childBean.id = jsonObject1.optInt("id");
                        childBean.level = jsonObject1.optString("level");
                        childBean.parent = jsonObject1.optInt("parent");
                        list.add(childBean);
                    }
                    pointClassicEntiy.child = list;
                    LogUtils.e(JsonUtils.beanToJson(pointClassicEntiy));
                    httpRecommendHouse(0, String.valueOf(latitude), String.valueOf(longitude), 0, "", JsonUtils.beanToJson(pointClassicEntiy), "", "");

                } catch (JSONException e) {
                    LogUtils.e(e.toString());
                    e.printStackTrace();
                }
                break;
            case R.id.rl_error:
                httpRecommendHouse(0, String.valueOf(latitude), String.valueOf(longitude), 0, "", "", "", "");
                break;
            default:
        }
    }

    private void showSearchAreaWindow() {
        if (searchAreaWindow == null) {
            searchAreaWindow = new SearchAreaWindow(this, new SearchAreaWindow.OnAreaResultListener() {
                @Override
                public void onAreaResult(String distanceNames, String distanceTypes, String areaNames, String areaCodes) {
                    distance = distanceTypes;
                    areaIds = areaCodes;

                    boolean noChoose = TextUtils.isEmpty(distanceTypes) && TextUtils.isEmpty(areaCodes);
                    ctvCityArea.setText(noChoose ? "投放区域" : String.format("%s%s", TextUtils.isEmpty(distanceTypes) ? "" : distanceNames, TextUtils.isEmpty(areaCodes) ? "" : areaNames));
                    ctvCityArea.setSelected(!noChoose);
                    ctvCityArea.setChecked(false);
                    updateDatas();
                }
            });
        }
        if (areaDatas != null) {
            searchAreaWindow.show(this, cityEntity, distance, areaDatas, vLine);
            ctvCityArea.setChecked(true);
        } else {
            getCityAreas(true);
        }
    }

    private void showCellTypeWindow() {
        if (cellTypeWindow == null) {
            cellTypeWindow = new CellTypeWindow(this, new CellTypeWindow.OnCellTypeResultListener() {
                @Override
                public void onCellTypResult(String names, String types) {
                    cellType = types;
                    boolean hasChoose = !TextUtils.isEmpty(types);
                    ctvCellType.setText(hasChoose ? names : "小区类型");
                    ctvCellType.setSelected(hasChoose);
                    ctvCellType.setChecked(false);
                    updateDatas();
                }
            });
        }
        cellTypeWindow.showAsDropDown(vLine);
        ctvCellType.setChecked(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case 8001:
                    LocationEntity choosed = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (choosed != null && !TextUtils.equals(choosed.getCity(), cityEntity.getName())) {

                        //清空行政区数据
                        areaDatas = null;

                        //重置参数
                        cityEntity = new AMapCityEntity();
                        cityEntity.setName(choosed.getCity());
                        cityEntity.setAdcode(TextUtils.isEmpty(choosed.getCityCode()) ? "" : choosed.getCityCode());

                        String name = cityEntity.getName();
                        if (name != null && name.length() > 5) {
                            name = String.format("%s…", name.substring(0, 4));
                        }
                        tvLocationTitle.setText(name);
                        if (searchAreaWindow != null) {
                            searchAreaWindow.clearAreas();
                        }
                        areaIds = "";
                        distance = "";

                        //重置投放区域显示
                        ctvCityArea.setChecked(false);
                        ctvCityArea.setSelected(false);
                        ctvCityArea.setText("投放区域");

                        //更新数据
                        updateDatas();
                        //获取新的下级行政区数据
                        getCityAreas(false);

                        //发送新的搜索城市
//                        EventBus.getDefault().post(new HomepageLocationHolder(choosed));
                    }
                    break;
                case 8002:
                    keyWord = data.getStringExtra(CommonUtil.KEY_VALUE_1);
                    String la = data.getStringExtra(CommonUtil.KEY_VALUE_2);
                    String lo = data.getStringExtra(CommonUtil.KEY_VALUE_3);
                    tvSearchTitle.setText(keyWord != null ? keyWord : "");
                    longitude = Double.parseDouble(lo);
                    latitude = Double.parseDouble(la);
                    if (longitude != 0) {
                        httpRecommendHouse(0, String.valueOf(latitude), String.valueOf(longitude), 0, "", "", "", "");
                    }
                    updateDatas();
                    break;
            }
        }
    }

    private void updateDatas() {
        if (textFragment != null) {
            if (mapMode) {
                showProgressDialog(getString(R.string.wait));
            }
            EventBus.getDefault().post(new KeywordEntity(keyWord));
//            textFragment.onParamsChange(keyWord, cityEntity, distance, areaIds, cellType);
        }
    }

    private void getCityAreas(final boolean open) {
        Observable.zip(Observable.just(cityEntity), AppContent.getInstance().getCities(), new Func2<AMapCityEntity, List<AMapCityEntity>, List<NormalParamEntity>>() {
            @Override
            public List<NormalParamEntity> call(AMapCityEntity cityEntity, List<AMapCityEntity> aMapCityEntities) {
                List<NormalParamEntity> areaDatas = new ArrayList<>();

                /*------------------解析本地文件获取区划信息------------------*/
                AMapCityEntity curCity = null;
                if (aMapCityEntities != null) {
                    for (AMapCityEntity city : aMapCityEntities) {
                        if (TextUtils.equals(city.getAdcode(), cityEntity.getAdcode()) || TextUtils.equals(city.getName(), cityEntity.getName())) {
                            curCity = city;
                            break;
                        }
                    }
                }

                List<AMapCityEntity> areaCities = curCity == null ? null : curCity.getDistricts();
                if (areaCities != null) {
                    for (AMapCityEntity areaCity : areaCities) {
                        areaDatas.add(new NormalParamEntity(areaCity.getAdcode(), areaCity.getName()));
                    }
                }
                return areaDatas;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<NormalParamEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<List<NormalParamEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        areaDatas = null;
                        if (open) {
                            showToast("查找行政区异常");
                        }
                    }

                    @Override
                    public void onNext(List<NormalParamEntity> areas) {
                        if (areas == null || areas.size() < 1) {
                            throw new ResultException(9003, "查找行政区异常");
                        }
                        areas.add(0, new NormalParamEntity("", "不限"));
                        areaDatas = areas;
                        if (open) {
                            searchAreaWindow.show(SearchActivity.this, cityEntity, distance, areaDatas, vLine);
                            ctvCityArea.setChecked(true);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addToCartSuccess(EventBusObjectEntity eventBusObjectEntity) {
        if (eventBusObjectEntity.getEventCode() == EventBusConstants.AddToCartToPlanSuccess) {
            LogUtils.d("添加到列表成功，开始动画---");
            lastPlanType = (String) eventBusObjectEntity.getData();
            startAddPlanAnimation();
        }
    }

    /**
     * 执行加入购物车的动画
     */
    public void startAddPlanAnimation() {
        if (RuntimeUtils.mapOrListAddPositionAnimation != null) {
            if (RuntimeUtils.animationImagePath != null) {
                GlideUtils.loadImage(this, RuntimeUtils.animationImagePath, iv_home_logo);
            }
            iv_home_logo.setX(DensityUtil.dip2px(16));
            iv_home_logo.setY(RuntimeUtils.mapOrListAddPositionAnimation[1] - DensityUtil.dip2px(50));
            iv_home_logo.setVisibility(View.VISIBLE);
            path = new AnimatorPath();
            path.moveTo(DensityUtil.dip2px(16), RuntimeUtils.mapOrListAddPositionAnimation[1] - DensityUtil.dip2px(50));
            path.secondBesselCurveTo(ScreenUtils.getScreenWidth() / 2, RuntimeUtils.mapOrListAddPositionAnimation[1] - 100,
                    ScreenUtils.getScreenWidth() - DensityUtil.dip2px(55),
                    ScreenUtils.getScreenHeight() - DensityUtil.dip2px(170));
            ObjectAnimator anim = ObjectAnimator.ofObject(this, "addPlan", new PathEvaluator(), path.getPoints().toArray());
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(1000);
            anim.start();
            iv_home_logo.animate().scaleX(0.1f).scaleY(0.1f).setDuration(1000).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    iv_home_logo.setVisibility(View.GONE);
                    iv_home_logo.setScaleX(1f);
                    iv_home_logo.setScaleY(1f);
                    iv_gouwuche.setImageResource(R.mipmap.gouwuche);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
            RuntimeUtils.mapOrListAddPositionAnimation = null;
        }
    }

    public void setAddPlan(PathPoint newLoc) {
        iv_home_logo.setTranslationX(newLoc.mX);
        iv_home_logo.setTranslationY(newLoc.mY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

}
