package com.kingyon.elevator.uis.activities.homepage;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.gjiazhe.wavesidebar.WaveSideBar;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.adapterone.CityAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.LocationUtils;
import com.leo.afbaselibrary.nets.callbacks.AbsAPICallback;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingActivity;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

public class CityActivity extends BaseStateRefreshingActivity implements BaseAdapterWithHF.OnItemClickListener<AMapCityEntity> {

    @BindView(R.id.side_bar)
    WaveSideBar sideBar;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.ed_search)
    EditText etSearch;
    @BindView(R.id.tv_city)
    TextView tvCity;

    private List<AMapCityEntity> cities = new ArrayList<>();
    private List<AMapCityEntity> hots = new ArrayList<>();
    private CityAdapter cityAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected String getTitleText() {
        return "选择城市";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_city;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        if (AppContent.getInstance().getLocation() != null) {
            tvCity.setText(AppContent.getInstance().getLocation().getName());
        } else {
            tvCity.setText("正在定位");
        }
        EventBus.getDefault().register(this);
        cityAdapter = new CityAdapter(this);
        cityAdapter.setOnItemClickListener(this);
        layoutManager = new LinearLayoutManager(this);
        rvContent.setLayoutManager(layoutManager);
        rvContent.setAdapter(cityAdapter);
        rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLayoutRefresh.setEnabled(layoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
        sideBar.setIndexItems("#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        sideBar.setTextColor(R.color.biometricprompt_color_primary);
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                if (TextUtils.equals("#", index)) {
                    rvContent.scrollToPosition(0);
                } else {
                    List<AMapCityEntity> datas = cityAdapter.getDatas();
                    for (int i = 0; i < datas.size(); i++) {
                        AMapCityEntity cityEntity = datas.get(i);
                        if (TextUtils.equals(cityEntity.getAcronyms().toUpperCase(), index)) {
                            rvContent.scrollToPosition(cityAdapter.getHeaderCount() + i);
                            break;
                        }
                    }
                }
            }
        });
        RxTextView.textChanges(etSearch)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<CharSequence>() {
                    @Override
                    public void onNext(CharSequence charSequence) {
                        if (TextUtils.isEmpty(charSequence)) {
                            cityAdapter.refreshDatas(cities);
                        } else {
                            cityAdapter.refreshDatas(getSearchResult(String.valueOf(charSequence)));
                            if (layoutManager.findFirstCompletelyVisibleItemPosition() > cityAdapter.getHeaderCount()) {
                                rvContent.scrollToPosition(cityAdapter.getHeaderCount());
                            }
                        }
                    }

                    @Override
                    protected void onResultError(ApiException ex) {
                        Logger.e(ex.getDisplayMessage());
                    }
                });
    }

    private List<? extends AMapCityEntity> getSearchResult(String search) {
        List<AMapCityEntity> result = new ArrayList<>();
        for (AMapCityEntity entity : cities) {
            if (!TextUtils.isEmpty(entity.getName()) && (entity.getName().contains(search) || entity.getPingYin().contains(search.toLowerCase()))) {
                result.add(entity);
            }
        }
        return result;
    }

    @Override
    public void onRefresh() {
        AppContent.getInstance().getCities()
                .subscribe(new AbsAPICallback<List<AMapCityEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(List<AMapCityEntity> cityEntities) {
                        if (cityEntities == null || cityEntities.size() < 1) {
                            loadingComplete(STATE_EMPTY);
                        } else {
                            hots.clear();
//                            for (AMapCityEntity entity : cityEntities) {
//                                if (entity.isHot()) {
//                                    hots.add(entity);
//                                }
//                            }
                            cities.clear();
                            Collections.sort(cityEntities, new Comparator<AMapCityEntity>() {
                                @Override
                                public int compare(AMapCityEntity o1, AMapCityEntity o2) {
                                    int result;
                                    if (o1 != null && o2 != null && !TextUtils.isEmpty(o1.getPingYin()) && !TextUtils.isEmpty(o2.getPingYin())) {
                                        result = o1.getPingYin().compareTo(o2.getPingYin());
                                    } else {
                                        result = 0;
                                    }
                                    return result;
                                }
                            });
                            cities.addAll(cityEntities);
                            cityAdapter.refreshHot(hots);
                            cityAdapter.refreshDatas(cities);
                            loadingComplete(STATE_CONTENT);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position, AMapCityEntity entity, BaseAdapterWithHF<AMapCityEntity> baseAdaper) {
        if (entity != null && (position == -100 || view.getId() == R.id.tv_name)) {
            onCityChoosed(entity);
        }

        if (entity == null) {
            LocationEntity location = AppContent.getInstance().getLocation();
            switch (view.getId()) {
                case R.id.tv_current:
                    if (location == null) {
                        checkLocation();
                    }
                    break;
                case R.id.tv_city:
                    if (location != null) {
                        onCityChoosed(new AMapCityEntity(location.getCity(),location.getLongitude()+","+location.getLatitude(),location.toString()));
                        LogUtils.e(location.toString());

                    }
                    break;
                case R.id.tv_location:
                    if (location != null) {
                        Intent intent = new Intent();
                        intent.putExtra(CommonUtil.KEY_VALUE_1, AppContent.getInstance().getLocation());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
            }
        }
    }

    private void checkLocation() {
        String[] locationPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        checkPermission(new CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                rvContent.post(new Runnable() {
                    @Override
                    public void run() {
                        LocationUtils.getInstance(CityActivity.this).startLocation();
                    }
                });
            }
        }, "需要允许定位相关权限用于获取地区资讯", locationPermission);
    }

    private void onCityChoosed(AMapCityEntity entity) {//查询城市code
        LogUtils.e(entity.toString());
        if (entity != null) {
            LocationEntity result = new LocationEntity();
            result.setName(FormatUtils.getInstance().getCityName(entity.getName()));
            result.setCity(entity.getName());

            double[] centerPosition = FormatUtils.getInstance().getCenterLatLon(entity.getCenter());
            result.setLongitude(centerPosition[0] == 0 ? null : centerPosition[0]);
            result.setLatitude(centerPosition[1] == 0 ? null : centerPosition[1]);

            result.setCityCode(entity.getAdcode());
            Intent intent = new Intent();
            intent.putExtra(CommonUtil.KEY_VALUE_1, result);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @OnClick(R.id.ll_city)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.putExtra(CommonUtil.KEY_VALUE_1, AppContent.getInstance().getLocation());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshLocation(LocationEntity entity) {
        if (AppContent.getInstance().getLocation() != null) {
            tvCity.setText(AppContent.getInstance().getLocation().getName());
        } else {
            tvCity.setText("定位失败");
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
