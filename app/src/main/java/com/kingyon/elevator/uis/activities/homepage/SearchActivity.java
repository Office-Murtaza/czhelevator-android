package com.kingyon.elevator.uis.activities.homepage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.EventBusConstants;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.EventBusObjectEntity;
import com.kingyon.elevator.entities.HomepageLocationHolder;
import com.kingyon.elevator.entities.KeywordEntity;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.entities.TabEntity;
import com.kingyon.elevator.entities.ToPlanTab;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.uis.fragments.homepage.MapSearchFragment;
import com.kingyon.elevator.uis.fragments.homepage.TextSearchFragment;
import com.kingyon.elevator.uis.pops.CellTypeWindow;
import com.kingyon.elevator.uis.pops.SearchAreaWindow;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.utils.animationutils.AnimatorPath;
import com.kingyon.elevator.utils.animationutils.PathEvaluator;
import com.kingyon.elevator.utils.animationutils.PathPoint;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.leo.afbaselibrary.utils.GlideUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

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


    private String keyWord;
    private AMapCityEntity cityEntity;
    List<NormalParamEntity> areaDatas;
    private String distance;
    private String areaIds;
    private String cellType;

    private boolean mapMode;

    private TextSearchFragment textFragment;
    private MapSearchFragment mapFragment;
    private boolean notFirstIn;

    private CellTypeWindow cellTypeWindow;
    private SearchAreaWindow searchAreaWindow;
    private AnimatorPath path;//声明动画集合
    private String lastPlanType = "";

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
        mapMode = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_3, false);
        return "搜索";
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        tvMapTitle.setText(mapMode ? "列表" : "地图");
        showFragment();
        tvSearchTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imgClear.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
            }
        });

        String name = FormatUtils.getInstance().getCityName(cityEntity.getName());
        if (name != null && name.length() > 5) {
            name = String.format("%s…", name.substring(0, 4));
        }
        tvLocationTitle.setText(name);
        tvSearchTitle.setText(keyWord);
    }

    private void showFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mapFragment = MapSearchFragment.newInstance(cityEntity);
        fragmentTransaction.add(R.id.fl_content, mapFragment);
        textFragment = TextSearchFragment.newInstance(keyWord, cityEntity, distance, areaIds, cellType);
        fragmentTransaction.add(R.id.fl_content, textFragment);
        fragmentTransaction.commit();
    }

    private void updateFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (textFragment != null) {
            fragmentTransaction.hide(textFragment);
            if (!mapMode) {
                fragmentTransaction.show(textFragment);
            }
        }
        if (mapFragment != null) {
            fragmentTransaction.hide(mapFragment);
            if (mapMode) {
                fragmentTransaction.show(mapFragment);
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
            , R.id.iv_gouwuche})
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
                mapMode = !mapMode;
                tvMapTitle.setText(mapMode ? "列表" : "地图");
                updateFragment();
                break;
            case R.id.iv_gouwuche:
                EventBus.getDefault().post(new ToPlanTab(lastPlanType));
                break;
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
                    tvSearchTitle.setText(keyWord != null ? keyWord : "");
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
            textFragment.onParamsChange(keyWord, cityEntity, distance, areaIds, cellType);
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
}
